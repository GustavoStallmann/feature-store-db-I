import { client, IResponse } from "../client";
import { IAuthSignInInput } from "../types";

export class AuthModel {
    async signIn(payload: IAuthSignInInput) {
        const { data } = await client.post<IResponse<null>>('/api/auth/login', payload); 
        return data; 
    }
}

export const authModel = new AuthModel(); 