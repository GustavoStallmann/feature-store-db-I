package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgUserDAO extends UserDAO {
    public PgUserDAO(ConnectionStrategy connectionStrategy) throws SQLException {
        super(connectionStrategy);
    }

    @Override
    protected User modelMapper(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String cpf = resultSet.getString("cpf");
        String name = resultSet.getString("nome");
        String type = resultSet.getString("tipo");
        String password = resultSet.getString("senha");

        return new User(UUID.fromString(id), cpf, name, UserType.valueOf(type), password);
    }

    @Override
    public List<User> list() throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.usuario
        """;

        ArrayList<User> users = new ArrayList<>();
        Connection connection = this.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                User user = modelMapper(result);
                users.add(user);
            }
        }

        return users;
    }

    @Override
    public void create(User model) throws SQLException {
        String query = """
            INSERT INTO feature_app.usuario (id, cpf, nome, tipo, senha)
            VALUES (?::uuid, ?, ?, ?::tipo_usuario, ?)
        """;

        Connection connection = getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, model.getId());
            statement.setString(2, model.getCpf());
            statement.setString(3, model.getName());
            statement.setObject(4, model.getType().name());
            statement.setString(5, model.getPassword() != null ? model.getPassword() : "");

            statement.executeUpdate();
        }
    }

    @Override
    public User selectByCpf(String cpf) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.usuario
            WHERE cpf = ?
            LIMIT 1
        """;

        Connection connection = getConnection();
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cpf);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = modelMapper(resultSet);
            }
        }

        return user;
    }

    @Override
    public void update(User model) throws SQLException {
        String query = """
            UPDATE feature_app.usuario
            SET nome = ?, tipo = ?::tipo_usuario, senha = ?
            WHERE id = ?
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, model.getName());
            preparedStatement.setObject(2, model.getType().name());
            preparedStatement.setString(3, model.getPassword() != null ? model.getPassword() : "");
            preparedStatement.setObject(4, model.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public User select(UUID id) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.usuario
            WHERE id = ?::uuid
            LIMIT 1
        """;

        Connection connection = getConnection();
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = modelMapper(resultSet);
            }
        }

        return user;
    }

    @Override
    public void delete(UUID id) throws SQLException {
        String query = """
            DELETE FROM feature_app.usuario
            WHERE id = ?:uuid
        """;

        Connection connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public User login(String cpf, String password) throws SQLException {
        String query = """
            SELECT *
            FROM feature_app.usuario
            WHERE cpf=? AND senha = ?
            LIMIT 1
        """;

        Connection connection = getConnection();
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cpf);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = modelMapper(resultSet);
            }
        }

        return user;
    }

}
