package com.zark.gttcheck.adapters;

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

public class GttCaseViewHolder extends RecyclerView.ViewHolder {

    //@BindView(R.id.cv_case) ConstraintLayout mCaseLayout;
    //@BindView(R.id.tv_case_id) TextView mCaseIdNumber;
    //@BindView(R.id.tv_case_count_iv) TextView mIvCount;
    //@BindView(R.id.tv_case_count_rx) TextView mRxCount;
    public TextView rxCount;

    public GttCaseViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        rxCount = view.findViewById(R.id.tv_case_count_rx);
    }

    public void bind(GttCase gttCase) {
        setId(String.valueOf(gttCase.getIdNumber()));
    }

    private void setId(String id) {
        rxCount.setText(id);
    }

}
