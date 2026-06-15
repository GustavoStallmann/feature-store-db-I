package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.CreateDatasetRequestDTO;
<<<<<<< HEAD
import com.bd_i.feature_store.dto.CreateUserRequestDTO;
=======
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.dto.UpdateDatasetRequestDTO;
import com.bd_i.feature_store.model.Dataset;
<<<<<<< HEAD
import com.bd_i.feature_store.model.User;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
=======
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f
import com.bd_i.feature_store.services.DatasetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

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

<<<<<<< HEAD
    @PostMapping
    ResponseEntity<ResponseDTO<Void>> postDataset(@Valid @RequestBody CreateDatasetRequestDTO body, User currentUser) throws SQLException {
        datasetService.createDataset(body, currentUser);
=======
    @GetMapping("/{id}")
    ResponseEntity<ResponseDTO<Dataset>> getDataset(@PathVariable UUID id) throws SQLException {
        Dataset dataset = datasetService.getDataset(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<Dataset>("Dataset obtido com sucesso", dataset)
        );
    }

    @PostMapping
    ResponseEntity<ResponseDTO<Void>> postDataset(@Valid @RequestBody CreateDatasetRequestDTO body) throws SQLException {
        datasetService.createDataset(body);
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO<>("Dataset criado com sucesso", null)
        );
    }
<<<<<<< HEAD
=======

    @PutMapping("/{id}")
    ResponseEntity<ResponseDTO<Void>> putDataset(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDatasetRequestDTO body
    ) throws SQLException {
        datasetService.updateDataset(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Dataset atualizado com sucesso", null)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseDTO<Void>> deleteDataset(@PathVariable UUID id) throws SQLException {
        datasetService.deleteDataset(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Dataset removido com sucesso", null)
        );
    }
>>>>>>> a6f5d96e9a55e4a933ccb10dbb731569c502473f
}
