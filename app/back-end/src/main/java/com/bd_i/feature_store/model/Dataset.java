package com.bd_i.feature_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Dataset {
    private UUID id;
    private LocalDate createdAt;
    private String name;
    private User creatorUser;
    private LocalDate updatedAt;
    private String description;
    private String origin;
}
