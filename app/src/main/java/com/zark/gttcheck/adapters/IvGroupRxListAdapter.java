package com.zark.gttcheck.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.zark.gttcheck.models.IvGroup;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRxListAdapter extends FirebaseListAdapter<IvGroup> {

    public IvGroupRxListAdapter(@NonNull FirebaseListOptions<IvGroup> options) {
        super(options);
    }

    // For implementation see:
    // https://stackoverflow.com/questions/40891268/how-to-get-firebase-data-into-a-listview
    @Override
    protected void populateView(View v, IvGroup model, int position) {

    }
}