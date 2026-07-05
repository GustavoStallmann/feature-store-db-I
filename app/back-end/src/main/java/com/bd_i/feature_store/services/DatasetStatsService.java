package com.bd_i.feature_store.services;

import com.bd_i.feature_store.dao.DaoFactory;
import com.bd_i.feature_store.dao.DatasetStatsDAO;
import com.bd_i.feature_store.dto.DatasetActivityPointDTO;
import com.bd_i.feature_store.dto.DatasetActivitySummaryDTO;
import com.bd_i.feature_store.dto.HourlyActivityPointDTO;
import com.bd_i.feature_store.persistence.PgConnectionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetStatsService {
    private final PgConnectionStrategy pgConnectionStrategy;

    public List<DatasetActivityPointDTO> getDailyActivity(int days) throws SQLException {
        try (DatasetStatsDAO datasetStatsDAO = DaoFactory.getDatasetStatsDAO(pgConnectionStrategy)) {
            return datasetStatsDAO.selectDailyActivity(days);
        }
    }

    public List<DatasetActivitySummaryDTO> getDatasetActivitySummary() throws SQLException {
        try (DatasetStatsDAO datasetStatsDAO = DaoFactory.getDatasetStatsDAO(pgConnectionStrategy)) {
            return datasetStatsDAO.selectDatasetActivitySummary();
        }
    }

    public List<HourlyActivityPointDTO> getHourlyActivity() throws SQLException {
        try (DatasetStatsDAO datasetStatsDAO = DaoFactory.getDatasetStatsDAO(pgConnectionStrategy)) {
            return datasetStatsDAO.selectHourlyActivity();
        }
    }
}
