package com.monkeygang.mindfactorybooking.DAO;

import com.monkeygang.mindfactorybooking.BuisnessLogic.ConnectionSingleton;
import com.monkeygang.mindfactorybooking.Objects.Booking;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BookingDAO implements Dao {

    Connection con;

    Timestamp lastStartTime;
    Timestamp lastEndTime;

    public BookingDAO() throws SQLException, IOException {

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

        ArrayList<Booking> allBookings = new ArrayList<>();

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM booking");

        ResultSet rs = ps.executeQuery();


        while (rs.next()) {
            Booking booking = new Booking(rs.getInt("id"), rs.getTimestamp("start_time"), rs.getTimestamp("end_time"), rs.getString("organisation"), rs.getString("field"), rs.getString("responsible"), rs.getInt("amount_of_people"), rs.getString("telephone"), rs.getString("title_of_responsible"));
            allBookings.add(booking);
        }


        return allBookings;

    }

    public PreparedStatement generatePreparedStatement(PreparedStatement ps, Object o) throws SQLException {

        //id is auto increment, so no need to add it here

        ps.setTimestamp(1, ((Booking) o).getStartTime());
        ps.setTimestamp(2, ((Booking) o).getEndTime());
        ps.setString(3, ((Booking) o).getOrganisation());
        ps.setString(4, ((Booking) o).getField());
        ps.setString(5, ((Booking) o).getResponsible());
        ps.setInt(6, ((Booking) o).getAmount_of_people());
        ps.setString(7, ((Booking) o).getTelephone());
        ps.setString(8, ((Booking) o).getTitle_of_responsible());

        return ps;
    }



    @Override
    public void save(Object o) throws SQLException {

        PreparedStatement ps = con.prepareStatement("INSERT INTO booking VALUES (?,?,?,?,?,?,?,?)");

        ps = generatePreparedStatement(ps,o);

        ps.execute();

        System.out.println("Booking saved");
        System.out.println("hashcode: " + o.hashCode());

        lastStartTime = ((Booking) o).getStartTime();
        lastEndTime = ((Booking) o).getEndTime();

        System.out.println("last start time: " + lastStartTime);
        System.out.println("last end time: " + lastEndTime);

    }

    @Override
    public void update(Object o, String[] args) throws SQLException {

        PreparedStatement ps = con.prepareStatement("UPDATE booking SET start_time = ?, end_time = ?, organisation = ?, field = ?, responsible = ?, amount_of_people = ?, telephone = ?, title_of_responsible = ? WHERE id = ?");

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

        Booking_CateringDAO booking_cateringDAO = new Booking_CateringDAO();

        booking_cateringDAO.deleteByBookingId(booking.getId());

        ps.setInt(1, booking.getId());
        ps.execute();

    }
}
