package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Booking_CateringDao implements Dao{
    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

        System.out.println("saving booking_catering");

        Connection con = ConnectionSingleton.getInstance().getConnection();

        if (CurrentBookingSingleton.getInstance().getCatering().getId() == 0) {
            System.out.println("no catering selected");
            return;
        }

        Booking booking = CurrentBookingSingleton.getInstance().getBooking();
        Catering catering = CurrentBookingSingleton.getInstance().getCatering();

        PreparedStatement ps = con.prepareStatement("INSERT INTO booking_catering (booking_id, catering_id) VALUES (?, ?)");

        ps.setInt(1, booking.getId());
        ps.setInt(2, catering.getId());

        ps.execute();


    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException {

    }

    public void deleteByBookingId(int id) throws SQLException, IOException {

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM booking_catering WHERE booking_id = ?");

        ps.setInt(1, id);

        ps.execute();

    }
}
