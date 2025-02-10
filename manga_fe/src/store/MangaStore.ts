import type MangaData from "@/interface/Manga";
import type MangaDexData from "@/interface/MangaDex";
import type ResponseRequest from "@/interface/ResponseRequest";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";
import { useAuthStore } from "./AuthStore";
import type { User } from "@/class/User";
import type ResponseListManga from "@/interface/ResponseListManga";
import { jwtDecode } from "jwt-decode";
import type DecodedToken from "@/interface/iDecodedToken";
import type iCoversManga from "@/interface/iCoversManga";
import { UserData } from "@/class/UserData";

export const useMangaStore = defineStore('manga', {
    state: () => ({
        manga: [] as MangaData[],
        user: useAuthStore().getUserAutenticado() as User
    }),

    actions: {
        async getAllManga(): Promise<MangaData[]> {
            try {
                const response = await api.get(`/api/v1/manga/readAll/${this.user.getId()}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                this.manga = response.data;
                return this.manga;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async getMangaById(id: string): Promise<MangaData> {
            try {
                const response = await api.get(`/api/v1/manga/read/${id}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        // Get 5 manga covers from the MangaDex API
        async getFiveMangaRandomMD(): Promise<MangaDexData[]> {
            try {
                const token = localStorage.getItem('token');
                const response = await api.get("/api/v1/manga/get-covers", {
                    headers: {
                        Authorization: `${token}`
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        // Get 5 manga covers from my library
        async getFiveMangaRandom(): Promise<iCoversManga[]> {
            try {
                const response = await api.get("/api/v1/manga/my-covers/30", {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async deleteMangaById(idManga: number): Promise<string> {
            try {
                const idUser = this.getIdUsuario() ?? '0';
                const dadosBack = new UserData(idUser, idManga, 0, 0);
                const response = await api.delete(`/api/v1/manga/delete`, {
                    data: dadosBack,
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        async registerManga(data: {}, callback: Function): Promise<string> {
            try {
                await api.post("/api/v1/manga/create", data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Mangá successfully registered!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while registering, check the data.";
            }
        },
        async editManga(id: number, data: {}, callback: Function): Promise<string> {
            try {
                await api.put(`/api/v1/manga/edit/${id}`, data, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                callback();
                return "Successfully edited manga!";
            } catch (error: any) {
                console.error(error);
                return "An error occurred while editing, please check the data.";
            }
        },
        async setFavorite(idManga: number): Promise<ResponseRequest> {
            try {
                const response = await api.post(`/api/v1/user/favorite-manga/${this.user.getId()}/${idManga}`, null, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return { statusCode: response.status, message: response.data };
            } catch (error: any) {
                console.error(error);
                return { statusCode: error, message: "An error occurred while editing, please check the data." };
            }
        },
        async getAllFavorites() {
            const token = localStorage.getItem('token');
            if(token != undefined) {
                const id = this.getIdUsuario();
                const response = await api.get(`/api/v1/user/manga-favorite-list/${id}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data.mangaList;
            }
            
        },
        async getListMangaByUser(id: string): Promise<ResponseListManga> {
            try {
                const response = await api.get(`/api/v1/user/manga-list/${id}`, {
                    headers: {
                        Authorization: `${this.user.getToken()}`
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response.data);
            }
        },
        async adicionaMangaNaListaDoUsuario(idManga: number) {
            try {
                const idUser = this.user.getId();
                const response = await api.post(`/api/v1/user/add-manga?idManga=${idManga}&idUser=${idUser}`, {}, {
                    headers: {
                        Authorization: this.user.getToken()
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response?.data || 'Erro ao adicionar manga');
            }
        },
        async removeDaLista(idManga: number) {
            try {
                const idUser = this.getIdUsuario();
                const response = await api.post(`/api/v1/user/remove-manga?idManga=${idManga}&idUser=${idUser}`, {}, {
                    headers: {
                        Authorization: this.user.getToken()
                    }
                });
                return response.data;
            } catch (error: any) {
                throw new Error(error.response?.data || 'Erro ao remover manga');
            }
        },
        getIdUsuario() {
            const token = localStorage.getItem('token');
            if(token != undefined) {
                const decode = jwtDecode<DecodedToken>(token);
                const id = decode.id;
                return id;
            }
        }
    },
})