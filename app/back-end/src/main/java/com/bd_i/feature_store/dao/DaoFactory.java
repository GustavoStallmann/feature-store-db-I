package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.persistence.ConnectionStrategy;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;

import java.sql.SQLException;

public class DaoFactory {
    private DaoFactory() {}

    public static UserDAO getUserDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgUserDAO(connectionStrategy);
        }

        return null;
    }

    public static DatasetDAO getDatasetDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgDatasetDAO(connectionStrategy);
        }

        return null;
    }

    public static DatasetVersionDAO getDatasetVersionDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgDatasetVersionDAO(connectionStrategy);
        }

        return null;
    }

    public static DatasetDownloadDAO getDownloadDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgDatasetDownloadDAO(connectionStrategy);
        }

        return null;
    }

    public static DatasetVersionAccessDAO getDatasetVersionAccessDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        if (connectionStrategy instanceof PgConnectionStrategy) {
            return new PgDatasetVersionAccessDAO(connectionStrategy);
        }

        return null;
    }
}
