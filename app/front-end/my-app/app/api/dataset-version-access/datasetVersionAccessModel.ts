import { client, IResponse } from "../client";
import { IDatasetVersionAccess } from "../types";

export class DatasetVersionAccessModel {
    async listAccesses() {
        const { data } = await client.get<IResponse<IDatasetVersionAccess[]>>('/api/access');
        return data;
    }

    async listAccessesByUserId(userId: string) {
        const { data } = await client.get<IResponse<IDatasetVersionAccess[]>>(`/api/access/user/${userId}`);
        return data;
    }

    async listAccessesByDatasetVersionId(datasetVersionId: string) {
        const { data } = await client.get<IResponse<IDatasetVersionAccess[]>>(`/api/access/dataset-version/${datasetVersionId}`);
        return data;
    }
}

export const datasetVersionAccessModel = new DatasetVersionAccessModel();
