package com.monkeygang.mindfactorybooking.Objects;

public class Catering {

    int id;
    String name;

    int base_price;
    int price_per_person;


    public Catering(int id, String name, int base_price, int price_per_person) {
        this.id = id;
        this.name = name;
        this.base_price = base_price;
        this.price_per_person = price_per_person;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBase_price() {
        return base_price;
    }

    public int getPrice_per_person() {
        return price_per_person;
    }
}
