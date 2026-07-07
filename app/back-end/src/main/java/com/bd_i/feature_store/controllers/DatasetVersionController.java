package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.CreateDatasetVersionRequestDTO;
import com.bd_i.feature_store.dto.DatasetVersionFeatureInputDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.dto.UpdateDatasetVersionRequestDTO;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.services.DatasetVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dataset-version")
@RequiredArgsConstructor
public class DatasetVersionController {
    private final DatasetVersionService datasetVersionService;
    private final ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<ResponseDTO<List<DatasetVersion>>> listDatasetVersions() throws SQLException {
        List<DatasetVersion> datasetVersions = datasetVersionService.listDatasetVersions();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersion>>("Lista de versões de datasets obtida com sucesso", datasetVersions)
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseDTO<DatasetVersion>> getDatasetVersion(@PathVariable UUID id) throws SQLException {
        DatasetVersion datasetVersion = datasetVersionService.getDatasetVersion(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<DatasetVersion>("Versão do dataset obtida com sucesso", datasetVersion)
        );
    }

    @GetMapping("/dataset/{datasetId}")
    ResponseEntity<ResponseDTO<List<DatasetVersion>>> listDatasetVersionsByDatasetId(
            @PathVariable UUID datasetId
    ) throws SQLException {
        List<DatasetVersion> datasetVersions = datasetVersionService.listDatasetVersionsByDatasetId(datasetId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersion>>("Lista de versões do dataset obtida com sucesso", datasetVersions)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ResponseDTO<Void>> postDatasetVersion(
            @RequestParam("file") MultipartFile file,
            @RequestParam("version") int version,
            @RequestParam(value = "modifications", required = false) String modifications,
            @RequestParam("datasetId") UUID datasetId,
            @RequestParam(value = "parentDatasetVersionId", required = false) UUID parentDatasetVersionId,
            @RequestParam("features") String featuresJson,
            Principal principal
    ) throws SQLException, IOException {
        if (version < 1) {
            throw new IllegalArgumentException("Informe uma versão válida");
        }

        List<DatasetVersionFeatureInputDTO> features;
        try {
            features = objectMapper.readValue(featuresJson, new TypeReference<List<DatasetVersionFeatureInputDTO>>() {});
        } catch (JacksonException e) {
            throw new IllegalArgumentException("Features enviadas em formato inválido");
        }

        CreateDatasetVersionRequestDTO body = new CreateDatasetVersionRequestDTO(
                version,
                modifications,
                datasetId,
                parentDatasetVersionId
        );
        datasetVersionService.createDatasetVersion(body, file, features, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDTO<>("Versão do dataset criada com sucesso", null)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseDTO<Void>> putDatasetVersion(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDatasetVersionRequestDTO body
    ) throws SQLException {
        datasetVersionService.updateDatasetVersion(id, body);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Versão do dataset atualizada com sucesso", null)
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseDTO<Void>> deleteDatasetVersion(@PathVariable UUID id) throws SQLException {
        datasetVersionService.deleteDatasetVersion(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Versão do dataset removida com sucesso", null)
        );
    }
}
