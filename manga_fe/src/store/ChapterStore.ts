import type ChapterData from "@/interface/Chapter";
import type PageData from "@/interface/Page";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";
import { useAuthStore } from "./AuthStore";
import type { User } from "@/class/User";

export const useChapterStore = defineStore('chapter', {
    state: () => ({
        chapter: {} as ChapterData,
        allChapter: [] as ChapterData[],
        user: useAuthStore().getUserAutenticado() as User
    }),

    actions: {
        async getChapterByID(id: string): Promise<ChapterData> {
            try {
                const response = await api.get(`/api/v1/chapter/read/${id}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                this.chapter = response.data;
                return this.chapter;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async getAllChapter(): Promise<ChapterData[]> {
            try {
                const response = await api.get("/api/v1/chapter/readAll", {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                this.allChapter = response.data;
                return this.allChapter;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async registerChapter(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/chapter/create", data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Chapter successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
        async editChapter(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/chapter/edit/${id}`, data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Successfully edited chapter!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async deleteChapterById(id: number): Promise<String> {
            try {
                const response = await api.delete(`/api/v1/chapter/delete/${id}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async getAllPages(): Promise<PageData[]> {
            try {
                const response = await api.get("/api/v1/chapter/getAll-pages", {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async deletePageById(id: number, idChapter: number): Promise<String> {
            try {
                const response = await api.delete(`/api/v1/chapter/delete/page/${id}/${idChapter}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async editPage(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/chapter/edit/page/${id}`, data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Successfully edited page!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async registerPage(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/chapter/register/page", data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Page successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
    }
})