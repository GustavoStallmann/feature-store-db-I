package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f

public record CreateDatasetRequestDTO(
        @NotBlank(message = "Informe um nome")
        String name,

<<<<<<< HEAD
        @NotBlank(message = "Informe a descrição")
        String description,

        @NotBlank(message = "Informe a origem")
=======
        @NotNull(message = "Informe o usuário criador")
        UUID creatorUserId,

        String description,

>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f
        String origin
) {}
