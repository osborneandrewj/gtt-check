package com.zark.gttcheck;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zark.gttcheck.utilities.MyStringKeys;

import timber.log.Timber;

public class AddCaseActivity extends AppCompatActivity
        implements EditNameFrag.OnFragmentInteractionListener,
        AddCaseFrag.OnFragmentInteractionListener {

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

    /**
     * From EditNameFrag
     *
     * This method occurs when user has edited or created the name of a case
     */
    @Override
    public void onNameEdited(String newName) {
        Bundle bundle = new Bundle();
        bundle.putString(MyStringKeys.CASE_NAME, newName);

        AddCaseFrag addCaseFrag = new AddCaseFrag();
        addCaseFrag.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.add_case_container, addCaseFrag).commit();
    }

    /**
     * User has selected "Done" from the AddCaseActivity
     */
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
