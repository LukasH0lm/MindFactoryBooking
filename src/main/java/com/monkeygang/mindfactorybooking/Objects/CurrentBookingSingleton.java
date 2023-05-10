package com.monkeygang.mindfactorybooking.Objects;

public class CurrentBookingSingleton{


    static CurrentBookingSingleton instance;
    Booking booking = null;
    boolean isEdit = false;

    public CurrentBookingSingleton() {

    }

    public static synchronized CurrentBookingSingleton getInstance() {
        if (instance == null) {
            instance = new CurrentBookingSingleton();
        }
        return instance;
    }

    public synchronized Booking getBooking() {

        if (booking == null) {
            System.out.println("booking is null");
            return new Booking(-1, null, null, null, null, null, -1, null, null);
        }
        return booking;

    }

    public synchronized void setCurrentBooking(Booking instance) {
        this.booking = instance;
    }

    public synchronized void setIsEdit(boolean state){
        isEdit = state;
    }

    public synchronized boolean getIsEdit(){
        return isEdit;
    }

}
