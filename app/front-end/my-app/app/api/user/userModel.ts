import { client, IResponse } from "../client";
import { IUser } from "../types";

export interface ICreateUserInput {
    name: string;
    cpf: string;
    password: string;
}

export class UserModel {
    async getUsers() {
        const { data } = await client.get<IResponse<IUser[]>>('/api/users');
        return data;
    }

    async login(cpf: string, password: string) {
        const { data } = await client.get<IResponse<IUser>>('/api/users/logar', { params: { cpf, password } });
        return data;
    }

    async createUser(payload: ICreateUserInput) {
        const { data } = await client.post<IResponse<null>>('/api/users/create', payload);
        return data;
    }
}

export const userModel = new UserModel();
