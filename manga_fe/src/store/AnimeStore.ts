import { defineStore } from 'pinia'
import { useUser } from '@/composables/user'
import { api } from '@/network/axiosInstance'

const { getToken } = useUser();

export const useAnimeStore = defineStore("anime", {
  state: () => ({
    token: getToken()
  }),
  actions: {
    async uploadEpisode(data: FormData) {
      try {
        await api.post("/api/v1/anime/upload", data, {
          headers: {
            Authorization: `${this.token}`
          }
        })
      } catch(error) {
        throw new Error(String(error));
      }
    },

    async getEpisode() {
      const response = await api.get("/api/v1/anime/episode/1/noragami", {
        responseType: "blob",
        headers: {
          Authorization: `${this.token}`
        }
      });

      return response.data;
    }
  }
});