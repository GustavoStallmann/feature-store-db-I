package com.bd_i.feature_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DatasetVersionDownload {
    private User user;
    private LocalDateTime downloadTime;
    private DatasetVersion datasetVersion;
}
