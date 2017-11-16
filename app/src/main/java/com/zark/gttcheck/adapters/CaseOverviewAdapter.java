package com.zark.gttcheck.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.CaseOverviewItem;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Osborne on 11/14/2017.
 *
 */

public class CaseOverviewAdapter extends RecyclerView.Adapter<CaseOverviewAdapter.ViewHolder> {

    private ArrayList<CaseOverviewItem> mDataset;
    private CaseOverviewItem mCase;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_case_id) TextView mCaseIdNumber;
        @BindView(R.id.tv_case_count_iv) TextView mIvCount;
        @BindView(R.id.tv_case_count_rx) TextView mRxCount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public CaseOverviewAdapter(Context context, ArrayList<CaseOverviewItem> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mDataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = mLayoutInflater.inflate(R.layout.case_overview_item_layout,
                parent, false);
        //ButterKnife.bind(this, view);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the current case
        if (mDataset.size() > 0) {
            mCase = mDataset.get(position);
            holder.mCaseIdNumber.setText(String.valueOf(mCase.getIdNumber()));
            holder.mIvCount.setText(String.valueOf(mCase.getIvCount()));
            holder.mRxCount.setText(String.valueOf(mCase.getRxCount()));
        }

    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        }
        return 0;
    }

    public void setNewDataSet(ArrayList<CaseOverviewItem> newDataSet) {
        mDataset = newDataSet;
        notifyDataSetChanged();
    }
}
