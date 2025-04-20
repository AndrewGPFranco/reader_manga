import { defineStore } from 'pinia'
import { useUser } from '@/composables/user'
import { api } from '@/network/axiosInstance'
import ResponseAPI from '@/class/api/ResponseAPI'
import type { iAnime } from '@/@types/iAnime'

const { getToken } = useUser();

export const useAnimeStore = defineStore("anime", {
  state: () => ({
    token: getToken()
  }),
  actions: {
    async registraAnime(title: string): Promise<ResponseAPI> {
      try {
        const data = {title: title};
        const response = await api.post("/api/v1/anime", data, {
          headers: {
            Authorization: `${this.token}`
          }
        })
        return new ResponseAPI(response.data.message, response.data.statusCode);
      } catch(error) {
        throw new Error(String(error));
      }
    },

    async findAll(): Promise<Array<iAnime>>{
      const response = await api.get("/api/v1/anime", {
        headers: {
          Authorization: `${this.token}`
        }
      })
      return response.data;
    }
  }
});