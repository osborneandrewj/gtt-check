package com.zark.gttcheck.models;

import java.util.ArrayList;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroup {

    private String name;
    private String reference;
    private Boolean central;
    private ArrayList<Rx> rxAttached;

    public IvGroup() {

    }

    public IvGroup(String name, String reference) {
        this.name = name;
        this.reference = reference;
    }

    public IvGroup(String name, String reference, Boolean central) {
        this.name = name;
        this.reference = reference;
        this.central = central;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getCentral() {
        return central;
    }

    public void setCentral(Boolean central) {
        this.central = central;
    }

    public ArrayList<Rx> getRxAttached() {
        return rxAttached;
    }

    public void setRxAttached(ArrayList<Rx> rxAttached) {
        this.rxAttached = rxAttached;
    }
}
