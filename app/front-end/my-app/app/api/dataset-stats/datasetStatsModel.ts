import { client, IResponse } from "../client";
import { IDatasetActivityPoint, IDatasetActivitySummary, IHourlyActivityPoint } from "../types";

export class DatasetStatsModel {
    async getDailyActivity(days: number = 30) {
        const { data } = await client.get<IResponse<IDatasetActivityPoint[]>>(
            "/api/dataset/stats/activity/daily",
            { params: { days } }
        );
        return data;
    }

    async getActivitySummary() {
        const { data } = await client.get<IResponse<IDatasetActivitySummary[]>>("/api/dataset/stats/summary");
        return data;
    }

    async getHourlyActivity() {
        const { data } = await client.get<IResponse<IHourlyActivityPoint[]>>("/api/dataset/stats/activity/hourly");
        return data;
    }
}

export const datasetStatsModel = new DatasetStatsModel();
