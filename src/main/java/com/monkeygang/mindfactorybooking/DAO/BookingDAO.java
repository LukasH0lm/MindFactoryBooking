package com.monkeygang.mindfactorybooking.DAO;

import com.monkeygang.mindfactorybooking.BuisnessLogic.ConnectionSingleton;
import com.monkeygang.mindfactorybooking.Objects.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class BookingDAO implements Dao{

    Connection con;

    public BookingDAO() throws SQLException {

        con = ConnectionSingleton.getInstance().getConnection();


    }

    @Override
    public Optional get(long id) {
        return Optional.empty();
    }

    @Override
    public List getAll() throws SQLException {

        ArrayList<Booking> allBookings = new ArrayList<>();

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM booking");

        ResultSet rs = ps.executeQuery();


        while (rs.next()){
            Booking booking = new Booking(rs.getInt("id"), rs.getTimestamp("start_time"), rs.getTimestamp("end_time"), rs.getString("organisation"), rs.getString("field"), rs.getString("responsible"), rs.getInt("amount_of_people"), rs.getString("telephone"), rs.getString("title_of_responsible"));
            allBookings.add(booking);
        }


        return allBookings;

    }

    @Override
    public void save(Object o) throws SQLException {


        PreparedStatement ps = con.prepareStatement("INSERT INTO booking VALUES (?,?,?,?,?,?,?,?,?)");

        ps.setInt(1, ((Booking) o).getId());
        ps.setTimestamp(2, ((Booking) o).getStartTime());
        ps.setTimestamp(3, ((Booking) o).getEndTime());
        ps.setString(4, ((Booking) o).getOrganisation());
        ps.setString(5, ((Booking) o).getField());
        ps.setString(6, ((Booking) o).getResponsible());
        ps.setInt(7, ((Booking) o).getAmount_of_people());
        ps.setString(8, ((Booking) o).getTelephone());
        ps.setString(9, ((Booking) o).getTitle_of_responsible());

        ps.execute();

    }

    @Override
    public void update(Object o, String[] params) {

    }

    @Override
    public void delete(Object o) {

    }
}
