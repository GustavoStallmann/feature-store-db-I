package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.persistence.ConnectionStrategy;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;

import java.sql.SQLException;

public class DaoFactory {
    private DaoFactory() {}

    public static PgUserDAO getUserDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgUserDAO(connectionStrategy);
        }

        return null;
    }
}
