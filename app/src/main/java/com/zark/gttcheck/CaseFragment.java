package com.zark.gttcheck;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFragment extends Fragment {


    public CaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_case, container, false);

        CardView headerCard = view.findViewById(R.id.card_view_case_details);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String transitionName = bundle.getString("transitionName");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                headerCard.setTransitionName(transitionName);
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

}
