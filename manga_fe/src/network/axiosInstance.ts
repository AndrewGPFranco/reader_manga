import axios, { type AxiosInstance } from 'axios'

export const api: AxiosInstance = axios.create({
    // baseURL: "http://localhost:8080"
    baseURL: "https://6367-2804-7f0-b240-b768-307f-125f-4d76-27ad.ngrok-free.app"
});