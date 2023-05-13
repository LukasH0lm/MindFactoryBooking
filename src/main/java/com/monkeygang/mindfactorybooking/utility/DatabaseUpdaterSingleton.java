package com.monkeygang.mindfactorybooking.utility;

import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.Customer;

public class DatabaseUpdaterSingleton {


    private static DatabaseUpdaterSingleton instance = null;

    private static Booking booking;
    private static Customer customer;
    private static Catering catering;


    private DatabaseUpdaterSingleton(){}


    public static synchronized DatabaseUpdaterSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseUpdaterSingleton();
        }
        return instance;
    }


    public void addBooking(Booking booking, Customer customer, Catering catering){
        //other things than the objects in the signature are needed to be added to the database,
        //but they can be inferred from the objects in the signature
        //for example the organisation of the customer can be inferred from the customer object



        //TODO: add methods from BookingController to this class
        //IDEA: for each step in the booking progress, send the object to this class
        //      and in the last step send them all to the database at once


        //order of adding to database:
        //1. Organization
        //2. Catering
        //3. Customer
        //4. customer_organisation
        //5. Booking
        //6. booking_catering
        //7. booking_customer






    }



    //idk what the fuck this method was meant with this but im sure I was thinking something brilliant
    public void updateDatabase(){

    }

}
