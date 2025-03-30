import type iMangaData from '@/@types/Manga'
import type iMangaDexData from '@/@types/MangaDex'
import type responseRequest from '@/@types/ResponseRequest'
import { api } from '@/network/axiosInstance'
import { defineStore } from 'pinia'
import { useAuthStore } from './AuthStore'
import type { User } from '@/class/User'
import type responseListManga from '@/@types/ResponseListManga'
import { jwtDecode } from 'jwt-decode'
import type iDecodedToken from '@/@types/iDecodedToken'
import type iCoversManga from '@/@types/iCoversManga'
import { UserData } from '@/class/UserData'

export const useMangaStore = defineStore('manga', {
  state: () => ({
    manga: [] as iMangaData[],
    user: useAuthStore().getUserAutenticado() as User
  }),

  actions: {
    async getAllManga(): Promise<iMangaData[]> {
      try {
        const response = await api.get(`/api/v1/manga/readAll/${this.user.getId()}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        this.manga = response.data
        return this.manga
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async getAllMangaPaginado(pageNumber: number, size: number): Promise<any> {
      try {
        const idUser = this.getIdUsuario()
        const response = await api.get(
          `/api/v1/manga/get-pageable?pageNumber=${pageNumber}&size=${size}&idUser=${idUser}`,
          {
            headers: {
              Authorization: `${this.user.getToken()}`
            }
          }
        )
        this.manga = response.data
        return this.manga
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async getMangaById(id: string): Promise<iMangaData> {
      try {
        const response = await api.get(`/api/v1/manga/read/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async getInfoManga(tituloManga: string): Promise<any> {
      const idUser = localStorage.getItem("id");
      const response = await api.get(`/api/v1/manga/get-info-manga/${tituloManga}/${idUser}`, {
        headers: {
          Authorization: `${this.user.getToken()}`
        }
      })
      return response.data
    },
    // Get 5 manga covers from the MangaDex API
    async getFiveMangaRandomMD(): Promise<iMangaDexData[]> {
      try {
        const token: string | null = localStorage.getItem('token');
        const response = await api.get('/api/v1/manga/get-covers', {
          headers: {
            Authorization: `${token}`
          }
        })
        return response.data;
      } catch (error: any) {
        throw new Error(error.response.data);
      }
    },
    // Get 5 manga covers from my library
    async getFiveMangaRandom(): Promise<iCoversManga[]> {
      try {
        const response = await api.get('/api/v1/manga/my-covers/30', {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async deleteMangaById(idManga: number): Promise<string> {
      try {
        const idUser = this.getIdUsuario() ?? '0'
        const dadosBack = new UserData(idUser, idManga, 0, 0)
        const response = await api.delete(`/api/v1/manga/delete`, {
          data: dadosBack,
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async registerManga(data: {}, callback: Function): Promise<string> {
      try {
        await api.post('/api/v1/manga/create', data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Mangá successfully registered!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while registering, check the data.'
      }
    },
    async editManga(id: number, data: {}, callback: Function): Promise<string> {
      try {
        await api.put(`/api/v1/manga/edit/${id}`, data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Successfully edited manga!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while editing, please check the data.'
      }
    },
    async setFavorite(idManga: number): Promise<responseRequest> {
      try {
        const response = await api.post(
          `/api/v1/user/favorite-manga/${this.user.getId()}/${idManga}`,
          null,
          {
            headers: {
              Authorization: `${this.user.getToken()}`
            }
          }
        )
        return { statusCode: response.status, message: response.data }
      } catch (error: any) {
        console.error(error)
        return {
          statusCode: error,
          message: 'An error occurred while editing, please check the data.'
        }
      }
    },
    async getAllFavorites() {
      const token = localStorage.getItem('token')
      if (token != undefined) {
        const id = this.getIdUsuario()
        const response = await api.get(`/api/v1/user/manga-favorite-list/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data.mangaList
      }
    },
    async getListMangaByUser(id: string): Promise<responseListManga> {
      try {
        const response = await api.get(`/api/v1/user/manga-list/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async adicionaMangaNaListaDoUsuario(idManga: number) {
      try {
        const idUser = localStorage.getItem("id");
        const response = await api.post(
          `/api/v1/user/add-manga?idManga=${idManga}&idUser=${idUser}`,
          {},
          {
            headers: {
              Authorization: this.user.getToken()
            }
          }
        )
        return response.data
      } catch (error: any) {
        throw new Error(error.response?.data || 'Erro ao adicionar manga')
      }
    },
    async removeDaLista(idManga: number) {
      try {
        const idUser = this.getIdUsuario()
        const response = await api.post(
          `/api/v1/user/remove-manga?idManga=${idManga}&idUser=${idUser}`,
          {},
          {
            headers: {
              Authorization: this.user.getToken()
            }
          }
        )
        return response.data
      } catch (error: any) {
        throw new Error(error.response?.data || 'Erro ao remover manga')
      }
    },
    getIdUsuario() {
      const token = localStorage.getItem('token')
      if (token != undefined) {
        const decode = jwtDecode<iDecodedToken>(token)
          return decode.id
      }
    },
    async getApenasNomeDosMangas(): Promise<string[]> {
      const result = await api.get("/api/v1/manga/nome-mangas", {
        headers: {
          Authorization: this.user.getToken()
        }
      });
      return result.data;
    },
    async getMangaPesquisado(mangaPesquisado: string): Promise<any> {
      const result = await api
        .get(`/api/v1/manga?pesquisado=${mangaPesquisado}`, {
        headers: {
          Authorization: this.user.getToken()
        }
      });
      return result.data;
    }
  }
})