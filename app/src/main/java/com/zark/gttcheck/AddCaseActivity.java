package com.zark.gttcheck;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AddCaseActivity extends AppCompatActivity
        implements EditNameFrag.OnFragmentInteractionListener,
        AddCaseFrag.OnAddCaseFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_case);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AddCaseFrag newAddCaseFrag = new AddCaseFrag();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.add_case_container, newAddCaseFrag)
                .addToBackStack(newAddCaseFrag.getClass().getName()).commit();
    }

    @Override
    public void onNameEdited(String newName) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate();
    }

    @Override
    public void onDoneButtonPressed() {
        //TODO: enter new case into database
        finish();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Timber.e("Got something in the backstack");
            fm.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
