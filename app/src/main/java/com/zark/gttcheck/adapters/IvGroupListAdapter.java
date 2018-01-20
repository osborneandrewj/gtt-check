package com.zark.gttcheck.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.IvGroup;

import butterknife.ButterKnife;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupListAdapter extends FirebaseRecyclerAdapter<IvGroup,
        IvGroupListAdapter.IvGroupListAdapterViewHolder> {

    /**
     * ViewHolder for each IV item
     */
    static class IvGroupListAdapterViewHolder extends RecyclerView.ViewHolder {



        public IvGroupListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public IvGroupListAdapter(@NonNull FirebaseRecyclerOptions<IvGroup> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull IvGroupListAdapterViewHolder holder, int position, @NonNull IvGroup model) {

        // Nothing to bind yet

    }

    @Override
    public IvGroupListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iv_group_layout, parent, false);
        return new IvGroupListAdapterViewHolder(view);
    }
}
