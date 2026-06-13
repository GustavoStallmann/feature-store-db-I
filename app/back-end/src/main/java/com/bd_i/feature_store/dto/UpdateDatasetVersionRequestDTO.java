package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateDatasetVersionRequestDTO(
        @Min(value = 1, message = "Informe uma versão válida")
        int version,

        String modifications,

        @NotBlank(message = "Informe o caminho do arquivo")
        String filePath,

        @NotNull(message = "Informe o usuário submetente")
        UUID submittingUserId,

        @NotNull(message = "Informe o dataset")
        UUID datasetId,

        UUID parentDatasetVersionId
) {}
