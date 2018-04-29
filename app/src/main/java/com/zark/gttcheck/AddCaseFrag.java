package com.zark.gttcheck;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zark.gttcheck.models.GttCase;
import com.zark.gttcheck.utilities.MyStringKeys;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCaseFrag extends Fragment {

    private static final String USER_NAME_KEY = "userNameKey";
    private GttCase GttCase;
    private OnFragmentInteractionListener mListener;
    private String newCaseName;

    @BindView(R.id.tv_details_id_edit)
    TextView caseName;
    @BindView(R.id.tv_label_sel_name)
    TextView btnSelectName;
    @BindView(R.id.tv_details_count_iv_edit)
    TextView caseIvCount;
    @BindView(R.id.tv_details_count_rx_edit)
    TextView caseRxCount;
    @BindView(R.id.btn_done)
    TextView btnDone;

    public AddCaseFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_case, container, false);
        ButterKnife.bind(this, view);

        // Create a new case
        GttCase = new GttCase();
        GttCase.setIvCount(0);
        GttCase.setRxCount(0);
        GttCase.setName("0000");


        if (getArguments() != null) {
            if (getArguments().getString(MyStringKeys.CASE_NAME) != null) {
                newCaseName = getArguments().getString(MyStringKeys.CASE_NAME);
                GttCase.setName(newCaseName);
            }
        }

        // Setup header
        caseName.setText(GttCase.getName());
        if (!TextUtils.equals(GttCase.getName(), "0000")) {
            // There is already a name, now check this step as done
            // TODO: check off "select name"
            Timber.e("Name already edited");
        }

        // Setup buttons
        btnSelectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                if (fm != null) {
                    fm.beginTransaction().replace(R.id.add_case_container, new EditNameFrag())
                            .addToBackStack("heya").commit();
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDoneButtonPressed();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener, doofus");
        }
    }

    private void updateCaseName(String newName) {
        caseName.setText(String.valueOf(newName));
    }

    public interface OnFragmentInteractionListener {
        void onDoneButtonPressed();
    }


}
