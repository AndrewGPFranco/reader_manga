import { defineStore } from 'pinia'
import { api } from '@/network/axiosInstance'
import { useUser } from '@/composables/user'

const { getToken } = useUser();

export const useAnimeStore = defineStore("anime", {
  state: () => ({
    token: getToken()
  }),
  actions: {
    async getEpisode() {
      // TODO: ajustar para ser dinâmico
      const data = {
        file: null,
        id: 2,
        title: "naruto"
      }
      const response = await api.post("/api/v1/anime/video", data, {
        responseType: "blob",
        headers: {
          Authorization: `${this.token}`
        }
      });

      return response.data;
    }
  }
});