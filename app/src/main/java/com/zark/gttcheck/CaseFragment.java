package com.zark.gttcheck;


import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zark.gttcheck.adapters.IvGroupRecyclerAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.Rx;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFragment extends Fragment implements IvGroupRecyclerAdapter.OnIvGroupSelectedListener {

    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String USER_NAME_KEY = "userNameKey";
    private static final String CASE_REF = "caseRef";
    private static final String RX_COUNT_KEY = "rxCount";
    private static String IV_GROUP_KEY = "ivGroupKey";
    private Boolean mIsFabFocused = true;

    // IV Group RecyclerView
    private RecyclerView.LayoutManager mIvGroupLayoutManager;
    private IvGroupRecyclerAdapter mAdapter;
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

        mFab.setOnClickListener(new View.OnClickListener() {

            //TODO: Fix this nonsense
            @Override
            public void onClick(View view) {

                // List of medications
                Rx newRx = new Rx("Medication", false);
                ArrayList<Rx> list = new ArrayList<>();
                list.add(newRx);
                list.add(newRx);
                list.add(newRx);

                // Create a new IV group
                IV_GROUP_KEY = mCaseDatabase.child("iv_groups").push().getKey();
                IvGroup newIv = new IvGroup("New IV", IV_GROUP_KEY, false);
                newIv.setRxAttached(list);
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

        mAdapter = new IvGroupRecyclerAdapter(getContext(), options, this);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
        mIvGroupLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mIvGroupLayoutManager);
    }

    @Override
    public void onIvGroupSelected(View view, int position) {
        if (mIsFabFocused) {
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                    .getColor(getContext(), R.color.colorCaseFragFabDeselected)));
            mIsFabFocused = false;
        } else {
            mFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                    .getColor(getContext(), R.color.colorAccent)));
            mIsFabFocused = true;
        }
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
