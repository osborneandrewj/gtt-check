package com.zark.gttcheck.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.zark.gttcheck.R;
import com.zark.gttcheck.models.Rx;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class RxListAdapter extends FirebaseListAdapter<Rx> {

    /**
     * ViewHolder for each Rx
     */
    static class RxListAdapterViewHolder {

        @BindView(R.id.rx_name) TextView rxName;

        public RxListAdapterViewHolder(View itemView) {

            ButterKnife.bind(this, itemView);
        }
    }

    public RxListAdapter(@NonNull FirebaseListOptions<Rx> options) {
        super(options);
    }

    // For implementation see:
    // https://stackoverflow.com/questions/40891268/how-to-get-firebase-data-into-a-listview
    @Override
    protected void populateView(View v, Rx model, int position) {
        RxListAdapterViewHolder holder = new RxListAdapterViewHolder(v);
        holder.rxName.setText(model.getName());
    }
}