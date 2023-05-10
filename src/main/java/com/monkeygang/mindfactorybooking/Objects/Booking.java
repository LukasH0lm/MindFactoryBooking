package com.monkeygang.mindfactorybooking.Objects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;

public class Booking {

    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String organisation;
    private String field;
    private String responsible;
    private String title_of_responsible;
    private String telephone;
    private int amount_of_people;


    public Booking(int id, Timestamp startTime, Timestamp endTime, String organisation, String field, String responsible, int amount_of_people, String telephone, String title_of_responsible) {
        this.id = id;

        Calendar cal = Calendar.getInstance();


        this.startTime = startTime;

        this.endTime = endTime;

        this.organisation = organisation;
        this.field = field;
        this.responsible = responsible;
        this.amount_of_people = amount_of_people;
        this.telephone = telephone;
        this.title_of_responsible = title_of_responsible;

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

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getTitle_of_responsible() {
        return title_of_responsible;
    }

    public void setTitle_of_responsible(String title_of_responsible) {
        this.title_of_responsible = title_of_responsible;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAmount_of_people() {
        return amount_of_people;
    }

    public void setAmount_of_people(int amount_of_people) {
        this.amount_of_people = amount_of_people;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && amount_of_people == booking.amount_of_people && Objects.equals(startTime, booking.startTime) && Objects.equals(endTime, booking.endTime) && Objects.equals(organisation, booking.organisation) && Objects.equals(field, booking.field) && Objects.equals(responsible, booking.responsible) && Objects.equals(title_of_responsible, booking.title_of_responsible) && Objects.equals(telephone, booking.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, organisation, field, responsible, title_of_responsible, telephone, amount_of_people);
    }
}
