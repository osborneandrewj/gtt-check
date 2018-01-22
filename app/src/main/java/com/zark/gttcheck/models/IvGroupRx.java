package com.zark.gttcheck.models;

import android.support.annotation.Nullable;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRx {

    private String name;
    private Boolean centralRequired;
    private String reference;

    public IvGroupRx(String name, Boolean centralRequired, String reference) {
        this.name = name;
        this.centralRequired = centralRequired;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCentralRequired() {
        return centralRequired;
    }

    public void setCentralRequired(Boolean centralRequired) {
        this.centralRequired = centralRequired;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
