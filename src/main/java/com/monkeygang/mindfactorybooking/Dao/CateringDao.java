package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.Catering;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CateringDao implements Dao {

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public CateringDao() throws SQLException, IOException {
    }

    @Override
    public Optional get(long id) throws SQLException, IOException {

        if (id == 0) {
            return Optional.of(new Catering(0, "No catering", 0, 0));
        }

        PreparedStatement ps = con.prepareStatement("SELECT * FROM catering WHERE id = ?");
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        return Optional.of(new Catering(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("base_price"),
                rs.getInt("price_per_person")));

    }

    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

        //we don't save catering at this moment


    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

    }
}
