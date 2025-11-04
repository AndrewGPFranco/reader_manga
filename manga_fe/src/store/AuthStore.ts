import {User} from '@/class/User'
import {defineStore} from 'pinia'
import {jwtDecode} from 'jwt-decode'
import {useUser} from '@/composables/user'
import {api} from '@/network/axiosInstance'
import {UserSession} from '@/class/UserSession'
import type {UserRegister} from '@/class/UserRegister'
import type iDecodedToken from '@/@types/iDecodedToken'
import type { iUsersManagament } from '@/@types/iUsersManagament'

const {setToken} = useUser()

export const useAuthStore = defineStore('auth', {
    state: (): { user: User; usuarioLogado: UserSession | null } => ({
        user: new User('', '', ''),
        usuarioLogado: null as UserSession | null
    }),

    actions: {
        async efetuarLogin(email: string, password: string): Promise<void> {
            try {
                const user = {
                    usernameOrEmail: email,
                    password: password
                }

                const result = await api.post('/api/v1/user/login', user)
                const decode: iDecodedToken = jwtDecode<iDecodedToken>(result.data.token)
                setToken(result.data.token)

                this.user = new User(decode.sub, result.data.token, decode.id)

                const token: string | undefined = this.user.getToken()

                if (token) this._setTokenLocalStorage(token)
            } catch (error: any) {
                throw new Error(error.response.data)
            }
        },
        getUserAutenticado() {
            if (this.user.getId() === '') {
                const token = localStorage.getItem('token')
                if (token) {
                    const decode: iDecodedToken = jwtDecode<iDecodedToken>(token)

                    this.user = new User(decode.sub, token, decode.id)
                }
            }

            return this.user
        },
        _setTokenLocalStorage(token: string): void {
            localStorage.setItem('token', token)
        },
        isUserAutenticado(): boolean {
            return this.user.getToken() !== '' || localStorage.getItem('token') !== null
        },
        async efetuarLogout(): Promise<void> {
            localStorage.removeItem('id')
            localStorage.removeItem('token')
            this.user = new User('', '', '')
            this.usuarioLogado = null
        },
        getRoleUser(): string {
            const token = localStorage.getItem('token')
            if (token != undefined) {
                const tokenDecode = jwtDecode<iDecodedToken>(token)
                return tokenDecode.role
            }
            return 'USER'
        },
        async register(user: UserRegister): Promise<string> {
            const result = await api.post('/api/v1/user/register', user)
            if (result.status === 200) return 'Usuário cadastrado com sucesso!'

            throw new Error('Falha ao cadastrar usuário')
        },
        async getUser() {
            if (this.usuarioLogado !== null) return this.usuarioLogado

            const token: string | null = localStorage.getItem('token')
            if (token != undefined) {
                const tokenDecode: iDecodedToken = jwtDecode<iDecodedToken>(token)
                const email: string = tokenDecode.sub
                const result = await api.get(`/api/v1/user?email=${email}`, {
                    headers: {
                        Authorization: `${token}`
                    }
                })
                const userData = result.data
                this.usuarioLogado = new UserSession(
                    userData.firstName,
                    userData.fullName,
                    userData.username,
                    userData.email,
                    userData.dateBirth,
                    userData.uriPath
                )
                return this.usuarioLogado
            }
            throw new Error('Falha ao encontrar usuário')
        },
        getIdUsuario(): string | null {
            const token: string | null = localStorage.getItem('token')
            if (token != null) {
                const decode = jwtDecode<iDecodedToken>(token)
                return decode.id
            }
            return token
        },
        async changePassword(oldPassword: string, newPassword: string): Promise<Map<boolean, string>> {
            const token = localStorage.getItem('token')
            if (token != undefined) {
                try {
                    const idUser = this.getIdUsuario()
                    const data = {oldPassword, newPassword, idUser}
                    const result = await api.post('/api/v1/user/change-password', data, {
                        headers: {
                            Authorization: `${token}`
                        }
                    })
                    return new Map([[true, result.data]])
                } catch (error: any) {
                    return new Map([[false, error.response.data]])
                }
            }
            return new Map([[false, 'Token inválido!']])
        },
        async changeProfilePhoto(dados: FormData): Promise<void> {
            const token = localStorage.getItem('token')
            if (token != undefined) {
                await api.post('/api/v1/user/change-photo', dados, {
                    headers: {
                        Authorization: `${token}`
                    }
                })
            }
        },
        async getUsuarios(pagina: number): Promise<any> {
            const token = localStorage.getItem('token')
            if (token != undefined) {
                const result = await api.get(`/api/v1/user/get-users?paginaAtual=${pagina}`, {
                    headers: {
                        Authorization: `${token}`
                    }
                });

                return result.data;
            }

            return []
        },
        async mudarRole(username: string, role: string): Promise<string> {
            const token = localStorage.getItem('token')
            if (token != undefined) {
                await api.put(`/api/v1/user/mudar-role?username=${username}&role=${role}`, null, {
                    headers: {
                        Authorization: `${token}`
                    }
                });

                return `Usuário ${username} virou ${role}`;
            }

            return "Ocorreu um erro ao realizar a mudança de role!";
        }
    }
})
