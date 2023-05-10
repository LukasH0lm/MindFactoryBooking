package com.monkeygang.mindfactorybooking.DAO;

import com.monkeygang.mindfactorybooking.BuisnessLogic.ConnectionSingleton;
import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Booking_CateringDAO implements Dao{
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

        if (!(o instanceof Catering)) {
            throw new IllegalArgumentException("o must be instance of Catering");
        }

        Catering catering = (Catering) o;

        Connection con = ConnectionSingleton.getInstance().getConnection();

        BookingDAO bookingDao = new BookingDAO();

        Booking booking = CurrentBookingSingleton.getInstance().getBooking();

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
