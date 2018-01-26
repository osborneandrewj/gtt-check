package com.zark.gttcheck.models;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by osborne on 11/13/2017.
 *
 */

public class GttCase {

    private int idNumber;
    private int ivCount;
    private int rxCount;
    private String reference;
    private Map<String, Boolean> iv;
    private Map<String, Boolean> rx;

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

    public Map<String, Boolean> getIv() {
        return iv;
    }

    public void setIv(Map<String, Boolean> iv) {
        this.iv = iv;
    }

    public Map<String, Boolean> getRx() {
        return rx;
    }

    public void setRx(Map<String, Boolean> rx) {
        this.rx = rx;
    }
}
