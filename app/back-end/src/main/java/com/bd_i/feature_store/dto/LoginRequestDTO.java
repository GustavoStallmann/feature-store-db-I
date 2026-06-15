package com.bd_i.feature_store.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "Insira o CPF")
    String cpf,

    @NotBlank(message = "Insira uma senha")
    String password)
{}
