package com.zark.gttcheck.models;

import android.support.annotation.Nullable;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRx {

    private String name;
    private Boolean central;
    private String reference;

    public IvGroupRx(String name, Boolean central, @Nullable String reference) {
        this.name = name;
        this.central = central;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCentral() {
        return central;
    }

    public void setCentral(Boolean central) {
        this.central = central;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
