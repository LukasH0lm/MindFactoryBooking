package com.monkeygang.mindfactorybooking.utility;

import com.monkeygang.mindfactorybooking.Dao.BookingDao;
import com.monkeygang.mindfactorybooking.Dao.Booking_ActivityDao;
import com.monkeygang.mindfactorybooking.Dao.Booking_CateringDao;
import com.monkeygang.mindfactorybooking.Dao.CustomerDao;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Customer;

import java.io.IOException;
import java.sql.SQLException;

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


    public boolean addCurrentBookingSingletonToDatabase() throws SQLException, IOException {
        //other things than the objects in the signature are needed to be added to the database,
        //but they can be inferred from the objects in the signature
        //for example the organisation of the customer can be inferred from the customer object


        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        if (currentBookingSingleton.getBooking() == null) {
            System.out.println("booking is null");
            return false;
        }

        if (currentBookingSingleton.getCustomer() == null) {
            System.out.println("customer is null");
            return false;
        }

        if (currentBookingSingleton.getCatering() == null) {
            System.out.println("catering is null");
            return false;
        }

        if (currentBookingSingleton.getActivity() == null) {
            System.out.println("activity is null");
            return false;
        }

        if (currentBookingSingleton.getOrganization() == null) {
            System.out.println("organization is null");
            return false;
        }



        //TODO: add methods from BookingController to this class
        //IDEA: for each step in the booking progress, send the object to this class
        //      and in the last step send them all to the database at once


        //order of adding to database:

        //1. Customer
        //2. Booking
        //3. booking_catering
        //4. booking_activity
        //5. booking_field (if applicable)


        //1. Customer

        CustomerDao customerDao = new CustomerDao();
        customerDao.save(currentBookingSingleton.getCustomer());

        //2. Booking


        BookingDao bookingDao = new BookingDao();
        bookingDao.save(currentBookingSingleton.getBooking());


        //3. booking_catering

        if (currentBookingSingleton.getCatering().getId() == 0) {
            System.out.println("no catering selected");
        }else{

            Booking_CateringDao booking_cateringDao = new Booking_CateringDao();

            booking_cateringDao.save(currentBookingSingleton);

        }




        //4. booking_activity

        if(currentBookingSingleton.getActivity().getId() == 0){
            System.out.println("no activity selected");
        }else{

            Booking_ActivityDao booking_activityDao = new Booking_ActivityDao();

            booking_activityDao.save(currentBookingSingleton);

        }


        //5. booking_field (if applicable)

        if(currentBookingSingleton.getSubject() == null){
            System.out.println("no subject selected");}
        else {

            //SubjectDAO subjectDAO = new SubjectDAO();

        }



        return true;


    }



    //idk what the fuck this method was meant with this but im sure I was thinking something brilliant
    public void updateDatabase(){

    }

}
