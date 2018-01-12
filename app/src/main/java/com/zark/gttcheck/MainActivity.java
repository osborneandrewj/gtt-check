package com.zark.gttcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zark.gttcheck.adapters.GttCaseOverviewAdapter;
import com.zark.gttcheck.adapters.GttCaseViewHolder;
import com.zark.gttcheck.models.GttCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1886;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCasesDatabaseReference;
    private ChildEventListener mCaseListener;

    // Case overview recyclerview
    private RecyclerView.LayoutManager mCasesLayoutManager;
    private GttCaseOverviewAdapter mAdapter;
    private ArrayList<GttCase> mCaseList;
    private FirebaseRecyclerAdapter<GttCase, GttCaseViewHolder> mCasesOververRVAdapter;
    private GttCaseViewHolder.OnCaseSelectedListener mOnCaseSelectedListener;
    private int mRVBackgroundColorCount = 0;

    // Authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

    private FragmentManager mFragmentManager;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.rv_cases_overview) RecyclerView mCasesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new EmptyLoggingTree());
        }

        // Initialize Firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideGttCaseList();

                AddCaseFragment fragment = new AddCaseFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.add(R.id.frag_container, fragment).addToBackStack("yo").commit();
            }
        });

        mCasesLayoutManager = new LinearLayoutManager(this);
        mCaseList = new ArrayList<>();
        //mAdapter = new GttCaseOverviewAdapter(this, mCaseList);

        mFragmentManager = getSupportFragmentManager();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize();
                    // User is signed in
                    Timber.d("User is signed in!");
                } else {
                    onSignedOutHideUI();

                    Timber.e("User is not signed in. Starting login UI...");
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                onSignedInInitialize();
                Timber.e("User is signed in!");
                Timber.e("Result Code: %s", resultCode);
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled the sign in process. Exit.
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onSignedInInitialize() {
        Timber.e("Initializing...");
        mCasesRecyclerView.setVisibility(View.VISIBLE);
        setupFirebaseAdapter();
    }

    public void onSignedOutHideUI() {
        mCasesRecyclerView.setVisibility(View.GONE);
    }

    public void hideGttCaseList() {
        mCasesRecyclerView.setVisibility(View.GONE);
    }

    public void setupFirebaseAdapter() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        mCasesDatabaseReference = mFirebaseDatabase.getReference()
                .child("users")
                .child(userId)
                .child("cases");

        Query query = mCasesDatabaseReference.limitToLast(50);

        FirebaseRecyclerOptions<GttCase> options =
                new FirebaseRecyclerOptions.Builder<GttCase>()
                .setQuery(query, GttCase.class)
                .build();

        mCasesOververRVAdapter = new

        mCasesOververRVAdapter.startListening();

        mCasesRecyclerView.setAdapter(mCasesOververRVAdapter);
        mCasesRecyclerView.setLayoutManager(mCasesLayoutManager);
        mCasesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_sign_out) {
            AuthUI.getInstance()
                    .signOut(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * If a fragment is in the UI, remove the fragment and show the RecyclerView
     */
    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStackImmediate();
            mCasesRecyclerView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (mCasesOververRVAdapter != null) {
            if (mCasesOververRVAdapter.hasObservers()) {
                mCasesOververRVAdapter.stopListening();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
