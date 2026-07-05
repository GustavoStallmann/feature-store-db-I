package com.bd_i.feature_store.dto;

import java.util.UUID;

public record DatasetActivitySummaryDTO(UUID datasetId, String datasetName, long totalDownloads, long totalAccesses) {
}
