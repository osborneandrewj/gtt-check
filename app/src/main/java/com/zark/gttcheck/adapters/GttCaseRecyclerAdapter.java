package com.zark.gttcheck.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Osborne on 1/24/2018.
 *
 */

public class GttCaseRecyclerAdapter
        extends FirestoreRecyclerAdapter<GttCase, GttCaseRecyclerAdapter.GttCaseViewHolder>{

    // Container activity must contain this interface
    public interface OnCaseSelectedListener {
        void onCaseClicked(View view, int position, String ref);
    }

    private Context mContext;
    private OnCaseSelectedListener mListener;

    /**
     * ViewHolder for each case
     */
    static class GttCaseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_case) CardView cardView;
        @BindView(R.id.tv_case_id) TextView idNumber;
        @BindView(R.id.tv_case_count_iv) TextView ivCount;
        @BindView(R.id.tv_case_count_rx) TextView rxCount;

        public GttCaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public GttCaseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<GttCase> options,
                                  Context context, OnCaseSelectedListener listener) {
        super(options);
        this.mContext = context.getApplicationContext();
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final GttCaseViewHolder holder,
                                    int position, @NonNull final GttCase model) {
        holder.idNumber.setText(String.valueOf(model.getIdNumber()));
        holder.ivCount.setText(String.valueOf(model.getIvCount()));
        holder.rxCount.setText(String.valueOf(model.getRxCount()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCaseClicked(view, holder.getAdapterPosition(), model.getReference());
                }
            }
        });

        // Set an alternating CardView background color
        if (holder.getAdapterPosition() % 2 == 0) {
            holder.cardView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.colorCasesRVLightSteelBlue));
        } else {
            holder.cardView.setBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.colorCasesRVLavender));
        }

        // Set a unique transition name for each item
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.cardView.setTransitionName("transition" + position);
        }
    }

    @Override
    public GttCaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_overview_item_layout, parent, false);

        return new GttCaseViewHolder(view);
    }
}
