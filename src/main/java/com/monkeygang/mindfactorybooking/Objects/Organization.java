package com.monkeygang.mindfactorybooking.Objects;

public class Organization {


    int id;
    String name;
    Organisation_type type;

    public Organization(int id, String name, Organisation_type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Organisation_type getType() {
        return type;
    }


    @Override
    public String toString() {
        return name;
    }

}
