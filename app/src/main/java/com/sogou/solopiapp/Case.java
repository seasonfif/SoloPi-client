package com.sogou.solopiapp;

import com.google.gson.annotations.SerializedName;

public class Case {

    String name;

    String rname;

    String module;

    String project;

    @SerializedName("package")
    String pkg;

    String description;

    String createdAt;

    public boolean exists;

    public Case(String name, String module, String project) {
        this.name = name;
        this.module = module;
        this.project = project;
    }
}
