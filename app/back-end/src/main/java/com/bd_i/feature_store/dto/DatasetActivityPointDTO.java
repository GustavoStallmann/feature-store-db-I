package com.bd_i.feature_store.dto;

import java.time.LocalDate;

public record DatasetActivityPointDTO(LocalDate day, long downloads, long accesses) {
}
