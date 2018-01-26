package com.zark.gttcheck.utilities;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zark.gttcheck.models.Rx;

import timber.log.Timber;

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

    public static DocumentReference getNewRxDbReference(String userId, String caseId, String ivId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document(caseId)
                .collection(IV_DIRECTORY).document(ivId)
                .collection(RX_DIRECTORY).document();
    }

    public static CollectionReference getRxColReference(String userId, String caseId, String ivId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document(caseId)
                .collection(IV_DIRECTORY).document(ivId).collection(RX_DIRECTORY);
    }

    public static DocumentReference getNewCaseDbReference(String userId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document();
    }

    public static DocumentReference getNewIvDbReference(String userId, String caseId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document(caseId)
                .collection(IV_DIRECTORY).document();
    }

    public static CollectionReference getIvDbColReference(String userId, String caseId) {
        return getUserDbReference(userId).collection(CASE_DIRECTORY).document(caseId)
                .collection(IV_DIRECTORY);
    }

    public static void addRxToIvGroup(String userId, String caseId,
                                      String ivGroupRef, String newRxRef, Rx newRx) {
        Timber.e("Got an Rx in here... %s", newRx.getName());
        Timber.e("Case: %s", caseId);
        Timber.e("IV: %s", ivGroupRef);
        Timber.e("Rx: %s", newRxRef);


        getUserDbReference(userId)
                .collection(CASE_DIRECTORY)
                .document(caseId)
                .collection(IV_DIRECTORY)
                .document(ivGroupRef)
                .collection(RX_DIRECTORY)
                .document(newRxRef)
                .set(newRx);
    }
}
