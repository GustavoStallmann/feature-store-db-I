package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersionFeature;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DatasetVersionFeatureDAO extends DAO<DatasetVersionFeature, UUID> {
    public DatasetVersionFeatureDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<DatasetVersionFeature> selectByDatasetVersionId(UUID id) throws SQLException;
}
