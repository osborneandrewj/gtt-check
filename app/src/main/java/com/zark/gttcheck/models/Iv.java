package com.zark.gttcheck.models;

import java.util.Map;

/**
 * Created by Osborne on 1/19/2018.
 *
 */

public class Iv {

    private String name;
    private String ref;
    private Boolean central;
    private Map<String, Boolean> rx;

    public Iv() {

    }

    public Iv(String name, String ref) {
        this.name = name;
        this.ref = ref;
    }

    public Iv(String name, String ref, Boolean central) {
        this.name = name;
        this.ref = ref;
        this.central = central;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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
