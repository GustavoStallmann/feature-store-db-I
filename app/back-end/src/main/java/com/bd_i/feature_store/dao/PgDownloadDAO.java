package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetDownload;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgDownloadDAO extends DownloadDAO {
    public PgDownloadDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    protected DatasetDownload modelMapper(ResultSet resultSet) throws SQLException {
        UUID userId = UUID.fromString(resultSet.getString("user_id"));
        LocalDateTime downloadTime = resultSet.getObject("data_hora", LocalDateTime.class);
        UUID datasetVersionId = UUID.fromString(resultSet.getString("dataset_versao_id"));

        UserDAO userDAO = DaoFactory.getUserDAO(this.getConnectionStrategy());
        User user = userDAO.select(userId);

        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(this.getConnectionStrategy());
        DatasetVersion datasetVersion = datasetVersionDAO.select(datasetVersionId);

        return new DatasetDownload(user, downloadTime, datasetVersion);
    }

    @Override
    public List<DatasetDownload> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.datasetDownload
        """;

        ArrayList<DatasetDownload> datasetDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetDownload datasetDownload = modelMapper(result);
                datasetDownloads.add(datasetDownload);
            }
        }

        return datasetDownloads;
    }

    @Override
    public void create(DatasetDownload model) throws SQLException {
        String query = """
            INSERT INTO feature_app.datasetDownload (user_id, data_hora, dataset_versao_id)
            VALUES (?::uuid, ?::timestamp, ?::uuid)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getUser().getId());
            statement.setObject(2, model.getDownloadTime());
            statement.setObject(3, model.getDatasetVersion().getId());

            statement.executeUpdate();
        }
    }

    @Override
    public List<DatasetDownload> selectByUserId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.datasetDownload
            WHERE user_id = ?::uuid
        """;

        ArrayList<DatasetDownload> datasetDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetDownload datasetDownload = modelMapper(result);
                datasetDownloads.add(datasetDownload);
            }
        }

        return datasetDownloads;
    }

    @Override
    public List<DatasetDownload> selectByDatasetVersionId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.datasetDownload
            WHERE dataset_versao_id = ?::uuid
        """;

        ArrayList<DatasetDownload> datasetDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetDownload datasetDownload = modelMapper(result);
                datasetDownloads.add(datasetDownload);
            }
        }

        return datasetDownloads;
    }

    @Override
    public void update(DatasetDownload model) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not available for Download.");
    }

    @Override
    public DatasetDownload select(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Select operation is not available for Download.");
    }

    @Override
    public void delete(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not available for Download.");
    }
}
