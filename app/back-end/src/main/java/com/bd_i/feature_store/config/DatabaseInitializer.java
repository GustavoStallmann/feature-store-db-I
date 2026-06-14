package com.bd_i.feature_store.config;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.UserDAO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.model.UserType;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(PgConnectionStrategy connectionStrategy, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                UserDAO userDAO = DaoFactory.getUserDAO(connectionStrategy);
                User admin = userDAO.selectByCpf("000.000.000-00");
                String defaultPasswordHash = passwordEncoder.encode("admin123");

                if (admin == null) {
                    System.out.println("Criando usuário admin padrão...");
                    User newAdmin = new User(
                            UUID.randomUUID(),
                            "000.000.000-00",
                            "admin",
                            UserType.admin,
                            defaultPasswordHash
                    );
                    userDAO.create(newAdmin);
                }
            } catch (Exception e) {
                System.err.println("Erro ao inicializar o usuário admin padrão: " + e.getMessage());
            }
        };
    }
}
