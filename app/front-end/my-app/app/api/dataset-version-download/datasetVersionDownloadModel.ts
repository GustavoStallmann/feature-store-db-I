import { client, IResponse } from "../client";
import { IDatasetVersionDownload } from "../types";

export class DatasetVersionDownloadModel {
    async listDownloads() {
        const { data } = await client.get<IResponse<IDatasetVersionDownload[]>>('/api');
        return data;
    }

    async listDownloadsByUserId(userId: string) {
        const { data } = await client.get<IResponse<IDatasetVersionDownload[]>>(`/api/user/${userId}`);
        return data;
    }

    async listDownloadsByDatasetVersionId(datasetVersionId: string) {
        const { data } = await client.get<IResponse<IDatasetVersionDownload[]>>(`/api/dataset-version/${datasetVersionId}`);
        return data;
    }

    async downloadDatasetVersion(datasetVersionId: string) {
        const response = await client.get<Blob>(`/api/dataset-version/${datasetVersionId}/download`, {
            responseType: 'blob',
        });
        return response.data;
    }
}

export const datasetVersionDownloadModel = new DatasetVersionDownloadModel();
