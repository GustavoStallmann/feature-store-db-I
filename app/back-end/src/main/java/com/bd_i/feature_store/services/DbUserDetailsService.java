package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {
    private final PgConnectionStrategy pgConnectionStrategy;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        try {
            User user;
            try (UserDAO userDAO = DaoFactory.getUserDAO(pgConnectionStrategy)) {
                user = userDAO.selectByCpf(cpf);
            }
            if (user == null) {
                throw new UsernameNotFoundException("Credenciais inválidas");
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getCpf())
                    .password(user.getPassword() != null ? user.getPassword() : "")
                    .roles(user.getType().name())
                    .build();
        } catch (SQLException e) {
            throw new UsernameNotFoundException("Erro ao buscar usuário no banco de dados", e);
        }
    }
}
