package com.zark.gttcheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.zark.gttcheck.adapters.CaseRecyclerAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.utilities.MyDbUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements CaseRecyclerAdapter.OnCaseSelectedListener {

    private static final int RC_SIGN_IN = 1886;
    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String CASE_REF = "caseRef";

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private String mUserId;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    // Case overview recyclerview
    private RecyclerView.LayoutManager mCasesLayoutManager;
    private CaseRecyclerAdapter mAdapter;

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
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        // Add user ID to preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(MyDbUtils.USER_ID_KEY, mUserId).apply();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                GttCase newCase = new GttCase(233, 5, 8, null);
//
//                DocumentReference newCaseRef = MyDbUtils.getNewCaseDbRef(mUserId);
//                newCase.setReference(newCaseRef.getId());
//                MyDbUtils.getUserDbRef(mUserId)
//                        .collection(MyDbUtils.CASE_DIR)
//                        .document(newCaseRef.getId())
//                        .set(newCase);

                hideCaseList();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                AddCaseFrag frag = new AddCaseFrag();
                transaction.replace(R.id.frag_container, frag).addToBackStack("heythere")
                .commit();

            }
        });

        mCasesLayoutManager = new LinearLayoutManager(this);
        mFragmentManager = getSupportFragmentManager();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize();
                    setupFirebaseAdapter();
                } else {
                    onSignedOutHideUI();
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(), RC_SIGN_IN);
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
        mCasesRecyclerView.setVisibility(View.VISIBLE);
    }

    public void onSignedOutHideUI() {
        mCasesRecyclerView.setVisibility(View.GONE);
    }

    public void hideCaseList() {
        mCasesRecyclerView.animate().alpha(0.0f).setDuration(1000);
        mCasesRecyclerView.setVisibility(View.GONE);
        mCasesRecyclerView.animate().alpha(1.0f);
        fab.setVisibility(View.GONE);
    }

    public void setupFirebaseAdapter() {

        com.google.firebase.firestore.Query query = MyDbUtils.getUserDbRef(mUserId)
                .collection(MyDbUtils.CASE_DIR);

        FirestoreRecyclerOptions<GttCase> options = new FirestoreRecyclerOptions.Builder<GttCase>()
                .setQuery(query, GttCase.class)
                .build();

        mAdapter = new CaseRecyclerAdapter(options, this, this);

        mAdapter.startListening();

        mCasesRecyclerView.setAdapter(mAdapter);
        mCasesRecyclerView.setLayoutManager(mCasesLayoutManager);
        mCasesRecyclerView.setVisibility(View.VISIBLE);

        int animId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation =
                AnimationUtils.loadLayoutAnimation(this, animId);
        mCasesRecyclerView.setLayoutAnimation(animation);
    }

    @Override
    public void onCaseClicked(View view, int position, String ref) {
        // Define common transition name
        CardView cardView = view.findViewById(R.id.card_view_case);
        String transitionName = "transitionName" + position;
        cardView.setTransitionName(transitionName);

        Bundle bundle = new Bundle();
        bundle.putString(TRANSITION_NAME_KEY, transitionName);
        bundle.putString(CASE_REF, ref);

        CaseFrag caseFrag = new CaseFrag();
        caseFrag.setArguments(bundle);

        // Transition for entering fragment
        Slide slideTransition = new Slide(Gravity.TOP);
        slideTransition.setDuration(500);

        // Transition for shared element only
        ChangeBounds changeBoundsTransition = new ChangeBounds();

        // Hide RecyclerView
        hideCaseList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            caseFrag.setSharedElementEnterTransition(new ChangeBounds());
            caseFrag.setEnterTransition(slideTransition);
            caseFrag.setExitTransition(new Fade());
            caseFrag.setSharedElementReturnTransition(new ChangeBounds());
        }

        this.getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(cardView, transitionName)
                .replace(R.id.frag_container, caseFrag)
                .addToBackStack("heya")
                .commit();
    }

    /**
     * Test method
     * TODO: Remove this method
     */
    private void runRecyclerLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context,
                R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

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
            fab.setVisibility(View.VISIBLE);
            runRecyclerLayoutAnimation(mCasesRecyclerView);
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
        if (mAdapter != null) {
            if (mAdapter.hasObservers()) {
                mAdapter.stopListening();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        // If there is a fragment shown, reload this fragment
        if (mFragmentManager.getBackStackEntryCount() != 0) {
            hideCaseList();
        }
    }
}
