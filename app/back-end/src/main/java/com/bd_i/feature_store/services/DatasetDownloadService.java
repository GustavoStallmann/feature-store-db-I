package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetVersionDAO;
import com.bd_i.feature_store.dao.DatasetDownloadDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateDownloadRequestDTO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.DatasetVersionDownload;
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
public class DatasetDownloadService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetVersionDownload> listDownloads() throws SQLException {
        DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return datasetDownloadDAO.list();
    }

    public List<DatasetVersionDownload> listDownloadsByUserId(UUID userId) throws SQLException {
        getUser(userId);

        DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return datasetDownloadDAO.selectByUserId(userId);
    }

    public List<DatasetVersionDownload> listDownloadsByDatasetVersionId(UUID datasetVersionId) throws SQLException {
        getDatasetVersion(datasetVersionId);

        DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return datasetDownloadDAO.selectByDatasetVersionId(datasetVersionId);
    }

    public void createDownload(CreateDownloadRequestDTO payload) throws SQLException {
        DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);

        DatasetVersionDownload datasetVersionDownload = new DatasetVersionDownload(
                getUser(payload.userId()),
                LocalDateTime.now(),
                getDatasetVersion(payload.datasetVersionId())
        );

        datasetDownloadDAO.create(datasetVersionDownload);
    }

    private User getUser(UUID id) throws SQLException {
        UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy);
        User user = userDAO.select(id);

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        return user;
    }

    private DatasetVersion getDatasetVersion(UUID id) throws SQLException {
        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(pgConnectionStrategy);
        DatasetVersion datasetVersion = datasetVersionDAO.select(id);

        if (datasetVersion == null) {
            throw new ResourceNotFound("Versão do dataset não encontrada");
        }

        return datasetVersion;
    }
}
