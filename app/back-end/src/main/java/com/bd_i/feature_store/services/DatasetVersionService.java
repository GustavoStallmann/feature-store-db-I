package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetDAO;
import com.bd_i.feature_store.dao.DatasetVersionDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateDatasetVersionRequestDTO;
import com.bd_i.feature_store.dto.UpdateDatasetVersionRequestDTO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatasetVersionService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetVersion> listDatasetVersions() throws SQLException {
        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);
        return datasetVersionDAO.list();
    }

    public List<DatasetVersion> listDatasetVersionsByDatasetId(UUID datasetId) throws SQLException {
        getDataset(datasetId);

        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);
        return datasetVersionDAO.selectByDatasetId(datasetId);
    }

    public DatasetVersion getDatasetVersion(UUID id) throws SQLException {
        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);
        DatasetVersion datasetVersion = datasetVersionDAO.select(id);

        if (datasetVersion == null) {
            throw new ResourceNotFound("Versão do dataset não encontrada");
        }

        return datasetVersion;
    }

    public void createDatasetVersion(CreateDatasetVersionRequestDTO payload) throws SQLException {
        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);

        DatasetVersion datasetVersion = new DatasetVersion(
                UUID.randomUUID(),
                payload.version(),
                payload.modifications(),
                LocalDateTime.now(),
                payload.filePath(),
                getUser(payload.submittingUserId()),
                getDataset(payload.datasetId()),
                getParentDatasetVersion(payload.parentDatasetVersionId())
        );

        datasetVersionDAO.create(datasetVersion);
    }

    public void updateDatasetVersion(UUID id, UpdateDatasetVersionRequestDTO payload) throws SQLException {
        DatasetVersion currentDatasetVersion = getDatasetVersion(id);
        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);

        DatasetVersion updatedDatasetVersion = new DatasetVersion(
                currentDatasetVersion.getId(),
                payload.version(),
                payload.modifications(),
                currentDatasetVersion.getCreatedAt(),
                payload.filePath(),
                getUser(payload.submittingUserId()),
                getDataset(payload.datasetId()),
                getParentDatasetVersion(payload.parentDatasetVersionId())
        );

        datasetVersionDAO.update(updatedDatasetVersion);
    }

    public void deleteDatasetVersion(UUID id) throws SQLException {
        getDatasetVersion(id);

        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);
        datasetVersionDAO.delete(id);
    }

    private User getUser(UUID id) throws SQLException {
        UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy);
        User user = userDAO.select(id);

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        return user;
    }

    private Dataset getDataset(UUID id) throws SQLException {
        DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(pgConnectionStrategy);
        Dataset dataset = datasetDAO.select(id);

        if (dataset == null) {
            throw new ResourceNotFound("Dataset não encontrado");
        }

        return dataset;
    }

    private DatasetVersion getParentDatasetVersion(UUID id) throws SQLException {
        if (id == null) {
            return null;
        }

        return getDatasetVersion(id);
    }
}
