package com.zark.gttcheck;

import android.support.transition.ChangeBounds;
import android.support.transition.ChangeImageTransform;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionSet;

/**
 * Created by Osborne on 1/15/18.
 *
 */

public class CaseDetailsTransition extends TransitionSet {
    public CaseDetailsTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform());
    }
}
