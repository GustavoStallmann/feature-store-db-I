package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
import com.bd_i.feature_store.persistence.ConnectionStrategy;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

public class PgDatasetDAO extends DatasetDAO {
    public PgDatasetDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        super(connectionStrategy);
    }

    @Override
    protected Dataset modelMapper(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String createdAt = resultSet.getString("criado_em");
        String name = resultSet.getString("nome");
        String creator = resultSet.getString("usuario_criador");
        String updatedAt = resultSet.getString("atualizado_em");
        String description = resultSet.getString("descricao");
        String origin = resultSet.getString("origem");

        // ERRO
        //PgUserDAO userDAO = DaoFactory.getUserDAO(this.connectionStrategy);
        //User user = userDAO.select(UUID.fromString(creator));
        User user = new User(UUID.fromString(creator), "cpf", "nome", UserType.user);
        return new Dataset(UUID.fromString(id), LocalDate.parse(createdAt), name, user, LocalDate.parse(updatedAt), description, origin);
    }

    @Override
    public List<Dataset> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.dataset
        """;

        ArrayList<Dataset> datasets = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Dataset dataset = modelMapper(result);
                datasets.add(dataset);
            }
        }

        return datasets;
    }

    @Override
    public void create(Dataset model) throws SQLException {
        String query = """
            INSERT INTO feature_app.dataset (id, criado_em, nome, usuario_criador, atualizado_em, descricao, origem)
            VALUES (?::uuid, ?::timestamp, ?::uuid, ?::timestamp, ?, ?)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getId());
            statement.setObject(2, model.getCreatedAt());
            statement.setString(3, model.getName());
            statement.setObject(4, model.getCreatorUser());
            statement.setObject(5, model.getUpdatedAt());
            statement.setString(6, model.getDescription());
            statement.setString(7, model.getOrigin());

            statement.executeUpdate();
        }
    }

    @Override
    public void update(Dataset model) throws SQLException {
        String query = """
            UPDATE feature_app.dataset
            SET nome = ?, descricao = ?
            WHERE id = ?
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, model.getName());
            preparedStatement.setObject(2, model.getDescription());
            preparedStatement.setObject(3, model.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Dataset select(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.dataset
            WHERE id = ?::uuid
            LIMIT 1
        """;

        Connection connection = getConnection();
        Dataset dataset = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            dataset = modelMapper(resultSet);
        }

        return dataset;
    }

    @Override
    public void delete(UUID id) throws SQLException {
        String query = """
            DELETE FROM feature_app.dataset
            WHERE id = ?:uuid
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
