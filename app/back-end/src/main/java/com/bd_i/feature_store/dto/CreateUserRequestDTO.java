package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank(message = "Informe um nome")
        String name,

        @NotBlank(message = "Informe o cpf")
        String cpf
) {}
