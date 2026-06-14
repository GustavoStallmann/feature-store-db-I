package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetDownload;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DownloadDAO extends DAO<DatasetDownload, UUID> {
    public DownloadDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<DatasetDownload> selectByUserId(UUID id) throws SQLException;
    public abstract List<DatasetDownload> selectByDatasetVersionId(UUID id) throws SQLException;
}
