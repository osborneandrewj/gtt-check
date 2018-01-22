package com.zark.gttcheck;


import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zark.gttcheck.adapters.IvGroupListAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.IvGroupRx;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFragment extends Fragment implements IvGroupListAdapter.OnIvGroupSelectedListener {

    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String USER_NAME_KEY = "userNameKey";
    private static final String CASE_REF = "caseRef";
    private static final String RX_COUNT_KEY = "rxCount";
    private static String IV_GROUP_KEY = "ivGroupKey";

    // IV Group RecyclerView
    private RecyclerView.LayoutManager mIvGroupLayoutManager;
    private IvGroupListAdapter mAdapter;
    private DatabaseReference mCaseDatabase;

    private String mUserId;
    private String mCaseRef;

    @BindView(R.id.header_case_details) ConstraintLayout mCaseHeader;
    @BindView(R.id.rv_iv_groups) RecyclerView mRecyclerView;
    @BindView(R.id.tv_details_count_rx) TextView mRxCount;
    @BindView(R.id.tv_details_count_iv) TextView mIvCount;
    @BindView(R.id.tv_details_id) TextView mCaseId;
    @BindView(R.id.fab_case) FloatingActionButton mFab;

    public CaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_case, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String transitionName = bundle.getString(TRANSITION_NAME_KEY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCaseHeader.setTransitionName(transitionName);
                Timber.e("Message received: %s", transitionName);
            }
            mUserId = bundle.getString(USER_NAME_KEY);
            mCaseRef = bundle.getString(CASE_REF);
        }

        // Make sure that Firebase has a value for the list of IV groups
        mCaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mUserId)
                .child("cases")
                .child(mCaseRef);

        // Add data to header once
        // Note: This does not update dynamically
        mCaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GttCase currentCase = dataSnapshot.getValue(GttCase.class);
                mRxCount.setText(String.valueOf(currentCase.getRxCount()));
                mIvCount.setText(String.valueOf(currentCase.getIvCount()));
                mCaseId.setText(String.valueOf(currentCase.getIdNumber()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Timber.e("RxCount: %s", mCaseDatabase.child(RX_COUNT_KEY));

        mFab.setOnClickListener(new View.OnClickListener() {

            //TODO: Fix this nonsense
            @Override
            public void onClick(View view) {
                //IvGroupRx newMed = new IvGroupRx("SomethingOrRather", true, null);

                IV_GROUP_KEY = mCaseDatabase.child("iv_groups").push().getKey();
                IvGroup newIv = new IvGroup("One", IV_GROUP_KEY);
                mCaseDatabase.child("iv_groups").child(IV_GROUP_KEY).setValue(newIv);
            }
        });

        startRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void startRecyclerView() {

        DatabaseReference ivGroupsDatabase = mCaseDatabase.child("iv_groups");

        Query query = ivGroupsDatabase.limitToLast(50);

        FirebaseRecyclerOptions<IvGroup> options =
                new FirebaseRecyclerOptions.Builder<IvGroup>()
                .setQuery(query, IvGroup.class)
                .build();

        mAdapter = new IvGroupListAdapter(options, this);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
        mIvGroupLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mIvGroupLayoutManager);
    }

    @Override
    public void onIvGroupSelected(View view, int position) {
        //Toast.makeText(getContext(), "Hey", Toast.LENGTH_SHORT).show();
        Timber.e("From the case...");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            if (mAdapter.hasObservers()) {
                mAdapter.stopListening();
            }
        }
    }
}
