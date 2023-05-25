package com.monkeygang.mindfactorybooking.utility;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {

    private static ConnectionSingleton instance;

    private Connection connection;


    private ConnectionSingleton() throws IOException {

        //  File file = new File(BookingApplication.class.getResource("").getPath() + "DatabaseSettings");

        // BufferedReader br = new BufferedReader(new FileReader(file));


        try {
            String serverName = "EASV-DB4";
            String databaseName = "CSe2002t_t_2_MindFactory";
            String userName = "CSe2022t_t_2";
            String password = "CSe2022tT2#";

            String url = "jdbc:sqlserver://" + serverName + ":1433;DatabaseName=" + databaseName + ";user=" + userName + ";password=" + password + ";encrypt=false;trustServerCertificate=true;";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("can not create connection");
            System.out.println(e.getMessage());
        }

        System.out.println("connected to DB");

    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectionSingleton getInstance() throws SQLException, IOException {

        if (instance == null) {
            instance = new ConnectionSingleton();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConnectionSingleton();
        }

        return instance;
    }


}
