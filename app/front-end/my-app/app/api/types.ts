export type UserType = 'admin' | 'user';

export interface IUser {
    id: string;
    cpf: string;
    name: string;
    type: UserType;
}

export interface IDataset {
    id: string;
    createdAt: string;
    name: string;
    creatorUser: IUser;
    updatedAt: string;
    description: string;
    origin: string;
}

export interface IDatasetVersion {
    id: string;
    version: number;
    modifications: string;
    createdAt: string;
    filePath: string;
    submittingUser: IUser;
    dataset: IDataset;
    parentDatasetVersion: IDatasetVersion | null;
}

export interface IDatasetVersionFeature {
    name: string;
    description: string | null;
    datasetVersion: IDatasetVersion;
}

export interface IDatasetVersionAccess {
    user: IUser;
    accessTime: string;
    datasetVersion: IDatasetVersion;
}

export interface IDatasetVersionDownload {
    user: IUser;
    downloadTime: string;
    datasetVersion: IDatasetVersion;
}

export interface IAuthSignInInput {
    cpf: string;
    password: string;
}

export interface IDatasetActivityPoint {
    day: string;
    downloads: number;
    accesses: number;
}

export interface IDatasetActivitySummary {
    datasetId: string;
    datasetName: string;
    totalDownloads: number;
    totalAccesses: number;
}

export interface IHourlyActivityPoint {
    hour: number;
    downloads: number;
    accesses: number;
}