import type MangaData from "@/interface/Manga";
import type MangaDexData from "@/interface/MangaDex";
import type ResponseRequest from "@/interface/ResponseRequest";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";

export const useMangaStore = defineStore('manga', {
    state: () => ({
        manga: [] as MangaData[]
    }),

    actions: {
        async getAllManga(): Promise<MangaData[]> {
            try {
                const response = await api.get("/api/v1/manga/readAll");
                this.manga = response.data;
                return this.manga;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async getMangaById(id: string): Promise<MangaData> {
            try {
                const response = await api.get(`/api/v1/manga/read/${id}`);
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        // Get 5 manga covers from the MangaDex API
        async getFiveMangaRandomMD(): Promise<MangaDexData[]> {
            try {
                const response = await api.get("/api/v1/manga/get-covers")
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        // Get 5 manga covers from my library
        async getFiveMangaRandom(): Promise<string[]> {
            try {
                const response = await api.get("/api/v1/manga/my-covers/30")
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async deleteMangaById(id: number): Promise<String> {
            try {
                const response = await api.delete(`/api/v1/manga/delete/${id}`);
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async registerManga(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/manga/create", data);
                callback();
                return "Mangá successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
        async editManga(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/manga/edit/${id}`, data);
                callback();
                return "Successfully edited manga!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async setFavorite(isFavorite: boolean, id: number): Promise<ResponseRequest> {
            try {
                const data = { isFavorite: isFavorite }
                const response = await api.post(`/api/v1/manga/favorite/${id}`, data);
                return { statusCode: response.status, message: response.data };
            } catch (error: any) {
                console.error(error);
                return { statusCode: error, message: "An error occurred while editing, please check the data." };
            }
        },
        async getAllFavorites(): Promise<MangaData[]> {
            const response = await api.get("/api/v1/manga/favorites");
            return response.data;
        }          
    },
})