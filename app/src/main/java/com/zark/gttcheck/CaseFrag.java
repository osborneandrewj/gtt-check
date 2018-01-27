package com.zark.gttcheck;


import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.zark.gttcheck.adapters.IvRecyclerAdapter;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.Iv;
import com.zark.gttcheck.models.Rx;
import com.zark.gttcheck.utilities.MyDbUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFrag extends Fragment implements IvRecyclerAdapter.OnIvGroupSelectedListener {

    private static final String TRANSITION_NAME_KEY = "transitionName";
    private static final String CASE_REF = "caseRef";
    private static final String RX_COUNT_KEY = "rxCount";
    private static String IV_GROUP_KEY = "ivGroupKey";
    private Boolean mIsFabFocused = true;

    // IV Group RecyclerView
    private RecyclerView.LayoutManager mIvGroupLayoutManager;
    private IvRecyclerAdapter mAdapter;
    private FirebaseFirestore mCaseDatabase;

    private String mUserId;
    private String mCaseId;

    @BindView(R.id.header_case_details) ConstraintLayout mCaseHeader;
    @BindView(R.id.rv_iv_groups) RecyclerView mRecyclerView;
    @BindView(R.id.tv_details_count_rx) TextView tv_RxCount;
    @BindView(R.id.tv_details_count_iv) TextView tv_IvCount;
    @BindView(R.id.tv_details_id) TextView tv_CaseId;
    @BindView(R.id.fab_case) FloatingActionButton mFab;

    public CaseFrag() {
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
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            mUserId = prefs.getString(MyDbUtils.USER_ID_KEY, null);
            Timber.e("User id: %s", mUserId);
            mCaseId = bundle.getString(CASE_REF);
        }

        // Initialize header
        //TODO: Dynamically update header information
        DocumentReference reference = MyDbUtils.getUserDbRef(mUserId)
                .collection(MyDbUtils.CASE_DIR)
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
                Iv newIv = new Iv("New IV", null, false);

                // Add this empty IV to the database
                DocumentReference ref = MyDbUtils.getNewIvDbRef(mUserId, mCaseId);
                newIv.setRef(ref.getId());
                MyDbUtils.getIvDbColRef(
                        mUserId, mCaseId).document(newIv.getRef()).set(newIv);
            }
        });

        startRecyclerView();
        return view;
    }

    private void startRecyclerView() {

        mCaseDatabase = FirebaseFirestore.getInstance();
        com.google.firebase.firestore.Query query =
                MyDbUtils.getIvDbColRef(mUserId, mCaseId);

        FirestoreRecyclerOptions<Iv> options = new FirestoreRecyclerOptions.Builder<Iv>()
                .setQuery(query, Iv.class)
                .build();

        mAdapter = new IvRecyclerAdapter(getContext(), options, this, mUserId, mCaseId);
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
        DocumentReference reference = MyDbUtils.getIvDbColRef(mUserId, mCaseId)
                .document(ivRef);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Timber.e("Setting case header failed: %s", e.toString());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Iv currentIv = documentSnapshot.toObject(Iv.class);
                    Timber.e("Current IV: %s", currentIv.getRef());
                    onExtendedMenuClicked(view, position, currentIv);
                }
            }
        });
    }

    public void onExtendedMenuClicked(View view, int position, Iv iv) {
        int id = view.getId();
        switch (id) {
            case R.id.ex_menu_add_rx:

                //TODO: use AddRxFragment here

                // Create a new Rx
                Rx newRx = new Rx("New Medication", false);

                // Set Rx reference and IV information
                newRx.setRef(MyDbUtils.getNewRxDbRef(mUserId, mCaseId, iv.getRef()).getId());
                Map<String, Boolean> newRxIv = new HashMap<>();
                newRxIv.put(iv.getRef(), true);
                newRx.setIv(newRxIv);

                // Add this Rx to the database
                MyDbUtils.getRxColRef(mUserId, mCaseId, iv.getRef())
                        .document(newRx.getRef()).set(newRx);
                mAdapter.notifyItemChanged(position);
                break;
            case R.id.ex_menu_delete_rx:
                Timber.e("Delete Rx");

                //TODO: use Rx-specific delete button here
                mAdapter.notifyItemChanged(position);
                break;
            case R.id.ex_menu_delete_iv:

                //TODO: implement delete functionality
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
