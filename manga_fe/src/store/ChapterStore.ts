import type iChapterData from '@/@types/iChapter'
import { api } from '@/network/axiosInstance'
import { defineStore } from 'pinia'
import { useAuthStore } from './AuthStore'
import type { User } from '@/class/User'

export const useChapterStore = defineStore('chapter', {
  state: () => ({
    chapter: {} as iChapterData[],
    allChapter: [] as iChapterData[],
    user: useAuthStore().getUserAutenticado() as User,
    sizePaginaCapitulo: null
  }),

  actions: {
    async getChapterByID(id: string): Promise<iChapterData[]> {
      try {
        const response = await api.get(`/api/v1/chapter/read/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        this.chapter = response.data
        return this.chapter
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async getAllChapter(pageNumber: number, size: number): Promise<any> {
      try {
        const response = await api.get(`/api/v1/chapter/readAll?size=${size}&pageNumber=${pageNumber}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        this.allChapter = response.data
        return this.allChapter
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async registerChapter(data: {}, callback: Function): Promise<string> {
      try {
        await api.post('/api/v1/chapter/create', data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Chapter successfully registered!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while registering, check the data.'
      }
    },
    async editChapter(id: number, data: {}, callback: Function): Promise<string> {
      try {
        await api.put(`/api/v1/chapter/edit/${id}`, data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Successfully edited chapter!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while editing, please check the data.'
      }
    },
    async deleteChapterById(id: number): Promise<string> {
      try {
        const response = await api.delete(`/api/v1/chapter/delete/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async getAllPages(pageNumber: number, size: number): Promise<any> {
      try {
        const response = await api.get(`/api/v1/chapter/getAll-pages?size=${size}&pageNumber=${pageNumber}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async deletePageById(id: number): Promise<string> {
      try {
        const response = await api.delete(`/api/v1/chapter/delete/page/${id}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data
      } catch (error: any) {
        throw new Error(error.response.data)
      }
    },
    async editPage(id: number, data: {}, callback: Function): Promise<string> {
      try {
        await api.put(`/api/v1/chapter/edit/page/${id}`, data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Successfully edited page!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while editing, please check the data.'
      }
    },
    async registerPage(data: {}, callback: Function): Promise<string> {
      try {
        await api.post('/api/v1/chapter/register/page', data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        callback()
        return 'Page successfully registered!'
      } catch (error: any) {
        console.error(error)
        return 'An error occurred while registering, check the data.'
      }
    },
    async getPaginaDoCapitulo(idCapitulo: string, index: number): Promise<Blob | MediaSource> {
      const response = await api.get(`/api/v1/chapter/image/${idCapitulo}/${index}`, {
        responseType: 'blob',
        headers: {
          Authorization: `${this.user.getToken()}`
        }
      })

      return response.data
    },
    async getQuantidadePaginasDoCapitulo(idCapitulo: string): Promise<number> {
      const response = await this.getChapterByID(idCapitulo);

      return response.length
    },
    getQuantidade(id: string) {
      if (this.sizePaginaCapitulo != null) {
        return this.sizePaginaCapitulo
      }
      return this.getQuantidadePaginasDoCapitulo(id);
    },
    async updateReadingProgress(idChapter: string, currentProgress: number) {
      try {
        const data = {
          idChapter: idChapter,
          readingProgress: currentProgress
        };

        await api.put(`/api/v1/chapter/update-progress`, data, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        });
      } catch (error) {
        console.error(error);
      }
    },
    async getReadingProgress(idChapter: string) {
      try {
        const response = await api.get(`/api/v1/chapter/reading-progress/${idChapter}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async getAllReadingProgress(pageNumber: number) {
      try {
        const response = await api.get(`/api/v1/chapter/reading-progress?pageNumber=${pageNumber}`, {
          headers: {
            Authorization: `${this.user.getToken()}`
          }
        })
        return response.data;
      } catch (error) {
        console.error(error);
      }
    }
  }
})
