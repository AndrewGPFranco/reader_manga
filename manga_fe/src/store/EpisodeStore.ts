import { defineStore } from 'pinia'
import { useUser } from '@/composables/user'
import { api } from '@/network/axiosInstance'

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

    async getEpisode() {
      const response = await api.get("/api/v1/episode/1/Noragami", {
        responseType: "blob",
        headers: {
          Authorization: `${this.token}`
        }
      });

      return response.data;
    }
  }
});