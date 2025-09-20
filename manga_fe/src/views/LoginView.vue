<template>
  <div class="container-form">
    <form class="form">
      <p id="heading">Login</p>
      <div class="field">
        <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
          viewBox="0 0 16 16">
          <path
            d="M13.106 7.222c0-2.967-2.249-5.032-5.482-5.032-3.35 0-5.646 2.318-5.646 5.702 0 3.493 2.235 5.708 5.762 5.708.862 0 1.689-.123 2.304-.335v-.862c-.43.199-1.354.328-2.29.328-2.926 0-4.813-1.88-4.813-4.798 0-2.844 1.921-4.881 4.594-4.881 2.735 0 4.608 1.688 4.608 4.156 0 1.682-.554 2.769-1.416 2.769-.492 0-.772-.28-.772-.76V5.206H8.923v.834h-.11c-.266-.595-.881-.964-1.6-.964-1.4 0-2.378 1.162-2.378 2.823 0 1.737.957 2.906 2.379 2.906.8 0 1.415-.39 1.709-1.087h.11c.081.67.703 1.148 1.503 1.148 1.572 0 2.57-1.415 2.57-3.643zm-7.177.704c0-1.197.54-1.907 1.456-1.907.93 0 1.524.738 1.524 1.907S8.308 9.84 7.371 9.84c-.895 0-1.442-.725-1.442-1.914z">
          </path>
        </svg>
        <input v-model="username" autocomplete="off" placeholder="Email or Username" class="input-field" type="text" />
      </div>
      <div class="field">
        <svg class="input-icon" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
          viewBox="0 0 16 16">
          <path
            d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z">
          </path>
        </svg>
        <input v-model="password" placeholder="Password" class="input-field" type="password" />
      </div>
      <div class="btn">
        <button class="button1" @click="efetuarLogin">
          Login
        </button>
        <router-link :to="{ name: 'registerUser' }" class="button2">Sign Up</router-link>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import router from '@/router'
import { useAuthStore } from '@/store/AuthStore'
import { validationFieldsLogin } from '@/utils/validation'
import { useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'

const useAuth = useAuthStore()
const message = useMessage()

const username = ref<string>('')
const password = ref<string>('')

const efetuarLogin = async (e: MouseEvent) => {
  try {
    e.preventDefault()
    const validation = validationFieldsLogin({ username: username.value, password: password.value })
    if (validation === true) {
      await useAuth.efetuarLogin(username.value, password.value)

      limparCampos()
      await router.push({ name: 'home' })
    } else {
      message.error(validation as string)
    }
  } catch (error: any) {
    if (error.message === "Cannot read properties of undefined (reading 'data')")
      message.error('Não há conexão com o servidor no momento!')
    else message.error(error.message)
  }
}

const limparCampos = () => {
  username.value = ''
  password.value = ''
}

onMounted(() => document.title = 'Leitor de mangás - Login')
</script>

<style scoped>
.container-form {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #292929 0%, #1a1a1a 100%);
  padding: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 40px;
  background: #1a1a1a;
  border-radius: 25px;
  border: 1px solid #444;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.5);
  transition: transform 0.3s ease-in-out;
  max-width: 400px;
}

.form:hover {
  transform: scale(1.02);
}

#heading {
  text-align: center;
  margin: 0 0 20px;
  color: #00bfff;
  font-size: 32px;
  font-weight: 700;
  text-transform: uppercase;
  position: relative;
}

#heading::before {
  content: '';
  position: absolute;
  left: 50%;
  bottom: -10px;
  transform: translateX(-50%);
  width: 50px;
  height: 2px;
  background: #00bfff;
}

.field {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #2a2a2a;
  border-radius: 12px;
  padding: 10px 15px;
  border: 1px solid rgba(105, 105, 105, 0.5);
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.field:focus-within {
  border-color: #00bfff;
  box-shadow: 0 0 8px rgba(0, 191, 255, 0.3);
}

.input-icon {
  height: 1.5em;
  width: 1.5em;
  fill: #fff;
  opacity: 0.7;
  transition: opacity 0.3s ease;
}

.field:focus-within .input-icon {
  opacity: 1;
}

.input-field {
  background: none;

  border: none;
  outline: none;
  width: 100%;
  color: #fff;
  font-size: 16px;
  padding: 8px 0;
}

.input-field::placeholder {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9em;
}

.btn {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
}

.button1,
.button2 {
  padding: 12px 25px;
  border-radius: 12px;
  border: none;
  outline: none;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  text-decoration: none;
  color: #fff;
  transition: all 0.3s ease;
}

.button1 {
  background: linear-gradient(90deg, #00bfff 0%, #007acc 100%);
}

.button1:hover {
  background: linear-gradient(90deg, #00ccff 0%, #0088dd 100%);
  transform: translateY(-2px);
}

.button2 {
  background: #333;
}

.button2:hover {
  background: #444;
  transform: translateY(-2px);
}
@media (max-width: 480px) {
  .form {
    padding: 20px;
    max-width: 100%;
    margin: 0 15px;
  }

  .btn {
    flex-direction: column;
    gap: 10px;
  }

  .button1,
  .button2 {
    width: 100%;
  }
}
</style>