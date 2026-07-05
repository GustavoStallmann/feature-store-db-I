package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.DatasetVersionFeature;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgDatasetVersionFeatureDAO extends DatasetVersionFeatureDAO {
    public PgDatasetVersionFeatureDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    protected DatasetVersionFeature modelMapper(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("nome");
        String description = resultSet.getString("descricao");
        UUID datasetVersionId = UUID.fromString(resultSet.getString("versao_dataset_id"));

        DatasetVersion datasetVersion;
        try (DatasetVersionDAO datasetVersionDAO = DaoFactory.getDatasetVersionDAO(this.getConnectionStrategy())) {
            datasetVersion = datasetVersionDAO.select(datasetVersionId);
        }

        return new DatasetVersionFeature(name, description, datasetVersion);
    }

    @Override
    public List<DatasetVersionFeature> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.feature_versao_dataset
        """;

        ArrayList<DatasetVersionFeature> datasetVersionFeatures = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetVersionFeature datasetVersionFeature = modelMapper(result);
                datasetVersionFeatures.add(datasetVersionFeature);
            }
        }

        return datasetVersionFeatures;
    }

    @Override
    public void create(DatasetVersionFeature model) throws SQLException {
        String query = """
            INSERT INTO feature_app.feature_versao_dataset (versao_dataset_id, nome, descricao)
            VALUES (?::uuid, ?, ?)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getDatasetVersion().getId());
            statement.setString(2, model.getName());
            statement.setString(3, model.getDescription());

            statement.executeUpdate();
        }
    }

    @Override
    public List<DatasetVersionFeature> selectByDatasetVersionId(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.feature_versao_dataset
            WHERE versao_dataset_id = ?::uuid
        """;

        ArrayList<DatasetVersionFeature> datasetVersionFeatures = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                DatasetVersionFeature datasetVersionFeature = modelMapper(result);
                datasetVersionFeatures.add(datasetVersionFeature);
            }
        }

        return datasetVersionFeatures;
    }

    @Override
    public void update(DatasetVersionFeature model) throws SQLException {
        throw new UnsupportedOperationException("Update operation is not available for DatasetVersionFeature.");
    }

    @Override
    public DatasetVersionFeature select(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Select operation is not available for DatasetVersionFeature.");
    }

    @Override
    public void delete(UUID id) throws SQLException {
        throw new UnsupportedOperationException("Delete operation is not available for DatasetVersionFeature");
    }
}
