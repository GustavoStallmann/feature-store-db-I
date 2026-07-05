package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;

public record DatasetVersionFeatureInputDTO(
        @NotBlank(message = "Informe o nome da feature")
        String name,

        String description
) {}
