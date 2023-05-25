package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SubjectDao implements Dao {

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public SubjectDao() throws SQLException, IOException {
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

        PreparedStatement ps = con.prepareStatement("INSERT INTO subject (name) VALUES (?)");


    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

    }
}
