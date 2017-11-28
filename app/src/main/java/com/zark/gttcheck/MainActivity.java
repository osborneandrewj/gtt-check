package com.zark.gttcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zark.gttcheck.adapters.CaseOverviewAdapter;
import com.zark.gttcheck.models.CaseOverviewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements CaseOverviewAdapter.CaseOnClickHandler {

    private static final int RC_SIGN_IN = 1886;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCasesDatabaseReference;
    private ChildEventListener mCaseListener;

    // Case overview recyclerview
    private RecyclerView.LayoutManager mCasesLayoutManager;
    private CaseOverviewAdapter mAdapter;
    private ArrayList<CaseOverviewItem> mCaseList;

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        Timber.e("User ID: %s", userId);
        mCasesDatabaseReference = mFirebaseDatabase.getReference()
                .child("users")
                .child(userId)
                .child("cases");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCasesDatabaseReference.push().setValue("hey");
                CaseOverviewItem newCase = new CaseOverviewItem(244, 13, 77);
                mCasesDatabaseReference.push().setValue(newCase);
                //mCaseList.add(new CaseOverviewItem(275, 13, 4));
                //mAdapter.setNewDataSet(mCaseList);
            }
        });

        // Cases RecyclerView
        mCasesRecyclerView.setHasFixedSize(true);
        mCasesLayoutManager = new LinearLayoutManager(this);
        mCasesRecyclerView.setLayoutManager(mCasesLayoutManager);
        mCaseList = new ArrayList<>();
        mAdapter = new CaseOverviewAdapter(this, mCaseList, this);
        mCasesRecyclerView.setAdapter(mAdapter);

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
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseReadListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * When a RecyclerView item is clicked:
     * 1. Hide RecyclerView
     * 2. Inflate the fragment to a FrameLayout
     */
    @Override
    public void onCaseClick() {
        Timber.e("I've been clicked...");

        // Hide the RecyclerView
        mCasesRecyclerView.setVisibility(View.GONE);

        // Inflate fragment
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        CaseFragment caseFragment = new CaseFragment();
        transaction.add(R.id.frag_container, caseFragment).addToBackStack("tag").commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                onSignedInInitialize();
                Timber.d("User is signed in!");
                Timber.d("Result Code: %s", resultCode);
            } else if (resultCode == RESULT_CANCELED) {
                // User canceled the sign in process. Exit.
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onSignedInInitialize() {
        mCasesRecyclerView.setVisibility(View.VISIBLE);
        attachDatabaseReadListener();
    }

    public void onSignedOutHideUI() {
        mCasesRecyclerView.setVisibility(View.GONE);
    }

    public void attachDatabaseReadListener() {
        if (mCaseListener == null) {
            mCaseListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    CaseOverviewItem newCase = dataSnapshot.getValue(CaseOverviewItem.class);
                    mCaseList.add(newCase);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    CaseOverviewItem newCase = dataSnapshot.getValue(CaseOverviewItem.class);
                    mCaseList.add(newCase);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mCasesDatabaseReference.addChildEventListener(mCaseListener);
        }
    }

    public void detachDatabaseReadListener() {
        mCasesDatabaseReference.removeEventListener(mCaseListener);
        Timber.e("removed event listener!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
}
