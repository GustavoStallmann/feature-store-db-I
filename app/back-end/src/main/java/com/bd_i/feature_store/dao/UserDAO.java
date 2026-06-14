package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.UUID;

public abstract class UserDAO extends DAO<User, UUID>{
    public UserDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract User selectByCpf(String cpf) throws SQLException;
}
