package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDatasetRequestDTO(
        @NotBlank(message = "Informe um nome")
        String name,

        @NotNull(message = "Informe o usuário criador")
        UUID creatorUserId,

        String description,

        String origin
) {}
