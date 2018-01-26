package com.zark.gttcheck.models;

import java.util.Map;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class Rx {

    private String name;
    private Boolean central;
    private String ref;
    private Map<String, Boolean> iv;

    public Rx() {
        // Empty constructor used by Firebase
    }

    public Rx(String name, Boolean central) {
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Map<String, Boolean> getIv() {
        return iv;
    }

    public void setIv(Map<String, Boolean> iv) {
        this.iv = iv;
    }
}
