package com.bd_i.feature_store.services;


import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetDAO;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<Dataset> listDatasets() throws SQLException {
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        return datasetDAO.list();
    }
}
