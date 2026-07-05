package com.bd_i.feature_store.controllers;

import com.bd_i.feature_store.dto.DatasetActivityPointDTO;
import com.bd_i.feature_store.dto.DatasetActivitySummaryDTO;
import com.bd_i.feature_store.dto.HourlyActivityPointDTO;
import com.bd_i.feature_store.dto.ResponseDTO;
import com.bd_i.feature_store.services.DatasetStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/dataset/stats")
@RequiredArgsConstructor
public class DatasetStatsController {
    private final DatasetStatsService datasetStatsService;

    @GetMapping("/activity/daily")
    ResponseEntity<ResponseDTO<List<DatasetActivityPointDTO>>> getDailyActivity(
            @RequestParam(defaultValue = "30") int days
    ) throws SQLException {
        List<DatasetActivityPointDTO> points = datasetStatsService.getDailyActivity(days);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Atividade diária obtida com sucesso", points)
        );
    }

    @GetMapping("/summary")
    ResponseEntity<ResponseDTO<List<DatasetActivitySummaryDTO>>> getDatasetActivitySummary() throws SQLException {
        List<DatasetActivitySummaryDTO> summary = datasetStatsService.getDatasetActivitySummary();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Resumo de atividade por dataset obtido com sucesso", summary)
        );
    }

    @GetMapping("/activity/hourly")
    ResponseEntity<ResponseDTO<List<HourlyActivityPointDTO>>> getHourlyActivity() throws SQLException {
        List<HourlyActivityPointDTO> points = datasetStatsService.getHourlyActivity();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>("Distribuição horária de atividade obtida com sucesso", points)
        );
    }
}
