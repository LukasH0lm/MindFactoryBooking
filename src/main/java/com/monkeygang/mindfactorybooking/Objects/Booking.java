package com.monkeygang.mindfactorybooking.Objects;

import java.sql.Time;
import java.sql.Timestamp;

public class Booking {

    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private int amount_of_people;
    private Customer customer;
    private Time arrival_time;
    private Time departure_time;
    private boolean is_transport_public = false;


    public Booking(int id, Timestamp startTime, Timestamp endTime, int amount_of_people, Customer customer, Time arrival_time, Time departure_time, boolean is_transport_public) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amount_of_people = amount_of_people;
        this.customer = customer;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.is_transport_public = is_transport_public;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getAmount_of_people() {
        return amount_of_people;
    }

    public void setAmount_of_people(int amount_of_people) {
        this.amount_of_people = amount_of_people;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Time getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Time arrival_time) {
        this.arrival_time = arrival_time;
    }

    public Time getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Time departure_time) {
        this.departure_time = departure_time;
    }

    public boolean isIs_transport_public() {
        return is_transport_public;
    }

    public void setIs_transport_public(boolean is_transport_public) {
        this.is_transport_public = is_transport_public;
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

        if (customer == null) return result;

        result = 31 * result + customer.hashCode();
        return result;
    }



}
