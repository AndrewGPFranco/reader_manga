import type MangaData from "@/interface/Manga";
import type MangaDexData from "@/interface/MangaDex";
import { api } from "@/network/axiosInstance";
import type { MessageApiInjection } from "naive-ui/es/message/src/MessageProvider";
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
        async getFiveMangaRandom(): Promise<MangaDexData[]> {
            try {
                const response = await api.get("/api/v1/manga/get-covers")
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
        }        
    },
})