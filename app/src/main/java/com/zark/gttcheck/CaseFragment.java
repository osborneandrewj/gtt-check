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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.zark.gttcheck.adapters.IvGroupRecyclerAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.Rx;
import com.zark.gttcheck.utilities.MyDatabaseUtils;

import java.util.HashMap;
import java.util.Map;

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
    private String mCaseId;

    @BindView(R.id.header_case_details) ConstraintLayout mCaseHeader;
    @BindView(R.id.rv_iv_groups) RecyclerView mRecyclerView;
    @BindView(R.id.tv_details_count_rx) TextView tv_RxCount;
    @BindView(R.id.tv_details_count_iv) TextView tv_IvCount;
    @BindView(R.id.tv_details_id) TextView tv_CaseId;
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
            mCaseId = bundle.getString(CASE_REF);
        }


        // Add data to header
        DocumentReference reference = MyDatabaseUtils.getUserDbReference(mUserId)
                .collection(MyDatabaseUtils.CASE_DIRECTORY)
                .document(mCaseId);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Timber.e("Setting case header failed: %s", e.toString());
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    GttCase currentCase = documentSnapshot.toObject(GttCase.class);
                    tv_RxCount.setText(String.valueOf(currentCase.getRxCount()));
                    tv_IvCount.setText(String.valueOf(currentCase.getIvCount()));
                    tv_CaseId.setText(String.valueOf(currentCase.getIdNumber()));
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {

            //TODO: Fix this nonsense
            @Override
            public void onClick(View view) {

                // Create a new IV
                IvGroup newIv = new IvGroup("New IV", null, false);

                // Add this empty IV to the database
                DocumentReference ref = MyDatabaseUtils.getNewIvDbReference(mUserId, mCaseId);
                newIv.setReference(ref.getId());
                MyDatabaseUtils.getIvDbColReference(
                        mUserId, mCaseId).document(newIv.getReference()).set(newIv);
            }
        });

        startRecyclerView();

        // Inflate the layout for this fragment
        return view;
    }

    private void startRecyclerView() {

        mCaseDatabase = FirebaseFirestore.getInstance();
        com.google.firebase.firestore.Query query =
                MyDatabaseUtils.getIvDbColReference(mUserId, mCaseId);

        FirestoreRecyclerOptions<IvGroup> options = new FirestoreRecyclerOptions.Builder<IvGroup>()
                .setQuery(query, IvGroup.class)
                .build();

        mAdapter = new IvGroupRecyclerAdapter(getContext(), options, this, mUserId, mCaseId);
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
    public void onIvGroupMenuSelected(final View view, final int position, final String ivRef) {
        // Get the IV group
        DocumentReference reference = MyDatabaseUtils.getIvDbColReference(mUserId, mCaseId)
                .document(ivRef);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Timber.e("Setting case header failed: %s", e.toString());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    IvGroup currentIv = documentSnapshot.toObject(IvGroup.class);
                    Timber.e("Current IV: %s", currentIv.getReference());
                    onExtendedMenuClicked(view, position, currentIv);
                }
            }
        });

        //DatabaseReference ivReference = mCaseDatabase.child("iv_groups").child(ivRef).child("rxAttached");

    }

    public void onExtendedMenuClicked(View view, int position, IvGroup iv) {
        int id = view.getId();
        switch (id) {
            case R.id.ex_menu_add_rx:
                Timber.e("Add Rx");

                // Create a new Rx
                Rx newRx = new Rx("New Medication", false);

                // Set Rx data
                newRx.setReference(MyDatabaseUtils.getNewRxDbReference(
                                mUserId, mCaseId, iv.getReference()).getId());
                Map<String, Boolean> newRxIv = new HashMap<>();
                newRxIv.put(iv.getReference(), true);
                newRx.setIv(newRxIv);

                // Add this Rx to the database
                MyDatabaseUtils.getRxColReference(mUserId, mCaseId, iv.getReference())
                        .document(newRx.getReference()).set(newRx);
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
