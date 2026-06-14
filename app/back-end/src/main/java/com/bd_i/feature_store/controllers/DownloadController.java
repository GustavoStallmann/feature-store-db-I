package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.DatasetDownload;
import com.bd_i.feature_store.services.DatasetDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/download")
@RequiredArgsConstructor
public class DownloadController {
    private final DatasetDownloadService datasetDownloadService;

    @GetMapping
    ResponseEntity<ResponseDTO<List<DatasetDownload>>> listDownloads() throws SQLException {
        List<DatasetDownload> datasetDownloads = datasetDownloadService.listDownloads();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetDownload>>("Lista de downloads obtida com sucesso", datasetDownloads)
        );
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<ResponseDTO<List<DatasetDownload>>> listDownloadsByUserId(@PathVariable UUID userId) throws SQLException {
        List<DatasetDownload> datasetDownloads = datasetDownloadService.listDownloadsByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetDownload>>("Lista de downloads do usuário obtida com sucesso", datasetDownloads)
        );
    }

    @GetMapping("/dataset-version/{datasetVersionId}")
    ResponseEntity<ResponseDTO<List<DatasetDownload>>> listDownloadsByDatasetVersionId(
            @PathVariable UUID datasetVersionId
    ) throws SQLException {
        List<DatasetDownload> datasetDownloads = datasetDownloadService.listDownloadsByDatasetVersionId(datasetVersionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetDownload>>("Lista de downloads da versão do dataset obtida com sucesso", datasetDownloads)
        );
    }
}
