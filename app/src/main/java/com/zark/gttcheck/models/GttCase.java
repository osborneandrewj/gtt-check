package com.zark.gttcheck.models;

import android.support.annotation.Nullable;

/**
 * Created by osborne on 11/13/2017.
 *
 */

public class GttCase {

    private int idNumber;
    private int ivCount;
    private int rxCount;
    private String reference;

    public GttCase() {

    }

    public GttCase(int idNumber, int ivCount, int rxCount, @Nullable String aReference) {
        this.idNumber = idNumber;
        this.ivCount = ivCount;
        this.rxCount = rxCount;
        this.reference = aReference;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public int getIvCount() {
        return ivCount;
    }

    public void setIvCount(int ivCount) {
        this.ivCount = ivCount;
    }

    public int getRxCount() {
        return rxCount;
    }

    public void setRxCount(int rxCount) {
        this.rxCount = rxCount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
