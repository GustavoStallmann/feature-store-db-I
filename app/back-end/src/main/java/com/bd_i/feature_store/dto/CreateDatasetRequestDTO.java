package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDatasetRequestDTO(
        @NotBlank(message = "Informe um nome")
        String name,

        String description,

        String origin
) {}
