package com.zark.gttcheck.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;

/**
 * Created by Osborne on 1/10/18.
 *
 */

public class GttCaseListAdapterNew extends FirebaseRecyclerAdapter<
        GttCase, GttCaseListAdapterNew.GttCaseViewHolder>{

    // Container activity must contain this interface
    public interface OnCaseSelectedListener {
        void onCaseClicked(View view, int position);
    }

    /**
     * ViewHolder for each case
     */
    static class GttCaseViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView idNumber;
        public TextView ivCount;
        public TextView rxCount;

        public GttCaseViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_case);
            idNumber = itemView.findViewById(R.id.tv_case_id);
            ivCount = itemView.findViewById(R.id.tv_case_count_iv);
            rxCount = itemView.findViewById(R.id.tv_case_count_rx);
        }
    }

    private Context mContext;

    public GttCaseListAdapterNew(@NonNull FirebaseRecyclerOptions<GttCase> options,
                                 Context context) {
        super(options);
        this.mContext = context.getApplicationContext();
    }

    @Override
    protected void onBindViewHolder(@NonNull GttCaseViewHolder holder, int position, @NonNull GttCase model) {

    }

    @Override
    public GttCaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.case_overview_item_layout, parent, false);

        GttCaseViewHolder gttCaseViewHolder =
                new GttCaseViewHolder(view);

        return gttCaseViewHolder;
    }
}
