package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Customer;
import com.monkeygang.mindfactorybooking.Objects.Organization;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;
import com.monkeygang.mindfactorybooking.Objects.Booking;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BookingDao implements Dao {

    Connection con;

    Timestamp lastStartTime;
    Timestamp lastEndTime;

    public BookingDao() throws SQLException, IOException {

        con = ConnectionSingleton.getInstance().getConnection();


    }

    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    public Booking getFromTimeStamps(Timestamp startTime, Timestamp endTime) throws SQLException, IOException {

        List<Booking> bookings = getAll();

        for (Booking booking : bookings) {

            if (startTime.equals(booking.getStartTime()) && endTime.equals(booking.getEndTime())){
                return booking;
            }
        }

        System.out.println("no new bookings");
        return null;

    }

    @Override
    public List getAll() throws SQLException, IOException {

        System.out.println("loading all bookings from database");

        ArrayList<Booking> allBookings = new ArrayList<>();

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM booking");

        ResultSet rs = ps.executeQuery();

        CustomerDao customerDao = new CustomerDao();

        while (rs.next()) {
            Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getTimestamp("start_time"),
                    rs.getTimestamp("end_time"),
                    rs.getInt("amount_of_visitors"),
                    //TODO: make null safe



                    (Customer) customerDao.get(rs.getInt("customer_id")).get());

            allBookings.add(booking);
        }


        return allBookings;

    }

    public PreparedStatement generatePreparedStatement(PreparedStatement ps, Object o) throws SQLException {

        //id is auto increment, so no need to add it here

        ps.setTimestamp(1, ((Booking) o).getStartTime());
        ps.setTimestamp(2, ((Booking) o).getEndTime());
        ps.setInt(3, ((Booking) o).getAmount_of_people());
        ps.setInt(4, ((Booking) o).getCustomer().getId());

        return ps;
    }



    @Override
    public void save(Object o) throws SQLException {


        System.out.println("saving booking");

        PreparedStatement ps = con.prepareStatement("INSERT INTO booking VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();



        ps.setTimestamp(1, currentBookingSingleton.getBooking().getStartTime());
        ps.setTimestamp(2, currentBookingSingleton.getBooking().getEndTime());
        ps.setInt(3, currentBookingSingleton.getBooking().getAmount_of_people());
        ps.setInt(4, currentBookingSingleton.getBooking().getCustomer().getId());

        int Booking_id = ps.executeUpdate();

        System.out.println("Booking saved");
        System.out.println("booking id: " + Booking_id);
        System.out.println("hashcode: " + o.hashCode());


        CurrentBookingSingleton.getInstance().getBooking().setId(Booking_id);


        //TODO: add booking_catering
        //TODO: add booking_activity
        //TODO: add booking_subject if organization is school

    }

    @Override
    public void update(Object o, String[] args) throws SQLException {

        PreparedStatement ps = con.prepareStatement("UPDATE booking SET start_time = ?, end_time = ?, amount_of_visitors = ? WHERE id = ?");

        ps = generatePreparedStatement(ps,o);

        ps.setInt(9, ((Booking) o).getId());

        ps.execute();

        System.out.println("Booking updated");
        System.out.println("hashcode: " + o.hashCode());

        lastStartTime = ((Booking) o).getStartTime();
        lastEndTime = ((Booking) o).getEndTime();

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

        Booking booking = (Booking) o;

        PreparedStatement ps = con.prepareStatement("DELETE FROM booking WHERE id = ?");

        Booking_CateringDao booking_cateringDAO = new Booking_CateringDao();

        booking_cateringDAO.deleteByBookingId(booking.getId());

        ps.setInt(1, booking.getId());
        ps.execute();

    }

    public Organization getOrganisation(Booking booking) throws SQLException, IOException {

        CustomerDao customerDao = new CustomerDao();

        Customer customer = (Customer) customerDao.get(booking.getCustomer().getId()).get();

        OrganisationDao organisationDao = new OrganisationDao();

        return (Organization) organisationDao.get(customer.getOrganisation().getId()).get();


    }
}
