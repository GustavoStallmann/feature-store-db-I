package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDatasetVersionRequestDTO(
        @Min(value = 1, message = "Informe uma versão válida")
        int version,

        String modifications,

        @NotNull(message = "Informe o dataset")
        UUID datasetId,

        UUID parentDatasetVersionId
) {}
