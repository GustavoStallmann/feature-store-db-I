package com.bd_i.feature_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class User {
    private UUID id;
    private String cpf;
    private String name;
    private UserType type;
}
