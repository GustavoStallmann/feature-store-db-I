package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.model.DatasetVersion;
import com.bd_i.feature_store.model.DatasetVersionDownload;
import com.bd_i.feature_store.services.DatasetDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DatasetDownloadController {
    private final DatasetDownloadService datasetDownloadService;

    @GetMapping()
    ResponseEntity<ResponseDTO<List<DatasetVersionDownload>>> listDownloads() throws SQLException {
        List<DatasetVersionDownload> datasetVersionDownloads = datasetDownloadService.listDownloads();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionDownload>>("Lista de downloads obtida com sucesso", datasetVersionDownloads)
        );
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<ResponseDTO<List<DatasetVersionDownload>>> listDownloadsByUserId(@PathVariable UUID userId) throws SQLException {
        List<DatasetVersionDownload> datasetVersionDownloads = datasetDownloadService.listDownloadsByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionDownload>>("Lista de downloads do usuário obtida com sucesso", datasetVersionDownloads)
        );
    }

    @GetMapping("/dataset-version/{datasetVersionId}")
    ResponseEntity<ResponseDTO<List<DatasetVersionDownload>>> listDownloadsByDatasetVersionId(
            @PathVariable UUID datasetVersionId
    ) throws SQLException {
        List<DatasetVersionDownload> datasetVersionDownloads = datasetDownloadService.listDownloadsByDatasetVersionId(datasetVersionId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<List<DatasetVersionDownload>>("Lista de downloads da versão do dataset obtida com sucesso", datasetVersionDownloads)
        );
    }

    @GetMapping("/dataset-version/{datasetVersionId}/download")
    public ResponseEntity<Resource> downloadDatasetVersion(
            @PathVariable UUID datasetVersionId,
            Principal principal
    ) throws SQLException {
        Resource resource = datasetDownloadService.downloadDatasetVersion(datasetVersionId, principal.getName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
