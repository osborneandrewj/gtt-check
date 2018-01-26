package com.zark.gttcheck.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.Rx;
import com.zark.gttcheck.utilities.MyDatabaseUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRecyclerAdapter extends FirestoreRecyclerAdapter<IvGroup,
        IvGroupRecyclerAdapter.IvGroupListAdapterViewHolder> {

    // Container activity must contain this interface
    public interface OnIvGroupSelectedListener {
        void onIvGroupSelected(View view, int position);
        void onIvGroupMenuSelected(View view, int position, String ivRef);
    }

    private Context mContext;
    private OnIvGroupSelectedListener mListener;
    private String mUserId;
    private String mCaseId;

    /**
     * ViewHolder for each IV item
     */
    static class IvGroupListAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_iv_group) CardView cardView;
        @BindView(R.id.rx_list) LinearLayout rxList;
        @BindView(R.id.rv_expandable) ExpandableLayout expandMenu;
        @BindView(R.id.ex_menu_add_rx) TextView menuAddRx;
        @BindView(R.id.ex_menu_delete_rx) TextView menuDeleteRx;
        @BindView(R.id.ex_menu_delete_iv) TextView menuDeleteIv;

        public IvGroupListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public IvGroupRecyclerAdapter(Context context, @NonNull FirestoreRecyclerOptions<IvGroup> options,
                                  OnIvGroupSelectedListener listener, String userId, String caseId) {
        super(options);
        mContext = context;
        mListener = listener;
        mUserId = userId;
        mCaseId = caseId;
    }

    @Override
    protected void onBindViewHolder(@NonNull final IvGroupListAdapterViewHolder holder,
                                    final int position, @NonNull final IvGroup model) {

        // Set a list of medications associated with this particular IV group
        CollectionReference rxCollection =
                MyDatabaseUtils.getIvDbColReference(mUserId, mCaseId).document(model.getReference())
                        .collection(MyDatabaseUtils.RX_DIRECTORY);
                rxCollection.whereEqualTo("iv." + model.getReference(), true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Timber.e("Size: %s", task.getResult().size());
                            List<Rx> rxArrayList = task.getResult().toObjects(Rx.class);
                            for (Rx currentRx : rxArrayList) {
                                View view = LayoutInflater.from(mContext).inflate(R.layout.rx_layout, null);
                                TextView rxNameTextView = view.findViewById(R.id.rx_name);
                                rxNameTextView.setText(currentRx.getName());
                                holder.rxList.addView(view);
                            }
                        }
                    });


//        final ArrayList<Rx> rxArrayList = new ArrayList<>(model.getRxAttached());
//        for (Rx currentRx : rxArrayList) {
//            View view  = LayoutInflater.from(mContext).inflate(R.layout.rx_layout, null);
//            TextView rxNameTextView = view.findViewById(R.id.rx_name);
//            rxNameTextView.setText(currentRx.getName());
//            holder.rxList.addView(view);
//        }

        // Expand when clicked
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandMenu.toggle();
                //holder.cardView.setCardElevation(8);
                mListener.onIvGroupSelected(view, holder.getAdapterPosition());
            }
        });

        holder.menuAddRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onIvGroupMenuSelected(view, position, model.getReference());
            }
        });

        holder.menuDeleteRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onIvGroupMenuSelected(view, position, model.getReference());
            }
        });

        holder.menuDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onIvGroupMenuSelected(view, position, model.getReference());
            }
        });
    }

    @Override
    public IvGroupListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iv_group_layout, parent, false);
        return new IvGroupListAdapterViewHolder(view);
    }
}
