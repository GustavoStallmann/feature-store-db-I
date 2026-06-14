package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetVersionDAO;
import com.bd_i.feature_store.dao.DownloadDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateDownloadRequestDTO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.DatasetDownload;
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

    public List<DatasetDownload> listDownloads() throws SQLException {
        DownloadDAO downloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return downloadDAO.list();
    }

    public List<DatasetDownload> listDownloadsByUserId(UUID userId) throws SQLException {
        getUser(userId);

        DownloadDAO downloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return downloadDAO.selectByUserId(userId);
    }

    public List<DatasetDownload> listDownloadsByDatasetVersionId(UUID datasetVersionId) throws SQLException {
        getDatasetVersion(datasetVersionId);

        DownloadDAO downloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);
        return downloadDAO.selectByDatasetVersionId(datasetVersionId);
    }

    public void createDownload(CreateDownloadRequestDTO payload) throws SQLException {
        DownloadDAO downloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy);

        DatasetDownload datasetDownload = new DatasetDownload(
                getUser(payload.userId()),
                LocalDateTime.now(),
                getDatasetVersion(payload.datasetVersionId())
        );

        downloadDAO.create(datasetDownload);
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
