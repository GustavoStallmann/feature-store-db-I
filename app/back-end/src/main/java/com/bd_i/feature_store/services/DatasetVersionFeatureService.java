package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetVersionDAO;
import com.bd_i.feature_store.dao.DatasetVersionFeatureDAO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.DatasetVersionFeature;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatasetVersionFeatureService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetVersionFeature> listFeatures() throws SQLException {
        try (DatasetVersionFeatureDAO datasetVersionFeatureDAO = DaoFactory.getDatasetVersionFeatureDAO(pgConnectionStrategy)) {
            return datasetVersionFeatureDAO.list();
        }
    }

    public List<DatasetVersionFeature> listFeaturesByDatasetVersionId(UUID datasetVersionId) throws SQLException {
        getDatasetVersion(datasetVersionId);

        try (DatasetVersionFeatureDAO datasetVersionFeatureDAO = DaoFactory.getDatasetVersionFeatureDAO(pgConnectionStrategy)) {
            return datasetVersionFeatureDAO.selectByDatasetVersionId(datasetVersionId);
        }
    }

    private DatasetVersion getDatasetVersion(UUID id) throws SQLException {
        DatasetVersion datasetVersion;
        try (DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy)) {
            datasetVersion = datasetVersionDAO.select(id);
        }

        if (datasetVersion == null) {
            throw new ResourceNotFound("Versão do dataset não encontrada");
        }

        return datasetVersion;
    }
}
