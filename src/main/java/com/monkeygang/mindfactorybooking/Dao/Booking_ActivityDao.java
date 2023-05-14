package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
import com.monkeygang.mindfactorybooking.Objects.Customer;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    @Override
    public List getAll() throws SQLException, IOException {
        return null;
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

    }
}
