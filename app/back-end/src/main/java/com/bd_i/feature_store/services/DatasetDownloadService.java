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

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DatasetDownloadService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetVersionDownload> listDownloads() throws SQLException {
        try (DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy)) {
            return datasetDownloadDAO.list();
        }
    }

    public List<DatasetVersionDownload> listDownloadsByUserId(UUID userId) throws SQLException {
        getUser(userId);

        try (DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy)) {
            return datasetDownloadDAO.selectByUserId(userId);
        }
    }

    public List<DatasetVersionDownload> listDownloadsByDatasetVersionId(UUID datasetVersionId) throws SQLException {
        getDatasetVersion(datasetVersionId);

        try (DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy)) {
            return datasetDownloadDAO.selectByDatasetVersionId(datasetVersionId);
        }
    }

    public void createDownload(CreateDownloadRequestDTO payload) throws SQLException {
        DatasetVersionDownload datasetVersionDownload = new DatasetVersionDownload(
                getUser(payload.userId()),
                LocalDateTime.now(),
                getDatasetVersion(payload.datasetVersionId())
        );

        try (DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy)) {
            datasetDownloadDAO.create(datasetVersionDownload);
        }
    }

    public Resource downloadDatasetVersion(UUID datasetVersionId, String userCpf) throws SQLException {
        User user;
        try (UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy)) {
            user = userDAO.selectByCpf(userCpf);
        }

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        DatasetVersion datasetVersion = getDatasetVersion(datasetVersionId);

        DatasetVersionDownload datasetVersionDownload = new DatasetVersionDownload(
                user,
                LocalDateTime.now(),
                datasetVersion
        );

        try (DatasetDownloadDAO datasetDownloadDAO = DaoFactory.getDownloadDAO(pgConnectionStrategy)) {
            datasetDownloadDAO.create(datasetVersionDownload);
        }

        try {
            Path file = Paths.get(datasetVersion.getFilePath());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFound("Arquivo não encontrado");
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFound("Arquivo não encontrado: " + e.getMessage());
        }
    }

    private User getUser(UUID id) throws SQLException {
        User user;
        try (UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy)) {
            user = userDAO.select(id);
        }

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        return user;
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
