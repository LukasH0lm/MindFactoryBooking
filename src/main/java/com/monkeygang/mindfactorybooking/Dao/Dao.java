package com.monkeygang.mindfactorybooking.Dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id) throws SQLException, IOException;

    List<T> getAll() throws SQLException, IOException;

    void save(T t) throws SQLException, IOException;

    void update(T t, String[] params) throws SQLException;

    void delete(T t) throws SQLException, IOException;
}
