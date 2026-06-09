package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import com.bd_i.feature_store.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final PgConnectionStrategy pgConnectionStrategy;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<ResponseDTO<List<User>>> getUsers() throws SQLException {
        List<User> users = userService.getUsers();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<User>>("Lista de usuários obtida com sucesso", users)
        );
    }

    @PostMapping
    ResponseEntity<ResponseDTO<Void>> postUser(@Valid @RequestBody CreateUserRequestDTO body) throws SQLException {
        userService.createUser(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ResponseDTO<>("Usuário criado com sucesso", null)
        );
    }
}
