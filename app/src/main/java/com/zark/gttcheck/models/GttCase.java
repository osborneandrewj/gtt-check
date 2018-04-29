package com.zark.gttcheck.models;

import android.support.annotation.Nullable;

import java.util.Map;

/**
 * Created by osborne on 11/13/2017.
 *
 */

public class GttCase {

    private String name;
    private int ivCount;
    private int rxCount;
    private String reference;
    private Map<String, Boolean> iv;
    private Map<String, Boolean> rx;

    public GttCase() {

    }

    public GttCase(String name, int ivCount, int rxCount, @Nullable String aReference) {
        this.name = name;
        this.ivCount = ivCount;
        this.rxCount = rxCount;
        this.reference = aReference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
