package com.monkeygang.mindfactorybooking.Objects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

public class Booking {

    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private int amount_of_people;
    private Customer customer;


    //overloading constructor for different situations
    //delete if not used
    public Booking(Timestamp startTime, Timestamp endTime, int amount_of_people, Customer customer) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount_of_people = amount_of_people;
        this.customer = customer;
    }

    public Booking(int id, Timestamp startTime, Timestamp endTime, int amount_of_people, Customer customer) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount_of_people = amount_of_people;
        this.customer = customer;
    }

    public Booking(Timestamp startTime, Timestamp endTime, int amount_of_people) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount_of_people = amount_of_people;
    }


    public Booking(Timestamp startTime, Timestamp endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }



    public int getId() {
        return id;
    }

    public void setId(int bookingId) {
        this.id = bookingId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public int getAmount_of_people() {
        return amount_of_people;
    }

    public Customer getCustomer() {
        return customer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != booking.id) return false;
        if (amount_of_people != booking.amount_of_people) return false;
        if (!startTime.equals(booking.startTime)) return false;
        if (!endTime.equals(booking.endTime)) return false;
        return customer.equals(booking.customer);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + amount_of_people;
        result = 31 * result + customer.hashCode();
        return result;
    }

    public void setAmountOfPeople(int i) {

        this.amount_of_people = i;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}
