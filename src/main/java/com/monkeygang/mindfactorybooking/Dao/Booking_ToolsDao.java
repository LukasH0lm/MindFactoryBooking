package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.Booking;
import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Redskaber;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Booking_ToolsDao implements Dao{
    @Override
    public Optional get(long id) throws SQLException, IOException {
        return Optional.empty();
    }

    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

        System.out.println("saving booking_redskaber");

        Connection con = ConnectionSingleton.getInstance().getConnection();

        List<Redskaber> redskaberList = CurrentBookingSingleton.getInstance().getCurrentRedskaber();

        if (redskaberList.isEmpty()) {
            System.out.println("no redskaber selected");
            return;
        }

        Booking booking = CurrentBookingSingleton.getInstance().getBooking();


        PreparedStatement ps = con.prepareStatement("INSERT INTO booking_tools (booking_id, redskabs_id) VALUES (?, ?)");


        for(Redskaber redskaber : redskaberList) {
            ps.setInt(1, booking.getId());
            ps.setInt(2, redskaber.getId());
            ps.addBatch();
        }

        ps.executeBatch();


    }

    public void deleteByBookingId(int id) throws SQLException, IOException {

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("DELETE FROM booking_tools WHERE booking_id = ?");

        ps.setInt(1, id);

        ps.execute();

    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

    }
}
