package com.zark.gttcheck.utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zark.gttcheck.models.IvGroup;

/**
 * Created by Osborne on 1/24/2018.
 *
 */

public class MyDatabaseUtils {

    private static final String USERS_DIRECTORY = "users";
    public static final String CASE_DIRECTORY = "cases";
    public static final String RX_DIRECTORY = "rx";
    public static final String IV_DIRECTORY = "iv";

    public static DocumentReference getUserDbReference(String userId) {
        return FirebaseFirestore.getInstance().collection(USERS_DIRECTORY)
                .document(userId);
    }

    public static DocumentReference getNewRxDbReference(String userId) {
        return getUserDbReference(userId).collection(RX_DIRECTORY).document();
    }

    public static DocumentReference getNewCaseDbReference(String userId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document();
    }

    public static DocumentReference getNewIvDbReference(String userId) {
        return getUserDbReference(userId).collection(IV_DIRECTORY).document();
    }

    public static void addRxToIvGroup(String userId, String ivGroupRef, String newRxRef) {

    }
}
