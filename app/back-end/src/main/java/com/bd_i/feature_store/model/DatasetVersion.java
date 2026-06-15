package com.bd_i.feature_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class DatasetVersion {
    private UUID id;
    private int version;
    private String modifications;
    private LocalDateTime createdAt;
    private String filePath;
    private User submittingUser;
    private Dataset dataset;
    private DatasetVersion parentDatasetVersion;

    boolean isParent() {
        return parentDatasetVersion == null;
    }
}
