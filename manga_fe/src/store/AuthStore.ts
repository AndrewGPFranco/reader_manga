import { User } from "@/class/User";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: new User("", "", "")
    }),

    actions: {
        async efetuarLogin(email: string, password: string) {
            try {
                const user = new User(email, password);
                const result = await api.post('/api/v1/user/login', user);
                user.setToken(result.data.token);
                user.setId(result.data.id);
                this.user = user;

                const token = user.getToken();
                const idUser = user.getId();
                if (token && idUser) {
                    this._setTokenLocalStorage(token);
                    this._setIdLocalStorage(idUser);
                }
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        getUserAutenticado() {
            const token = localStorage.getItem('token');
            const id = localStorage.getItem('id');
            if(token && id) {
                this.user.setToken(token);
                this.user.setId(id);
            }
            return this.user;
        },
        _setTokenLocalStorage(token: string) {
            localStorage.setItem('token', token);
        },
        _setIdLocalStorage(id: string) {
            localStorage.setItem('id', id);
        },
        isUserAutenticado(): boolean {
            return this.user.getToken() !== "" || localStorage.getItem('token') !== null;
        },
        async efetuarLogout() {
            localStorage.removeItem('id');
            localStorage.removeItem('token');
            this.user = new User("", "", "");
        }
    }
})