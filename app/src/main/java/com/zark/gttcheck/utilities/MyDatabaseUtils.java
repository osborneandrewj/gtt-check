package com.zark.gttcheck.utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zark.gttcheck.models.IvGroup;

/**
 * Created by Osborne on 1/24/2018.
 *
 */

public class MyDatabaseUtils {

    private static final String USERS_DIRECTORY = "users";
    private static final String CASE_DIRECTORY = "cases";
    private static final String RX_DIRECTORY = "rx";
    private static final String IV_DIRECTORY = "iv";

    public static DatabaseReference getRxDbReference(String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child(USERS_DIRECTORY)
                .child(userId)
                .child(RX_DIRECTORY);
    }

    public static DatabaseReference getCasesDbReference(String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child(USERS_DIRECTORY)
                .child(userId)
                .child(CASE_DIRECTORY);
    }

    public static DatabaseReference getIvDbReference(String userId) {
        return FirebaseDatabase.getInstance().getReference()
                .child(USERS_DIRECTORY)
                .child(userId)
                .child(IV_DIRECTORY);
    }

    public static void addRxToIvGroup(String userId, String ivGroupRef, String newRxRef) {

        // Add Rx to IV Group
        DatabaseReference ivDatabase = getIvDbReference(userId);
        ivDatabase.child(ivGroupRef).child(RX_DIRECTORY).setValue(newRxRef, newRxRef);

        // Add IV Group reference to Rx
        DatabaseReference rxDatabase = getRxDbReference(userId);
        rxDatabase.child(newRxRef).child(IV_DIRECTORY).child(ivGroupRef);
    }
}
