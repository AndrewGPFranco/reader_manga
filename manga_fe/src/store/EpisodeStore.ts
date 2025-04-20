import { defineStore } from 'pinia'
import { useUser } from '@/composables/user'
import { api } from '@/network/axiosInstance'
import type { iEpisode } from '@/@types/iEpisode'

const { getToken } = useUser();

export const useEpisodeStore = defineStore("episode", {
  state: () => ({
    token: getToken()
  }),
  actions: {
    async uploadEpisode(data: FormData) {
      try {
        await api.post("/api/v1/episode/upload", data, {
          headers: {
            Authorization: `${this.token}`
          }
        })
      } catch(error) {
        throw new Error(String(error));
      }
    },

    async getEpisode(title: string, id: string) {
      const response = await api.get(`/api/v1/episode/${id}/${title}`, {
        responseType: "blob",
        headers: {
          Authorization: `${this.token}`
        }
      });

      return response.data;
    },

    async getAllEpisodesByAnime(idManga: string): Promise<Array<iEpisode>> {
      const response = await api.get(`/api/v1/episode/all/${idManga}`, {
        headers: {
          Authorization: `${this.token}`
        }
      });

      return response.data;
    }
  }
});