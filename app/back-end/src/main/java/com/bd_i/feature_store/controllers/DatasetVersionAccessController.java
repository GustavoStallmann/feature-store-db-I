package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.DatasetVersionAccess;
import com.bd_i.feature_store.services.DatasetVersionAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/api/dataset-version-access", "/api/access"})
@RequiredArgsConstructor
public class DatasetVersionAccessController {
    private final DatasetVersionAccessService datasetVersionAccessService;

    @GetMapping
    ResponseEntity<ResponseDTO<List<DatasetVersionAccess>>> listAccesses() throws SQLException {
        List<DatasetVersionAccess> datasetVersionAccesses = datasetVersionAccessService.listAccesses();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionAccess>>("Lista de acessos obtida com sucesso", datasetVersionAccesses)
        );
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<ResponseDTO<List<DatasetVersionAccess>>> listAccessesByUserId(@PathVariable UUID userId) throws SQLException {
        List<DatasetVersionAccess> datasetVersionAccesses = datasetVersionAccessService.listAccessesByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionAccess>>("Lista de acessos do usuário obtida com sucesso", datasetVersionAccesses)
        );
    }

    @GetMapping("/dataset-version/{datasetVersionId}")
    ResponseEntity<ResponseDTO<List<DatasetVersionAccess>>> listAccessesByDatasetVersionId(
            @PathVariable UUID datasetVersionId
    ) throws SQLException {
        List<DatasetVersionAccess> datasetVersionAccesses = datasetVersionAccessService.listAccessesByDatasetVersionId(datasetVersionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionAccess>>("Lista de acessos da versão do dataset obtida com sucesso", datasetVersionAccesses)
        );
    }
}
