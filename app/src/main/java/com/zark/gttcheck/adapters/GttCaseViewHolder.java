package com.zark.gttcheck.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;

import butterknife.ButterKnife;

/**
 * Created by Osborne on 11/28/2017.
 *
 */

public class GttCaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    //@BindView(R.id.cv_case) ConstraintLayout mCaseLayout;
    //@BindView(R.id.tv_case_id) TextView mCaseIdNumber;
    //@BindView(R.id.tv_case_count_iv) TextView mIvCount;
    //@BindView(R.id.tv_case_count_rx) TextView mRxCount;
    public CardView cardView;
    public TextView idNumber;
    public TextView ivCount;
    public TextView rxCount;

    //private final View.OnClickListener mListener;
    private RecyclerViewClickListener mClickListener;

    public GttCaseViewHolder(final View view, RecyclerViewClickListener listener) {
        super(view);

        //ButterKnife.bind(this, view);
        cardView = view.findViewById(R.id.card_view_case);
        idNumber = view.findViewById(R.id.tv_case_id);
        ivCount = view.findViewById(R.id.tv_case_count_iv);
        rxCount = view.findViewById(R.id.tv_case_count_rx);
        mClickListener = listener;
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
        mClickListener.onClick(v, getAdapterPosition());
    }

}
