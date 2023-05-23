package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.Redskaber;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolsDao implements Dao{

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public ToolsDao() throws SQLException, IOException {
    }

    @Override
    public Optional get(long id) throws SQLException, IOException {


        if (id == 0) {
            return Optional.of(new Redskaber(0, "Ingen redskaber"));
        }

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tools WHERE id = ?");
        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        return Optional.of(new Redskaber(
                rs.getInt("id"),
                rs.getString("name")));
    }

    @Override
    public List getAll() throws SQLException, IOException {
        System.out.println("loading all redskaber from database xD");

        ArrayList<Redskaber> allTools = new ArrayList<>();

        Connection con = ConnectionSingleton.getInstance().getConnection();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tools");


        ResultSet rs = ps.executeQuery();




        while (rs.next()) {
            Redskaber redskaber = new Redskaber(
                    rs.getInt("id"),
                    rs.getString("name"));


            allTools.add(redskaber);
        }


        return allTools;

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
