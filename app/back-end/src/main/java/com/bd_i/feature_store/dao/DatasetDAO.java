package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.util.UUID;

public abstract class DatasetDAO extends DAO<Dataset, UUID>{
    public DatasetDAO (ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }
}
