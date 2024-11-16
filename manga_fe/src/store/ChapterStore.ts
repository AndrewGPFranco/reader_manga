import type ChapterData from "@/interface/Chapter";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";

export const useChapterStore = defineStore('chapter', {
    state: () => ({
        chapter: {} as ChapterData
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
        }
    },
})