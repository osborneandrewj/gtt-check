package com.zark.gttcheck.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;

/**
 * Created by Osborne on 11/28/2017.
 *
 */

public class GttCaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    // Container activity must contain this interface
    public interface OnCaseSelectedListener {
        void onCaseClicked(View view, int position);
    }

    public CardView cardView;
    public TextView idNumber;
    public TextView ivCount;
    public TextView rxCount;

    private OnCaseSelectedListener mOnCaseSelectedListener;

    public GttCaseViewHolder(final View view, OnCaseSelectedListener listener) {
        super(view);

        //ButterKnife.bind(this, view);
        cardView = view.findViewById(R.id.card_view_case);
        idNumber = view.findViewById(R.id.tv_case_id);
        ivCount = view.findViewById(R.id.tv_case_count_iv);
        rxCount = view.findViewById(R.id.tv_case_count_rx);
        mOnCaseSelectedListener = listener;
        view.setOnClickListener(this);
    }

    public void bind(GttCase gttCase) {
        setId(String.valueOf(gttCase.getIdNumber()));
        setIvCount(String.valueOf(gttCase.getIvCount()));
        setRxCount(String.valueOf(gttCase.getRxCount()));
    }

    private void setId(String id) {
        idNumber.setText(id);
    }

    private void setIvCount(String iv) {
        ivCount.setText(iv);
    }

    private void setRxCount(String rx) {
        rxCount.setText(rx);
    }

    @Override
    public void onClick(View v) {
        mOnCaseSelectedListener.onCaseClicked(v, getAdapterPosition());
    }

}
