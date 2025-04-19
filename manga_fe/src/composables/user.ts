import { ref } from 'vue'

const tokenUser = ref<string | undefined>();

export function useUser() {
  const setToken = (token: string | undefined) => {
    if(token != undefined)
      tokenUser.value = token;
  }

  const resetToken = () => {
    tokenUser.value = undefined;
  }

  return { setToken, resetToken, getToken: () => tokenUser.value }
}