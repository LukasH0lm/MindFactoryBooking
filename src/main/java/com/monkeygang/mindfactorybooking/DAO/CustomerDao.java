package com.monkeygang.mindfactorybooking.DAO;

import com.monkeygang.mindfactorybooking.Objects.Customer;
import com.monkeygang.mindfactorybooking.Objects.Organization;
import com.monkeygang.mindfactorybooking.utility.ConnectionSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerDao implements Dao {

    Connection con;

    CustomerDao() throws SQLException, IOException {

        Connection con = ConnectionSingleton.getInstance().getConnection();

    }

    @Override
    public Optional get(long id) throws SQLException, IOException {

        OrganisationDao companyDao = new OrganisationDao();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM customer WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        return Optional.of(new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("title"),
                rs.getString("mail"),
                rs.getString("phone"),
                //TODO: make null safe
                (Organization) companyDao.get(rs.getInt("company_id")).get()));

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
