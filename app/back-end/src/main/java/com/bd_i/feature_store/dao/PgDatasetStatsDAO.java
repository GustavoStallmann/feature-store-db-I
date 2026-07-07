package com.bd_i.feature_store.dao;

import com.bd_i.feature_store.dto.DatasetActivityPointDTO;
import com.bd_i.feature_store.dto.DatasetActivitySummaryDTO;
import com.bd_i.feature_store.dto.HourlyActivityPointDTO;
import com.bd_i.feature_store.persistence.ConnectionStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PgDatasetStatsDAO extends DatasetStatsDAO {
    public PgDatasetStatsDAO(ConnectionStrategy connectionStrategy) {
        super(connectionStrategy);
    }

    @Override
    public List<DatasetActivityPointDTO> selectDailyActivity(int days) throws SQLException {
        String query = """
            SELECT 
                COALESCE(downloads.dia, acessos.dia) AS dia,
                COALESCE(downloads.total, 0) AS downloads,
                COALESCE(acessos.total, 0) AS acessos
            FROM (
                SELECT data_hora::date AS dia, COUNT(*) AS total
                FROM feature_app.download
                WHERE data_hora::date >= CURRENT_DATE - 1
                GROUP BY data_hora::date
            ) downloads
            FULL OUTER JOIN (
                SELECT data_hora::date AS dia, COUNT(*) AS total
                FROM feature_app.acesso
                WHERE data_hora::date >= CURRENT_DATE - 1
                GROUP BY data_hora::date
            ) acessos 
            ON downloads.dia = acessos.dia
            ORDER BY dia;
        """;

        ArrayList<DatasetActivityPointDTO> points = new ArrayList<>();
        Connection connection = getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    LocalDate day = result.getObject("dia", LocalDate.class);
                    long downloads = result.getLong("downloads");
                    long accesses = result.getLong("acessos");
                    points.add(new DatasetActivityPointDTO(day, downloads, accesses));
                }
            }
        }

        return points;
    }

    @Override
    public List<DatasetActivitySummaryDTO> selectDatasetActivitySummary() throws SQLException {
        String query = """
            SELECT ds.id, ds.nome,
                COALESCE(SUM(a.total), 0) as total_acessos,
                COALESCE(SUM(d.total), 0) as total_downloads
            FROM feature_app.versao_dataset dv
            LEFT JOIN feature_app.dataset ds ON ds.id = dv.dataset_id
            LEFT JOIN (
                SELECT dataset_versao_id, COUNT(*) AS total
                FROM feature_app.acesso
                GROUP BY dataset_versao_id
            ) a ON a.dataset_versao_id = dv.id
            LEFT JOIN (
                SELECT dataset_versao_id, COUNT(*) AS total
                FROM feature_app.download
                GROUP BY dataset_versao_id
            ) d ON d.dataset_versao_id = dv.id
            GROUP BY ds.id, ds.nome
            ORDER BY total_acessos DESC, total_downloads DESC;
        """;

        ArrayList<DatasetActivitySummaryDTO> summaries = new ArrayList<>();
        Connection connection = getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                UUID datasetId = UUID.fromString(result.getString("id"));
                String datasetName = result.getString("nome");
                long totalDownloads = result.getLong("total_downloads");
                long totalAccesses = result.getLong("total_acessos");
                summaries.add(new DatasetActivitySummaryDTO(datasetId, datasetName, totalDownloads, totalAccesses));
            }
        }

        return summaries;
    }

    @Override
    public List<HourlyActivityPointDTO> selectHourlyActivity() throws SQLException {
        String query = """
            WITH hours AS (
                SELECT generate_series(0, 23) AS hour
            ),
            downloads AS (
                SELECT EXTRACT(HOUR FROM data_hora)::int AS hour, COUNT(*) AS total
                FROM feature_app.download
                GROUP BY EXTRACT(HOUR FROM data_hora)
            ),
            accesses AS (
                SELECT EXTRACT(HOUR FROM data_hora)::int AS hour, COUNT(*) AS total
                FROM feature_app.acesso
                GROUP BY EXTRACT(HOUR FROM data_hora)
            )
            SELECT hours.hour,
                COALESCE(downloads.total, 0) AS downloads,
                COALESCE(accesses.total, 0) AS accesses
            FROM hours
            LEFT JOIN downloads ON downloads.hour = hours.hour
            LEFT JOIN accesses ON accesses.hour = hours.hour
            ORDER BY hours.hour
        """;

        ArrayList<HourlyActivityPointDTO> points = new ArrayList<>();
        Connection connection = getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                int hour = result.getInt("hour");
                long downloads = result.getLong("downloads");
                long accesses = result.getLong("accesses");
                points.add(new HourlyActivityPointDTO(hour, downloads, accesses));
            }
        }

        return points;
    }
}
