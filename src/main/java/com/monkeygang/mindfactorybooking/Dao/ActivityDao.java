package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.Activity;
import com.monkeygang.mindfactorybooking.Objects.Organisation_type;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ActivityDao implements Dao{

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public ActivityDao() throws SQLException, IOException {
    }

    @Override
    public Optional get(long id) throws SQLException, IOException {

        if (id == 0) {
            return Optional.of(new Activity(0, "No activity"));
        }

        PreparedStatement ps = con.prepareStatement("SELECT * FROM activity WHERE id = ?");
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        return Optional.of(new Activity(
                rs.getInt("id"),
                rs.getString("name"),
                Organisation_type.values()[rs.getInt("type")]));

    }

    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

    }




}
