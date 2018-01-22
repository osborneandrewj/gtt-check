package com.zark.gttcheck.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zark.gttcheck.R;
import com.zark.gttcheck.models.Rx;

import java.util.List;

/**
 * Created by osbor on 1/22/2018.
 *
 */

public class RxAdapter extends ArrayAdapter<Rx> {

    public RxAdapter(@NonNull Context context, int resource, @NonNull List<Rx> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rx_layout, parent, false);
        }

        Rx currentRx = getItem(position);
        if (currentRx != null) {
            TextView textView = (TextView) convertView.findViewById(R.id.rx_name);
            textView.setText(currentRx.getName());
        }
        return convertView;
    }
}
