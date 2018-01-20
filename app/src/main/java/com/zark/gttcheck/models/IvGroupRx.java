package com.zark.gttcheck.models;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class IvGroupRx {

    private String name;
    private Boolean central;

    public IvGroupRx(String name, Boolean central) {
        this.name = name;
        this.central = central;
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
}
