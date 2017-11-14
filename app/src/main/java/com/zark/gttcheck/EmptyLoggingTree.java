package com.zark.gttcheck;

import timber.log.Timber;

/**
 * Created by osborne on 11/13/2017.
 *
 */

public class EmptyLoggingTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // Purposefully left empty
    }
}
