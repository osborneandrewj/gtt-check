package com.zark.gttcheck.models;

import com.google.firebase.database.DatabaseReference;
import com.zark.gttcheck.utilities.MyDatabaseUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroup {

    private String name;
    private String reference;
    private Boolean central;
    private Map<String, Boolean> rx;

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

    public Map<String, Boolean> getRx() {
        return rx;
    }

    public void setRx(Map<String, Boolean> rx) {
        this.rx = rx;
    }
}
