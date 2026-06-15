package com.bd_i.feature_store.services;


import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateDatasetRequestDTO;
<<<<<<< HEAD
import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
=======
import com.bd_i.feature_store.dto.UpdateDatasetRequestDTO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.User;
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f
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

    public void createDataset(CreateDatasetRequestDTO payload, User currentUser) throws SQLException {
        try {
            DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
            Dataset dataset = new Dataset(UUID.randomUUID(), LocalDate.now(), payload.name(), currentUser, LocalDate.now(), payload.description(), payload.origin());
            datasetDAO.create(dataset);
        } catch (Exception e) {
            System.err.println("Falha ao criar dataset");
            throw e;
        }
    }
    public List<Dataset> listDatasets() throws SQLException {
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        return datasetDAO.list();
    }

    public Dataset getDataset(UUID id) throws SQLException {
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        Dataset dataset = datasetDAO.select(id);

        if (dataset == null) {
            throw new ResourceNotFound("Dataset não encontrado");
        }

        return dataset;
    }

    public void createDataset(CreateDatasetRequestDTO payload) throws SQLException {
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy);
        User creatorUser = userDAO.select(payload.creatorUserId());

        Dataset dataset = new Dataset(
                UUID.randomUUID(),
                LocalDate.now(),
                payload.name(),
                creatorUser,
                LocalDate.now(),
                payload.description(),
                payload.origin()
        );

        datasetDAO.create(dataset);
    }

    public void updateDataset(UUID id, UpdateDatasetRequestDTO payload) throws SQLException {
        Dataset currentDataset = getDataset(id);
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);

        Dataset updatedDataset = new Dataset(
                currentDataset.getId(),
                currentDataset.getCreatedAt(),
                payload.name(),
                currentDataset.getCreatorUser(),
                LocalDate.now(),
                payload.description(),
                payload.origin()
        );

        datasetDAO.update(updatedDataset);
    }

    public void deleteDataset(UUID id) throws SQLException {
        getDataset(id);

        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        datasetDAO.delete(id);
    }
}
