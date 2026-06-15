import { client, IResponse } from "../client";
import { IDataset } from "../types";

export interface ICreateDatasetInput {
    name: string;
    creatorUserId: string;
    description?: string;
    origin?: string;
}

export interface IUpdateDatasetInput {
    name: string;
    description?: string;
    origin?: string;
}

export class DatasetModel {
    async listDatasets() {
        const { data } = await client.get<IResponse<IDataset[]>>('/api/dataset');
        return data;
    }

    async getDataset(id: string) {
        const { data } = await client.get<IResponse<IDataset>>(`/api/dataset/${id}`);
        return data;
    }

    async createDataset(payload: ICreateDatasetInput) {
        const { data } = await client.post<IResponse<null>>('/api/dataset', payload);
        return data;
    }

    async updateDataset(id: string, payload: IUpdateDatasetInput) {
        const { data } = await client.put<IResponse<null>>(`/api/dataset/${id}`, payload);
        return data;
    }

    async deleteDataset(id: string) {
        const { data } = await client.delete<IResponse<null>>(`/api/dataset/${id}`);
        return data;
    }
}

export const datasetModel = new DatasetModel();
