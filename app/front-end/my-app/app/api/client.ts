import axios from "axios";

export const client = axios.create({
    baseURL: process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080",
    withCredentials: true,
});

export interface IResponse<T> {
    message: string; 
    data: T
};