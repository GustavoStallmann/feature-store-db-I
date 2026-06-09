package com.bd_i.feature_store.persistence;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionStrategy {
    Connection connect() throws SQLException;
    void disconnect(Connection connection) throws SQLException;
}
