package com.monkeygang.mindfactorybooking.Objects;

import java.util.Objects;

public class Redskaber {

    private String name;
    private int id;


    public Redskaber(int id, String name) {
        this.name = name;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Redskaber redskaber = (Redskaber) o;

        if (id != redskaber.id) return false;
        if (!Objects.equals(name, redskaber.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }


}
