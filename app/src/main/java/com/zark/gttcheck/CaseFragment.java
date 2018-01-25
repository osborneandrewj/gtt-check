package com.zark.gttcheck;


import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zark.gttcheck.adapters.IvGroupRecyclerAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.Rx;
import com.zark.gttcheck.utilities.MyDatabaseUtils;

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
    private FirebaseFirestore mCaseDatabase;

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


        // Add data to header once
        // Note: This does not update dynamically
        mCaseDatabase = FirebaseFirestore.getInstance();
        DocumentReference reference = mCaseDatabase.collection("users")
                .document(mUserId)
                .collection("cases")
                .document(mCaseRef);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GttCase currentCase = documentSnapshot.toObject(GttCase.class);
                mRxCount.setText(String.valueOf(currentCase.getRxCount()));
                mIvCount.setText(String.valueOf(currentCase.getIvCount()));
                mCaseId.setText(String.valueOf(currentCase.getIdNumber()));
            }
        });


        mFab.setOnClickListener(new View.OnClickListener() {

            //TODO: Fix this nonsense
            @Override
            public void onClick(View view) {

                // Create a new IV and add it to the database
                IvGroup newIv = new IvGroup("New IV", null, false);
                DocumentReference newIvRef = mCaseDatabase.collection("users")
                        .document(mUserId)
                        .collection("cases")
                        .document(mCaseRef)
                        .collection("iv")
                        .document();
                newIv.setReference(newIvRef.getId());
                mCaseDatabase.collection("users")
                        .document(mUserId)
                        .collection("cases")
                        .document(mCaseRef)
                        .collection("iv")
                        .document(newIvRef.getId())
                        .set(newIv);
            }
        });

        startRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void startRecyclerView() {

        mCaseDatabase = FirebaseFirestore.getInstance();
        com.google.firebase.firestore.Query query = mCaseDatabase.collection("users")
                .document(mUserId)
                .collection("cases")
                .document(mCaseRef)
                .collection("iv");

        FirestoreRecyclerOptions<IvGroup> options = new FirestoreRecyclerOptions.Builder<IvGroup>()
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
    public void onIvGroupMenuSelected(View view, int position, final String ivRef) {
        // Get the IV group
        IvGroup ivGroup;
        //DatabaseReference ivReference = mCaseDatabase.child("iv_groups").child(ivRef).child("rxAttached");
        int id = view.getId();
        switch (id) {
            case R.id.ex_menu_add_rx:
                Timber.e("Add Rx");
                Rx newRx = new Rx("New Medication", false);
                DocumentReference newRxRef = mCaseDatabase.collection("users")
                        .document(mUserId)
                        .collection("cases")
                        .document(mCaseRef)
                        .collection("iv")
                        .document(ivRef)
                        .collection("rx")
                        .document();
                newRx.setReference(newRxRef.getId());
                mCaseDatabase.collection("users")
                        .document(mUserId)
                        .collection("cases")
                        .document(mCaseRef)
                        .collection("iv")
                        .document(ivRef)
                        .collection("rx")
                        .document(newRxRef.getId())
                        .set(newRx);
                mAdapter.notifyItemChanged(position);
                break;
            case R.id.ex_menu_delete_rx:
                Timber.e("Delete Rx");
                break;
            case R.id.ex_menu_delete_iv:
                Timber.e("Delete IV");
                break;
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
