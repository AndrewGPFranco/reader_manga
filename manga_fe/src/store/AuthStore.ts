import { User } from "@/class/User";
import { api } from "@/network/axiosInstance";
import { defineStore } from "pinia";

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: new User("", "", "")
    }),

    actions: {
        async efetuarLogin(email: string, password: string): Promise<boolean> {
            try {
                const user = new User(email, password);
                const result = await api.post('/api/v1/user/login', user);
                user.setToken(result.data.token);
                this.user = user;

                const token = user.getToken();
                if (token)
                    this._setTokenLocalStorage(token);

                return true;
            } catch(error: any) {
                throw new Error(error.response.data);
            }
        },
        getUserAutenticado() {
            const token = localStorage.getItem('token');
            if(token) {
                this.user.setToken(token);
            }
            return this.user;
        },
        _setTokenLocalStorage(token: string) {
            localStorage.setItem('token', token);
        },
        isUserAutenticado(): boolean {
            return this.user.getToken() !== "" || localStorage.getItem('token') !== null;
        }
    }
})