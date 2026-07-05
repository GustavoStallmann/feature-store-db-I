package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.DatasetVersionFeature;
import com.bd_i.feature_store.services.DatasetVersionFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dataset-version-feature")
@RequiredArgsConstructor
public class DatasetVersionFeatureController {
    private final DatasetVersionFeatureService datasetVersionFeatureService;

    @GetMapping
    ResponseEntity<ResponseDTO<List<DatasetVersionFeature>>> listFeatures() throws SQLException {
        List<DatasetVersionFeature> datasetVersionFeatures = datasetVersionFeatureService.listFeatures();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionFeature>>("Lista de features obtida com sucesso", datasetVersionFeatures)
        );
    }

    @GetMapping("/dataset-version/{datasetVersionId}")
    ResponseEntity<ResponseDTO<List<DatasetVersionFeature>>> listFeaturesByDatasetVersionId(
            @PathVariable UUID datasetVersionId
    ) throws SQLException {
        List<DatasetVersionFeature> datasetVersionFeatures = datasetVersionFeatureService.listFeaturesByDatasetVersionId(datasetVersionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionFeature>>("Lista de features da versão do dataset obtida com sucesso", datasetVersionFeatures)
        );
    }
}
