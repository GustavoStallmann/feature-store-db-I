package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetVersionDAO;
import com.bd_i.feature_store.dao.DatasetVersionAccessDAO;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.exception.ResourceNotFound;
import com.bd_i.feature_store.model.DatasetVersionAccess;
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
public class DatasetVersionAccessService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetVersionAccess> listAccesses() throws SQLException {
        DatasetVersionAccessDAO datasetVersionAccessDAO = DaoFactory.getDatasetVersionAccessDAO(pgConnectionStrategy);
        return datasetVersionAccessDAO.list();
    }

    public List<DatasetVersionAccess> listAccessesByUserId(UUID userId) throws SQLException {
        getUser(userId);

        DatasetVersionAccessDAO datasetVersionAccessDAO = DaoFactory.getDatasetVersionAccessDAO(pgConnectionStrategy);
        return datasetVersionAccessDAO.selectByUserId(userId);
    }

    public List<DatasetVersionAccess> listAccessesByDatasetVersionId(UUID datasetVersionId) throws SQLException {
        getDatasetVersion(datasetVersionId);

        DatasetVersionAccessDAO datasetVersionAccessDAO = DaoFactory.getDatasetVersionAccessDAO(pgConnectionStrategy);
        return datasetVersionAccessDAO.selectByDatasetId(datasetVersionId);
    }

    public void registerAccess(UUID versionId, String userCpf) throws SQLException {
        UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy);
        User user = userDAO.selectByCpf(userCpf);

        if (user == null) {
            throw new ResourceNotFound("Usuário não encontrado");
        }

        DatasetVersion datasetVersion = getDatasetVersion(versionId);

        DatasetVersionAccess access = new DatasetVersionAccess(user, LocalDateTime.now(), datasetVersion);
        DatasetVersionAccessDAO datasetVersionAccessDAO = DaoFactory.getDatasetVersionAccessDAO(pgConnectionStrategy);
        datasetVersionAccessDAO.create(access);
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
