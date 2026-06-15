package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersionDownload;
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

public class PgDatasetDownloadDAO extends DatasetDownloadDAO {
    public PgDatasetDownloadDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    protected DatasetVersionDownload modelMapper(ResultSet resultSet) throws SQLException {
        UUID userId = UUID.fromString(resultSet.getString("user_id"));
        LocalDateTime downloadTime = resultSet.getObject("data_hora", LocalDateTime.class);
        UUID datasetVersionId = UUID.fromString(resultSet.getString("dataset_versao_id"));

        UserDAO userDAO = DaoFactory.getUserDAO(this.getConnectionStrategy());
        User user = userDAO.select(userId);

        DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(this.getConnectionStrategy());
        DatasetVersion datasetVersion = datasetVersionDAO.select(datasetVersionId);

        return new DatasetVersionDownload(user, downloadTime, datasetVersion);
    }

    @Override
    public List<DatasetVersionDownload> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.download
        """;

        ArrayList<DatasetVersionDownload> datasetVersionDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetVersionDownload datasetVersionDownload = modelMapper(result);
                datasetVersionDownloads.add(datasetVersionDownload);
            }
        }

        return datasetVersionDownloads;
    }

    @Override
    public void create(DatasetVersionDownload model) throws SQLException {
        String query = """
            INSERT INTO feature_app.download (user_id, data_hora, dataset_versao_id)
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
    public List<DatasetVersionDownload> selectByUserId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.download
            WHERE user_id = ?::uuid
        """;

        ArrayList<DatasetVersionDownload> datasetVersionDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersionDownload datasetVersionDownload = modelMapper(result);
                datasetVersionDownloads.add(datasetVersionDownload);
            }
        }

        return datasetVersionDownloads;
    }

    @Override
    public List<DatasetVersionDownload> selectByDatasetVersionId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.download
            WHERE dataset_versao_id = ?::uuid
        """;

        ArrayList<DatasetVersionDownload> datasetVersionDownloads = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersionDownload datasetVersionDownload = modelMapper(result);
                datasetVersionDownloads.add(datasetVersionDownload);
            }
        }

        return datasetVersionDownloads;
    }

    @Override
    public void update(DatasetVersionDownload model) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not available for Download.");
    }

    @Override
    public DatasetVersionDownload select(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Select operation is not available for Download.");
    }

    @Override
    public void delete(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not available for Download.");
    }
}
