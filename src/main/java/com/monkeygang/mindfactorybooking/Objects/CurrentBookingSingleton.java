package com.monkeygang.mindfactorybooking.Objects;

import com.monkeygang.mindfactorybooking.Dao.BookingDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrentBookingSingleton {


    static CurrentBookingSingleton instance;
    Booking booking = null;
    Organization organization = null;

    private List<Redskaber> redskaberList = new ArrayList<>();


    Activity activity = null;
    int activity_id = -1;

    Catering catering = null;
    int catering_id = -1;

    Customer customer = null;

    Subject subject = null;

    boolean isEdit = false;
    boolean isTemporary = false;

    private CurrentBookingSingleton() {

    }

    public static synchronized CurrentBookingSingleton getInstance() {
        if (instance == null) {
            instance = new CurrentBookingSingleton();
        }
        return instance;
    }


    public synchronized Organization getOrganization() {

        if (organization == null) {
            System.out.println("organization is null");
            return new Organization(-1, null, null);
        }
        return organization;

    }

    public synchronized void setCurrentOrganization(Organization instance) {
        this.organization = instance;
    }

    public synchronized Booking getBooking() {

        /*
        if (booking == null) {
            System.out.println("booking is null");
            return new Booking(null, null, -1, null);
        }
        */
        return booking;

    }

    public synchronized void setCurrentBooking(Booking instance) {
        this.booking = instance;
    }


    public synchronized Catering getCatering() {

        if (catering == null) {
            System.out.println("catering is null");
            return new Catering(-1, null, -1, -1);
        }
        return catering;

    }

    public synchronized List<Redskaber> getCurrentRedskaber() {

        if (redskaberList == null) {
            System.out.println("redskaberList is null");
            return null;
        }

        return redskaberList;

    }



    public synchronized void setCurrentRedskaber(Redskaber redskaber) {
        redskaberList.add(redskaber);
    }



    public synchronized void setCurrentCatering(Catering instance) {
        this.catering = instance;
    }








    public synchronized int getCateringId() {

        if (catering_id == -1) {
            System.out.println("catering_id is null");
            return -1;
        }
        return catering_id;

    }

    public synchronized void setCateringId(int instance) {

        this.catering_id = instance;
    }

    public synchronized void setIsEdit(boolean state) {
        isEdit = state;
    }

    public synchronized boolean getIsEdit() {
        return isEdit;
    }

    public void setActivityId(int i) {

        this.activity_id = i;

    }

    public int getActivityId() {

        return activity_id;

    }

    public void reset() throws SQLException, IOException {

        if (!isEdit && booking != null && isTemporary) {
            BookingDao bookingDao = new BookingDao();
            bookingDao.delete(booking);
        }

        instance = null;

    }


    public boolean getIsTemporary() {

        return isTemporary;

    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CurrentBookingSingleton{" +
                "Start tid: " + booking.getStartTime() +
                "\nSlut tid: " + booking.getEndTime() +
                "\nOrganisation: " + organization.getName() +
                "\nForplejning: " + catering.getName() +
                "\nAktivitet: " + activity.getName() +
                "\nNavn: " + customer.getName() +
                "\nEmail: " + customer.getEmail() +

                '}';
    }


    public String toUIString() {
        return
                "Start tid: " + booking.getStartTime() +
                        "\nSlut tid: " + booking.getEndTime() +
                        "\nOrganisation: " + organization.getName() +
                        "\nForplejning: " + catering.getName() +
                        "\nAktivitet: " + activity.getName() +
                        "\nNavn: " + customer.getName() +
                        "\nEmail: " + customer.getEmail();


    }


    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {

            this.activity = activity;

    }


    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public void setIsTemporary(boolean b) {

        isTemporary = b;

    }
}
