package com.zark.gttcheck.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.IvGroupRx;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupListAdapter extends FirebaseRecyclerAdapter<IvGroup,
        IvGroupListAdapter.IvGroupListAdapterViewHolder> {

    // Container activity must contain this interface
    public interface OnIvGroupSelectedListener {
        void onIvGroupSelected(View view, int position);
    }

    private OnIvGroupSelectedListener mListener;

    /**
     * ViewHolder for each IV item
     */
    static class IvGroupListAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lv_iv_group) ListView ivGroup;

        public IvGroupListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public IvGroupListAdapter(@NonNull FirebaseRecyclerOptions<IvGroup> options,
                              OnIvGroupSelectedListener listener) {
        super(options);
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final IvGroupListAdapterViewHolder holder, final int position, @NonNull IvGroup model) {

        // Nothing to bind yet

        holder.ivGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
