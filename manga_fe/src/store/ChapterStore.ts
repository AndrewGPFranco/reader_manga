import type ChapterData from "@/interface/Chapter";
import type PageData from "@/interface/Page";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";

export const useChapterStore = defineStore('chapter', {
    state: () => ({
        chapter: {} as ChapterData,
        allChapter: [] as ChapterData[]
    }),

    actions: {
        async getChapterByID(id: string): Promise<ChapterData> {
            try {
                const response = await api.get(`/api/v1/chapter/read/${id}`);
                this.chapter = response.data;
                return this.chapter;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async getAllChapter(): Promise<ChapterData[]> {
            try {
                const response = await api.get("/api/v1/chapter/readAll");
                this.allChapter = response.data;
                return this.allChapter;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async registerChapter(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/chapter/create", data);
                callback();
                return "Chapter successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
        async editChapter(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/chapter/edit/${id}`, data);
                callback();
                return "Successfully edited chapter!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async deleteChapterById(id: number): Promise<String> {
            try {
                const response = await api.delete(`/api/v1/chapter/delete/${id}`);
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async getAllPages(): Promise<PageData[]> {
            try {
                const response = await api.get("/api/v1/chapter/getAll-pages");
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async deletePageById(id: number, idChapter: number): Promise<String> {
            try {
                const response = await api.delete(`/api/v1/chapter/delete/page/${id}/${idChapter}`);
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async editPage(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/chapter/edit/page/${id}`, data);
                callback();
                return "Successfully edited page!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async registerPage(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/chapter/register/page", data);
                callback();
                return "Page successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
    }
})