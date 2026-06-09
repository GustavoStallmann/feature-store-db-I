package com.bd_i.feature_store.persistence;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class PgConnectionStrategy implements ConnectionStrategy {
    @Value("${spring.pg.url}")
    private String url;

    @Value("${spring.pg.user}")
    private String user;

    @Value("${spring.pg.password}")
    private String password;

    @Override
    public Connection connect() throws SQLException {
        Connection connection = null;
        try {
            String connectionUrl = String.format("jdbc:postgresql://%s", url);
            connection = DriverManager.getConnection(connectionUrl, user, password);
            return connection;
        } catch (SQLException e){
            System.err.printf("PostgresDB connection failed %s\n",e.getMessage());
            throw e;
        }
    }

    @Override
    public void disconnect(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Database connection close failed");
        }
    }
}
