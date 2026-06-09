package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.PgUserDAO;
import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PgConnectionStrategy connectionStrategy;

    public void createUser(CreateUserRequestDTO payload) throws SQLException {
        try {
            PgUserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
            User user = new User(UUID.randomUUID(), payload.cpf(), payload.name(), UserType.user);
            userDAO.create(user);
        } catch (Exception e) {
            System.err.println("Falha ao criar usuário");
            throw e;
        }
    }

    public List<User> getUsers() throws SQLException {
        PgUserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
        return userDAO.list();
    }
}
