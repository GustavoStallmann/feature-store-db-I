package com.bd_i.feature_store.dto;

public record ResponseDTO<T>(String message, T data) {
}
