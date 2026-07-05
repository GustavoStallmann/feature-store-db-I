import { client, IResponse } from "../client";
import { IDatasetVersion } from "../types";

export interface IDatasetVersionFeatureInput {
    name: string;
    description?: string;
}

export interface ICreateDatasetVersionInput {
    file: File;
    version: number;
    datasetId: string;
    modifications?: string;
    parentDatasetVersionId?: string;
    features: IDatasetVersionFeatureInput[];
}

export interface IUpdateDatasetVersionInput {
    version: number;
    modifications?: string;
    filePath: string;
    submittingUserId: string;
    datasetId: string;
    parentDatasetVersionId?: string;
}

export class DatasetVersionModel {
    async listDatasetVersions() {
        const { data } = await client.get<IResponse<IDatasetVersion[]>>('/api/dataset-version');
        return data;
    }

    async getDatasetVersion(id: string) {
        const { data } = await client.get<IResponse<IDatasetVersion>>(`/api/dataset-version/${id}`);
        return data;
    }

    async listByDatasetId(datasetId: string) {
        const { data } = await client.get<IResponse<IDatasetVersion[]>>(`/api/dataset-version/dataset/${datasetId}`);
        return data;
    }

    async createDatasetVersion(payload: ICreateDatasetVersionInput) {
        const form = new FormData();
        form.append('file', payload.file);
        form.append('version', String(payload.version));
        form.append('datasetId', payload.datasetId);
        if (payload.modifications) form.append('modifications', payload.modifications);
        if (payload.parentDatasetVersionId) form.append('parentDatasetVersionId', payload.parentDatasetVersionId);
        form.append('features', JSON.stringify(payload.features));

        const { data } = await client.post<IResponse<null>>('/api/dataset-version', form, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return data;
    }

    async updateDatasetVersion(id: string, payload: IUpdateDatasetVersionInput) {
        const { data } = await client.put<IResponse<null>>(`/api/dataset-version/${id}`, payload);
        return data;
    }

    async deleteDatasetVersion(id: string) {
        const { data } = await client.delete<IResponse<null>>(`/api/dataset-version/${id}`);
        return data;
    }
}

export const datasetVersionModel = new DatasetVersionModel();
