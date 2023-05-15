package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.Organization;
import com.monkeygang.mindfactorybooking.Objects.Organisation_type;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrganisationDao implements Dao{

    Connection con;

    public OrganisationDao() throws SQLException, IOException {

        con = ConnectionSingleton.getInstance().getConnection();

    }

    @Override
    public Optional get(long id) throws SQLException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM organization WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();
        return Optional.of(new Organization(rs.getInt("id"), rs.getString("name"), Organisation_type.values()[rs.getInt("type")]));
    }

    @Override
    public List getAll() throws SQLException, IOException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM organization");

        ResultSet rs = ps.executeQuery();

        List<Organization> organizations = new LinkedList<>();

        while (rs.next()) {
            Organization organization = new Organization(
                    rs.getInt("id"),
                    rs.getString("name"),
                    Organisation_type.values()[rs.getInt("type")]);
            //TODO: make null safe
            organizations.add(organization);
        }

        return organizations;


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
