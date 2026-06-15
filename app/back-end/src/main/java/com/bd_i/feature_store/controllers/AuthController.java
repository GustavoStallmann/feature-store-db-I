package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.LoginRequestDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<ResponseDTO<Void>> login(
            @Valid @RequestBody LoginRequestDTO body,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.authenticate(body, request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Login realizado com sucesso", null)
        );
    }
}
