package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import com.bd_i.feature_store.services.DatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DatasetController {
    private final DatasetService datasetService;

    @GetMapping()
    ResponseEntity<ResponseDTO<List<Dataset>>> listDatasets() throws SQLException {
        List<Dataset> datasets = datasetService.listDatasets();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<Dataset>>("Lista de datasets obtida com sucesso", datasets)
        );
    }
}
