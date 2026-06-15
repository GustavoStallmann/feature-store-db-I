package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgDatasetVersionDAO extends DatasetVersionDAO {
    public PgDatasetVersionDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    protected DatasetVersion modelMapper(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        int version = resultSet.getInt("versao");
        String modifications = resultSet.getString("modificacoes");
        LocalDateTime createdAt = getLocalDateTime(resultSet, "criado_em");
        String filePath = resultSet.getString("caminho_arquivo");
        UUID submittingUserId = UUID.fromString(resultSet.getString("usuario_submetente"));
        UUID datasetId = UUID.fromString(resultSet.getString("dataset_id"));
        String parentDatasetVersionId = resultSet.getString("dataset_versao_pai");

        ConnectionStrategy connectionStrategy = this.getConnectionStrategy();
        User submittingUser;
        try (UserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy)) {
            submittingUser = userDAO.select(submittingUserId);
        }

        Dataset dataset;
        try (DatasetDAO datasetDAO = DaoFactory.getDatasetDAO(connectionStrategy)) {
            dataset = datasetDAO.select(datasetId);
        }

        DatasetVersion parentDatasetVersion = null;
        if (parentDatasetVersionId != null) {
            parentDatasetVersion = select(UUID.fromString(parentDatasetVersionId));
        }

        return new DatasetVersion(
            id,
            version,
            modifications,
            createdAt,
            filePath,
            submittingUser,
            dataset,
            parentDatasetVersion
        );
    }

    @Override
    public List<DatasetVersion> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.versao_dataset
        """;

        ArrayList<DatasetVersion> datasetVersions = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetVersion datasetVersion = modelMapper(result);
                datasetVersions.add(datasetVersion);
            }
        }

        return datasetVersions;
    }

    @Override
    public void create(DatasetVersion model) throws SQLException {
        String query = """
            INSERT INTO feature_app.versao_dataset (
                id,
                versao,
                modificacoes,
                criado_em,
                caminho_arquivo,
                usuario_submetente,
                dataset_id,
                dataset_versao_pai
            )
            VALUES (?::uuid, ?, ?, ?::timestamp, ?, ?::uuid, ?::uuid, ?::uuid)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getId());
            statement.setInt(2, model.getVersion());
            statement.setString(3, model.getModifications());
            statement.setTimestamp(4, getTimestamp(model.getCreatedAt()));
            statement.setString(5, model.getFilePath());
            statement.setObject(6, model.getSubmittingUser().getId());
            statement.setObject(7, model.getDataset().getId());
            statement.setObject(8, getParentDatasetVersionId(model));

            statement.executeUpdate();
        }
    }

    @Override
    public void update(DatasetVersion model) throws SQLException {
        String query = """
            UPDATE feature_app.versao_dataset
            SET versao = ?,
                modificacoes = ?,
                caminho_arquivo = ?,
                usuario_submetente = ?::uuid,
                dataset_id = ?::uuid,
                dataset_versao_pai = ?::uuid
            WHERE id = ?::uuid
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, model.getVersion());
            preparedStatement.setString(2, model.getModifications());
            preparedStatement.setString(3, model.getFilePath());
            preparedStatement.setObject(4, model.getSubmittingUser().getId());
            preparedStatement.setObject(5, model.getDataset().getId());
            preparedStatement.setObject(6, getParentDatasetVersionId(model));
            preparedStatement.setObject(7, model.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public DatasetVersion select(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.versao_dataset
            WHERE id = ?::uuid
            LIMIT 1
        """;

        Connection connection = getConnection();
        DatasetVersion datasetVersion = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                datasetVersion = modelMapper(resultSet);
            }
        }

        return datasetVersion;
    }

    @Override
    public void delete(UUID id) throws SQLException {
        String query = """
            DELETE FROM feature_app.versao_dataset
            WHERE id = ?::uuid
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<DatasetVersion> selectByDatasetId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.versao_dataset
            WHERE dataset_id = ?::uuid
        """;

        ArrayList<DatasetVersion> datasetVersions = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersion datasetVersion = modelMapper(result);
                datasetVersions.add(datasetVersion);
            }
        }

        return datasetVersions;
    }

    private LocalDateTime getLocalDateTime(ResultSet resultSet, String column) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(column);

        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }

    private Timestamp getTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return Timestamp.valueOf(localDateTime);
    }

    private UUID getParentDatasetVersionId(DatasetVersion model) {
        if (model.getParentDatasetVersion() == null) {
            return null;
        }

        return model.getParentDatasetVersion().getId();
    }

    @Override
    public DatasetVersion selectByVersionAndDatasetId(int version, UUID datasetId) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.versao_dataset
            WHERE versao = ? AND dataset_id = ?::uuid
            LIMIT 1
        """;

        Connection connection = getConnection();
        DatasetVersion datasetVersion = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, version);
            preparedStatement.setObject(2, datasetId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                datasetVersion = modelMapper(resultSet);
            }
        }

        return datasetVersion;
    }
}
