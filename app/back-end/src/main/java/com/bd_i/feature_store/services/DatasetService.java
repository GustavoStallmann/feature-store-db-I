package com.bd_i.feature_store.services;


import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateDatasetRequestDTO;
import com.bd_i.feature_store.dto.UpdateDatasetRequestDTO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatasetService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public void createDataset(CreateDatasetRequestDTO payload, String creatorCpf) throws SQLException {
        User creatorUser;
        try (UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy)) {
            creatorUser = userDAO.selectByCpf(creatorCpf);
        }

        Dataset dataset = new Dataset(UUID.randomUUID(), LocalDate.now(), payload.name(), creatorUser, LocalDate.now(), payload.description(), payload.origin());
        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy)) {
            datasetDAO.create(dataset);
        }
    }

    public List<Dataset> listDatasets() throws SQLException {
        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy)) {
            return datasetDAO.list();
        }
    }

    public Dataset getDataset(UUID id) throws SQLException {
        Dataset dataset;
        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy)) {
            dataset = datasetDAO.select(id);
        }

        if (dataset == null) {
            throw new ResourceNotFound("Dataset não encontrado");
        }

        return dataset;
    }

    public void updateDataset(UUID id, UpdateDatasetRequestDTO payload) throws SQLException {
        Dataset currentDataset = getDataset(id);

        Dataset updatedDataset = new Dataset(
                currentDataset.getId(),
                currentDataset.getCreatedAt(),
                payload.name(),
                currentDataset.getCreatorUser(),
                LocalDate.now(),
                payload.description(),
                payload.origin()
        );

        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy)) {
            datasetDAO.update(updatedDataset);
        }
    }

    public void deleteDataset(UUID id) throws SQLException {
        getDataset(id);

        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy)) {
            datasetDAO.delete(id);
        }
    }
}
