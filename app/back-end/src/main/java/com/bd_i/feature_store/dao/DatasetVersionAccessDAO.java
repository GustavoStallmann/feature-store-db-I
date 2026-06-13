package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersionAccess;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DatasetVersionAccessDAO extends DAO<DatasetVersionAccess, UUID> {
    public DatasetVersionAccessDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<DatasetVersionAccess> selectByUserId(UUID id) throws SQLException;
    public abstract List<DatasetVersionAccess> selectByDatasetId(UUID id) throws SQLException;
}
