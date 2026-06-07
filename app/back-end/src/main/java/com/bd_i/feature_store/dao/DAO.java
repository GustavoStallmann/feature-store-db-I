package com.bd_i.feature_store.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, K> {
    public List<T> list() throws SQLException;
    public void create(T model) throws SQLException;
    public void update(T model) throws SQLException;
    public void select(K id) throws SQLException;
    public void delete(K id) throws SQLException;
}
