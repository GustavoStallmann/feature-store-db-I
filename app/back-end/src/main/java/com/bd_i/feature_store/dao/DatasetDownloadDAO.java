package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersionDownload;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DatasetDownloadDAO extends DAO<DatasetVersionDownload, UUID> {
    public DatasetDownloadDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<DatasetVersionDownload> selectByUserId(UUID id) throws SQLException;
    public abstract List<DatasetVersionDownload> selectByDatasetVersionId(UUID id) throws SQLException;
}
