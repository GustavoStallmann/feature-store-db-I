package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final PgConnectionStrategy connectionStrategy;
    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserRequestDTO payload) throws SQLException {
        try {
            UserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
            String encodedPassword = passwordEncoder.encode(payload.password());
            System.out.println(payload);
            System.out.println(payload.password());
            User user = new User(
                    UUID.randomUUID(),
                    payload.cpf(),
                    payload.name(),
                    UserType.user,
                    encodedPassword
            );
            System.out.println(user.getPassword());
            userDAO.create(user);
        } catch (Exception e) {
            System.err.println("Falha ao criar usuário");
            throw e;
        }
    }

    public List<User> getUsers() throws SQLException {
        UserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
        return userDAO.list();
    }

    public User login(String cpf, String password) throws SQLException {
        UserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
        User user = userDAO.login(cpf, password);
        return user;
    }
}
