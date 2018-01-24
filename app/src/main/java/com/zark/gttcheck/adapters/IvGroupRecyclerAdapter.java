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
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.IvGroup;
import com.zark.gttcheck.models.Rx;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRecyclerAdapter extends FirebaseRecyclerAdapter<IvGroup,
        IvGroupRecyclerAdapter.IvGroupListAdapterViewHolder> {

    // Container activity must contain this interface
    public interface OnIvGroupSelectedListener {
        void onIvGroupSelected(View view, int position);
    }

    private Context mContext;
    private OnIvGroupSelectedListener mListener;

    /**
     * ViewHolder for each IV item
     */
    static class IvGroupListAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_iv_group) CardView cardView;
        @BindView(R.id.rx_list) LinearLayout rxList;
        @BindView(R.id.rv_expandable) ExpandableLayout expandMenu;

        public IvGroupListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public IvGroupRecyclerAdapter(Context context, @NonNull FirebaseRecyclerOptions<IvGroup> options,
                                  OnIvGroupSelectedListener listener) {
        super(options);
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull final IvGroupListAdapterViewHolder holder, final int position, @NonNull IvGroup model) {

        // Set a list of medications associated with this particular IV group
        ArrayList<Rx> rxArrayList = new ArrayList<>(model.getRxAttached());
        for (Rx currentRx : rxArrayList) {
            View view  = LayoutInflater.from(mContext).inflate(R.layout.rx_layout, null);
            TextView rxNameTextView = view.findViewById(R.id.rx_name);
            rxNameTextView.setText(currentRx.getName());
            holder.rxList.addView(view);
        }

        // Expand when clicked
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandMenu.toggle();
                //holder.cardView.setCardElevation(8);
                mListener.onIvGroupSelected(view, holder.getAdapterPosition());
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
