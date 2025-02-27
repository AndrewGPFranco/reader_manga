import { User } from '@/class/User'
import { api } from '@/network/axiosInstance'
import { defineStore } from 'pinia'
import { jwtDecode } from 'jwt-decode'
import type iDecodedToken from '@/interface/iDecodedToken'
import type { UserRegister } from '@/class/UserRegister'
import { UserSession } from '@/class/UserSession'

export const useAuthStore = defineStore('auth', {
  state: (): { user: User; usuarioLogado: UserSession | null } => ({
    user: new User('', '', '', ''),
    usuarioLogado: null as UserSession | null
  }),

  actions: {
    async efetuarLogin(email: string, password: string): Promise<void> {
      try {
        const user: User = new User(email, password);
        const result = await api.post('/api/v1/user/login', user);
        const decode: iDecodedToken = jwtDecode<iDecodedToken>(result.data.token);
        user.setToken(result.data.token);
        user.setId(decode.id);
        this.user = user;

        const token: string | undefined = user.getToken();
        const idUser: string | undefined = user.getId();
        if (token && idUser) {
          this._setTokenLocalStorage(token);
          this._setIdLocalStorage(idUser);
        }
      } catch (error: any) {
        throw new Error(error.response.data);
      }
    },
    getUserAutenticado() {
      const token = localStorage.getItem('token')
      const id = localStorage.getItem('id')
      if (token && id) {
        this.user.setToken(token)
        this.user.setId(id)
      }
      return this.user
    },
    _setTokenLocalStorage(token: string): void {
      localStorage.setItem('token', token)
    },
    _setIdLocalStorage(id: string): void {
      localStorage.setItem('id', id)
    },
    isUserAutenticado(): boolean {
      return this.user.getToken() !== '' || localStorage.getItem('token') !== null
    },
    async efetuarLogout(): Promise<void> {
      localStorage.removeItem('id')
      localStorage.removeItem('token')
      this.user = new User('', '', '', '');
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
        const email: string = tokenDecode.sub;
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
          userData.dateBirth
        )
        return this.usuarioLogado
      }
      throw new Error('Falha ao encontrar usuário')
    },
    getIdUsuario(): string | null {
      const token: string | null = localStorage.getItem('token');
      if (token != null) {
        const decode = jwtDecode<iDecodedToken>(token);
        return decode.id;
      }
      return token;
    },
    async changePassword(oldPassword: string, newPassword: string): Promise<Map<boolean, string>> {
      const token = localStorage.getItem('token')
      if (token != undefined) {
        try {
          const idUser = this.getIdUsuario()
          const data = { oldPassword, newPassword, idUser }
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
    }
  }
})
