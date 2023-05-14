package com.monkeygang.mindfactorybooking.Objects;

public class Activity {


    int id;
    String name;
    Organisation_type organisation_type;
    public Activity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Activity(int id, String name, Organisation_type organisation_type) {
        this.id = id;
        this.name = name;
        this.organisation_type = organisation_type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
