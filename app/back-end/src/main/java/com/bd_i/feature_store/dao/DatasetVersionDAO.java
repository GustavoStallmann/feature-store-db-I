package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DatasetVersionDAO extends DAO<DatasetVersion, UUID> {
    public DatasetVersionDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<DatasetVersion> selectByDatasetId(UUID id) throws SQLException;
    public abstract DatasetVersion selectByVersionAndDatasetId(int version, UUID datasetId) throws SQLException;
}
