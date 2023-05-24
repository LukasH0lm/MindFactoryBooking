package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.*;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.awt.print.Book;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Booking_ActivityDao implements Dao{

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public Booking_ActivityDao() throws SQLException, IOException {
    }

    @Override
    public Optional get(long id) throws SQLException, IOException {
        return Optional.empty();
    }

    public Activity getActivityByBookingId(long id) throws SQLException, IOException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM booking_activity WHERE booking_id = ?");

        ps.setLong(1, id);

        ps.execute();

        ResultSet rs = ps.getResultSet();

        if (rs.next()) {
            return new Activity(rs.getInt("activity_id"), rs.getString("activity_name"), Organisation_type.values()[ rs.getInt("type")]);
        }

        return null;

    }
    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    public Integer getActivityIDbyBookingID(int bookingID) throws SQLException, IOException {
        PreparedStatement ps = con.prepareStatement("SELECT activity_id FROM booking_activity WHERE booking_id = ?");
        ps.setInt(1, bookingID);
        ps.execute();

        if (ps.getResultSet().next()) {
            return ps.getResultSet().getInt("activity_id");
        }

        // Hvis Booking ikke har en aktivitet, returner 0
        return 0;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

        System.out.println("saving booking_activity");

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();


        if (currentBookingSingleton.getActivity().getId() == 0) {
            System.out.println("no activity selected");
            return;
        }


        PreparedStatement ps = con.prepareStatement("INSERT INTO booking_activity (booking_id, activity_id) VALUES (?, ?)");

        ps.setInt(1, currentBookingSingleton.getBooking().getId());
        ps.setInt(2, currentBookingSingleton.getActivity().getId());

        ps.execute();


    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {


        Booking booking = (Booking) o;

        PreparedStatement ps = con.prepareStatement("DELETE FROM booking_activity WHERE booking_id = ?");
        ps.setInt(1, booking.getId());
        ps.execute();



    }


    public void deleteById(int id) throws SQLException, IOException {


        PreparedStatement ps = con.prepareStatement("DELETE FROM booking_activity WHERE booking_id = ?");
        ps.setInt(1, id);
        ps.execute();



    }

}
