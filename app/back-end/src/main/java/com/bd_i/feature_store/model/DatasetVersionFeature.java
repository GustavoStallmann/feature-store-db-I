package com.bd_i.feature_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DatasetVersionFeature {
    private String name;
    private String description;
    private DatasetVersion datasetVersion;
}
