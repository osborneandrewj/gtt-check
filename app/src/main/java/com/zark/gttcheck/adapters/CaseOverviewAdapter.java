package com.zark.gttcheck.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.GttCase;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Osborne on 11/14/2017.
 *
 */

public class CaseOverviewAdapter extends RecyclerView.Adapter<CaseOverviewAdapter.ViewHolder> {

    private ArrayList<GttCase> mDataset;
    private GttCase mCase;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private final CaseOnClickHandler mClickHandler;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_case) ConstraintLayout mCaseLayout;
        @BindView(R.id.tv_case_id) TextView mCaseIdNumber;
        @BindView(R.id.tv_case_count_iv) TextView mIvCount;
        @BindView(R.id.tv_case_count_rx) TextView mRxCount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CaseOverviewAdapter(Context context, ArrayList<GttCase> data,
                               CaseOnClickHandler handler) {
        mLayoutInflater = LayoutInflater.from(context);
        mDataset = data;
        mClickHandler = handler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = mLayoutInflater.inflate(R.layout.case_overview_item_layout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mCaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onCaseClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        }
        return 0;
    }

    public void setNewDataSet(ArrayList<GttCase> newDataSet) {
        mDataset = newDataSet;
        notifyDataSetChanged();
    }

    public interface CaseOnClickHandler {
        void onCaseClick();
    }
}
