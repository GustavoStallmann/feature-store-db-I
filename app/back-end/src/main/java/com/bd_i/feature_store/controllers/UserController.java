package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import com.bd_i.feature_store.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.io.Console;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    ResponseEntity<ResponseDTO<List<User>>> getUsers() throws SQLException {
        List<User> users = userService.getUsers();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<User>>("Lista de usuários obtida com sucesso", users)
        );
    }

    @GetMapping("/logar")
    ResponseEntity<ResponseDTO<User>> login(@Valid @RequestParam @NotBlank String cpf, @Valid @RequestParam @NotBlank String password) throws SQLException {
        User user = userService.login(cpf, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<User>("Login realizado com sucesso", user)
        );
    }
    @PostMapping("/create")
    ResponseEntity<ResponseDTO<Void>> postUser(@Valid @RequestBody CreateUserRequestDTO body) throws SQLException {
        userService.createUser(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ResponseDTO<>("Usuário criado com sucesso", null)
        );
    }
}
