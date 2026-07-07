import { client, IResponse } from "../client";
import { IDatasetVersionFeature } from "../types";

export class DatasetVersionFeatureModel {
    async listDatasetVersionFeatures(datasetVersionId: string) {
        const { data } = await client.get<IResponse<IDatasetVersionFeature[]>>(`/api/dataset-version-feature/dataset-version/${datasetVersionId}`);
        return data;
    }
}

export const datasetVersionFeatureModel = new DatasetVersionFeatureModel();
