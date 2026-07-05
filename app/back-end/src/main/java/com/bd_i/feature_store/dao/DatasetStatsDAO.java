package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.dto.DatasetActivityPointDTO;
import com.bd_i.feature_store.dto.DatasetActivitySummaryDTO;
import com.bd_i.feature_store.dto.HourlyActivityPointDTO;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DatasetStatsDAO implements AutoCloseable {
    private final ConnectionStrategy connectionStrategy;
    private Connection connection = null;

    public DatasetStatsDAO(ConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    abstract public List<DatasetActivityPointDTO> selectDailyActivity(int days) throws SQLException;
    abstract public List<DatasetActivitySummaryDTO> selectDatasetActivitySummary() throws SQLException;
    abstract public List<HourlyActivityPointDTO> selectHourlyActivity() throws SQLException;

    protected Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = connectionStrategy.connect();
        }

        return connection;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connectionStrategy.disconnect(connection);
        }
    }
}
