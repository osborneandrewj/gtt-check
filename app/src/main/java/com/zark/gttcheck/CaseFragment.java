package com.zark.gttcheck;


import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zark.gttcheck.adapters.IvGroupListAdapter;
import com.zark.gttcheck.models.IvGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFragment extends Fragment {

    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String USER_NAME_KEY = "userNameKey";

    // IV Group RecyclerView
    private RecyclerView.LayoutManager mIvGroupLayoutManager;
    private IvGroupListAdapter mAdapter;
    private DatabaseReference mIvGroupDatabase;

    private String mUserId;

    @BindView(R.id.header_case_details) ConstraintLayout mCaseHeader;
    @BindView(R.id.rv_iv_groups) RecyclerView mRecyclerView;

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
        }

        // Make sure that Firebase has a value for the list of IV groups
        mIvGroupDatabase = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mUserId)
                .child("cases")
                .child("iv_group"); // Change this value
        mIvGroupDatabase.setValue("Hey");
        // Inflate the layout for this fragment
        return view;
    }

    private void startRecyclerView() {

        Query query = mIvGroupDatabase.limitToLast(50);

        FirebaseRecyclerOptions<IvGroup> options =
                new FirebaseRecyclerOptions.Builder<IvGroup>()
                .setQuery(query, IvGroup.class)
                .build();

        mAdapter = new IvGroupListAdapter(options);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
        mIvGroupLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mIvGroupLayoutManager);
    }

}
