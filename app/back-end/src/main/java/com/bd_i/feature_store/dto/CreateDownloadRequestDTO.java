package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDownloadRequestDTO(
        @NotNull(message = "Informe o usuário")
        UUID userId,

        @NotNull(message = "Informe a versão do dataset")
        UUID datasetVersionId
) {}
