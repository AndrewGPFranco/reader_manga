import {defineStore} from 'pinia'
import {useUser} from "@/composables/user";
import {api} from "@/network/axiosInstance";

const {getToken} = useUser()

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        token: getToken()
    }),
    actions: {
        async getNotifications(currentPage: number) {
            try {
                const response = await api.get(`/api/v1/user/get-notifications?paginaAtual=${currentPage}`, {
                    headers: {
                        Authorization: `${this.token}`
                    }
                })
                return response.data
            } catch (error) {
                throw new Error(String(error))
            }
        }
    }
})
