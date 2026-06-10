package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.persistence.ConnectionStrategy;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public abstract class DAO<T, K> implements AutoCloseable {
    private final ConnectionStrategy connectionStrategy;
    private Connection connection = null;

    abstract protected T modelMapper(ResultSet resultSet) throws SQLException;

    abstract public List<T> list() throws SQLException;
    abstract public void create(T model) throws SQLException;
    abstract public void update(T model) throws SQLException;
    abstract public T select(K id) throws SQLException;
    abstract public void delete(K id) throws SQLException;

    Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = connectionStrategy.connect();
        }

        return connection;
    }

    protected ConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    @Override
    public void close() throws Exception {
        connectionStrategy.disconnect(connection);
    }
}
