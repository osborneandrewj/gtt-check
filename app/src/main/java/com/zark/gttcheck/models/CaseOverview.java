package com.zark.gttcheck.models;

/**
 * Created by osborne on 11/13/2017.
 *
 */

public class CaseOverview {

    private int idNumber;
    private int ivCount;
    private int rxCount;

    public CaseOverview() {

    }

    public CaseOverview(int idNumber, int ivCount, int rxCount) {
        this.idNumber = idNumber;
        this.ivCount = ivCount;
        this.rxCount = rxCount;
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
}
