package com.zark.gttcheck.utilities;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Osborne on 1/24/2018.
 *
 */

public class MyDbUtils {

    public static final String USER_ID_KEY = "user_id_key";
    private static final String USERS_DIR = "users";
    public static final String CASE_DIR = "cases";
    public static final String RX_DIR = "rx";
    public static final String IV_DIR = "iv";

    public static DocumentReference getUserDbRef(String userId) {
        return FirebaseFirestore.getInstance().collection(USERS_DIR)
                .document(userId);
    }

    public static DocumentReference getNewRxDbRef(String userId, String caseId, String ivId) {
        return getUserDbRef(userId).collection(CASE_DIR).document(caseId)
                .collection(IV_DIR).document(ivId)
                .collection(RX_DIR).document();
    }

    public static CollectionReference getRxColRef(String userId, String caseId, String ivId) {
        return getUserDbRef(userId).collection(CASE_DIR).document(caseId)
                .collection(IV_DIR).document(ivId).collection(RX_DIR);
    }

    public static DocumentReference getNewCaseDbRef(String userId) {
        return getUserDbRef(userId).collection(CASE_DIR).document();
    }

    public static DocumentReference getNewIvDbRef(String userId, String caseId) {
        return getUserDbRef(userId).collection(CASE_DIR).document(caseId)
                .collection(IV_DIR).document();
    }

    public static CollectionReference getIvDbColRef(String userId, String caseId) {
        return getUserDbRef(userId).collection(CASE_DIR).document(caseId)
                .collection(IV_DIR);
    }
}
