package com.monkeygang.mindfactorybooking.Dao;

import com.monkeygang.mindfactorybooking.Objects.CurrentBookingSingleton;
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

    Connection con = ConnectionSingleton.getInstance().getConnection();

    public CustomerDao() throws SQLException, IOException {

        Connection con = ConnectionSingleton.getInstance().getConnection();

    }

    @Override
    public Optional get(long id) throws SQLException, IOException {

        OrganisationDao companyDao = new OrganisationDao();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM customer WHERE id = ?");

        ps.setLong(1, id);

        ResultSet rs = ps.executeQuery();

        rs.next();

        return Optional.of(new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("title"),
                rs.getString("mail"),
                rs.getString("phone"),
                //TODO: make null safe
                (Organization) companyDao.get(rs.getInt("organisation_id")).get()));

    }



    @Override
    public List getAll() throws SQLException, IOException {
        return null;
    }

    @Override
    public void save(Object o) throws SQLException, IOException {

        System.out.println("saving customer");

        CurrentBookingSingleton currentBookingSingleton = CurrentBookingSingleton.getInstance();

        int organization_id = currentBookingSingleton.getOrganization().getId();

        System.out.println("organization_id: " + organization_id);

        Customer customer = currentBookingSingleton.getCustomer();

        PreparedStatement ps = con.prepareStatement(
                "IF NOT EXISTS " +
                        "( SELECT 1 FROM customer WHERE name = ? AND title = ? AND mail = ? AND phone = ? AND organisation_id = ? )" +
                        "    INSERT INTO customer VALUES (?,?,?,?,?);"
                        ,
                PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, customer.getName());
        ps.setString(2,customer.getTitle());
        ps.setString(3,customer.getEmail());
        ps.setString(4, customer.getPhone());
        ps.setInt(5,organization_id);

        ps.setString(6, customer.getName());
        ps.setString(7,customer.getTitle());
        ps.setString(8,customer.getEmail());
        ps.setString(9, customer.getPhone());
        ps.setInt(10,organization_id);


        int customer_id = ps.executeUpdate();

        CurrentBookingSingleton.getInstance().getBooking().setId(customer_id);
        CurrentBookingSingleton.getInstance().getCustomer().setId(customer_id);


    }

    @Override
    public void update(Object o, String[] params) throws SQLException {

    }

    @Override
    public void delete(Object o) throws SQLException, IOException {

    }
}
