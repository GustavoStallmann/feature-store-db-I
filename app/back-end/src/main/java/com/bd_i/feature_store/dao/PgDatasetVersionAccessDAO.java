package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersionAccess;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgDatasetVersionAccessDAO extends DatasetVersionAccessDAO {
    public PgDatasetVersionAccessDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    protected DatasetVersionAccess modelMapper(ResultSet resultSet) throws SQLException {
        UUID userId = UUID.fromString(resultSet.getString("user_id"));
        LocalDateTime accessTime = resultSet.getObject("data_hora", LocalDateTime.class);
        UUID datasetVersionId = UUID.fromString(resultSet.getString("dataset_versao_id"));

        UserDAO userDAO = DaoFactory.getUserDAO(this.getConnectionStrategy());
        User user = userDAO.select(userId);

        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(this.getConnectionStrategy());
        DatasetVersion datasetVersion = datasetVersionDAO.select(datasetVersionId);

        return new DatasetVersionAccess(user, accessTime, datasetVersion);
    }

    @Override
    public List<DatasetVersionAccess> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.acesso
        """;

        ArrayList<DatasetVersionAccess> datasetVersionAccesses = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetVersionAccess datasetVersionAccess = modelMapper(result);
                datasetVersionAccesses.add(datasetVersionAccess);
            }
        }

        return datasetVersionAccesses;
    }

    @Override
    public void create(DatasetVersionAccess model) throws SQLException {
        String query = """
            INSERT INTO feature_app.acesso (user_id, data_hora, dataset_versao_id)
            VALUES (?::uuid, ?, ?::uuid)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getUser().getId());
            statement.setObject(2, model.getAccessTime());
            statement.setObject(3, model.getDatasetVersion().getId());

            statement.executeUpdate();
        }
    }


    @Override
    public List<DatasetVersionAccess> selectByUserId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.acesso
            WHERE user_id = ?::uuid
        """;

        ArrayList<DatasetVersionAccess> datasetVersionAccesses = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersionAccess datasetVersionAccess = modelMapper(result);
                datasetVersionAccesses.add(datasetVersionAccess);
            }
        }

        return datasetVersionAccesses;
    }

    @Override
    public List<DatasetVersionAccess> selectByDatasetId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.acesso
            WHERE dataset_versao_id = ?::uuid
        """;

        ArrayList<DatasetVersionAccess> datasetVersionAccesses = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersionAccess datasetVersionAccess = modelMapper(result);
                datasetVersionAccesses.add(datasetVersionAccess);
            }
        }

        return datasetVersionAccesses;
    }

    @Override
    public void update(DatasetVersionAccess model) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not available for DatasetVersionAccess.");
    }

    @Override
    public DatasetVersionAccess select(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Select operation is not available for DatasetVersionAccess.");
    }

    @Override
    public void delete(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not available for DatasetVersionAccess");
    }
}
