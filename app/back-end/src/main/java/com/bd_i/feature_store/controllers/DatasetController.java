package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.CreateDatasetRequestDTO;
import com.bd_i.feature_store.dto.CreateUserRequestDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.Dataset;
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import com.bd_i.feature_store.services.DatasetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    ResponseEntity<ResponseDTO<Void>> postDataset(@Valid @RequestBody CreateDatasetRequestDTO body, User currentUser) throws SQLException {
        datasetService.createDataset(body, currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO<>("Dataset criado com sucesso", null)
        );
    }
}
