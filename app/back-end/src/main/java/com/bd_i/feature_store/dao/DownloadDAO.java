package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.Download;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DownloadDAO extends DAO<Download, UUID> {
    public DownloadDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    public abstract List<Download> selectByUserId(UUID id) throws SQLException;
    public abstract List<Download> selectByDatasetVersionId(UUID id) throws SQLException;
}
