<template>
  <div class="container-form">
    <form class="form">
      <p class="title">Registrar-se</p>
      <p class="message">Registre-se para ter acesso a plataforma!</p>
      <div class="flex">
        <label>
          <input class="input" v-model="nome" type="text" required />
          <span>Nome</span>
        </label>

        <label>
          <input class="input" v-model="apelido" type="text" required />
          <span>Apelido</span>
        </label>
      </div>

      <label>
        <input class="input" v-model="nomeCompleto" type="text" required />
        <span>Nome completo</span>
      </label>

      <label>
        <input
          class="input"
          v-maska
          data-maska="##/##/####"
          v-model="dataNascimento"
          type="text"
          required
        />
        <span>Data de nascimento</span>
      </label>

      <label>
        <input class="input" v-model="email" type="email" required />
        <span>Email</span>
      </label>

      <label>
        <input class="input" v-model="senha" type="password" required />
        <span>Senha</span>
      </label>
      <label>
        <input class="input" v-model="confirmarSenha" type="password" required />
        <span>Confirmar senha</span>
      </label>
      <button class="submit" @click="registrar">Enviar</button>
      <p class="signin">
        Já possui uma conta? <router-link :to="{ name: 'login' }">Entrar</router-link>
      </p>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'
import { vMaska } from 'maska/vue'
import { useAuthStore } from '@/store/AuthStore'
import { UserRegister } from '@/class/UserRegister'
import { useMessage } from 'naive-ui'
import router from '@/router'
import { validationFieldsRegister } from '@/utils/validation'

const nome = ref<string>('')
const apelido = ref<string>('')
const nomeCompleto = ref<string>('')
const dataNascimento = ref<string>('')
const email = ref<string>('')
const senha = ref<string>('')
const confirmarSenha = ref<string>('')

const auth = useAuthStore()
const message = useMessage()

const registrar = async (e: MouseEvent) => {
  e.preventDefault();

  try {
    const validacao = validationFieldsRegister({
      nome: nome.value,
      apelido: apelido.value,
      nomeCompleto: nomeCompleto.value,
      dataNascimento: dataNascimento.value,
      email: email.value,
      senha: senha.value,
      confirmarSenha: confirmarSenha.value
    })

    if (typeof validacao === 'string') {
      message.error(validacao)
    } else {
      const [dia, mes, ano] = dataNascimento.value.split('/')
      const data = new Date(parseInt(ano), parseInt(mes) - 1, parseInt(dia))
      const userRegister = new UserRegister(
        nome.value,
        apelido.value,
        nomeCompleto.value,
        data,
        email.value,
        senha.value
      )

      const result = await auth.register(userRegister)
      message.success(result)
      await router.push({ name: 'login' })
    }
  } catch (error: any) {
    message.error(error.message)
  }
}

onMounted(() => document.title = 'Leitor de mangás - Registrar-se')
</script>

<style scoped>
.container-form {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #292929;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 350px;
  padding: 20px;
  border-radius: 20px;
  position: relative;
  background-color: #1a1a1a;
  color: #fff;
  border: 1px solid #333;
}

.title {
  font-size: 28px;
  font-weight: 600;
  letter-spacing: -1px;
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 30px;
  color: #00bfff;
}

.title::before {
  width: 18px;
  height: 18px;
}

.title::after {
  width: 18px;
  height: 18px;
  animation: pulse 1s linear infinite;
}

.title::before,
.title::after {
  position: absolute;
  content: '';
  height: 16px;
  width: 16px;
  border-radius: 50%;
  left: 0;
  background-color: #00bfff;
}

.message,
.signin {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.7);
}

.signin {
  text-align: center;
}

.signin a:hover {
  text-decoration: underline royalblue;
}

.signin a {
  color: #00bfff;
}

.flex {
  display: flex;
  width: 100%;
  gap: 6px;
}

.form label {
  position: relative;
}

.form label .input {
  background-color: #333;
  color: #fff;
  width: 100%;
  padding: 20px 05px 05px 10px;
  outline: 0;
  border: 1px solid rgba(105, 105, 105, 0.397);
  border-radius: 10px;
}

.form label .input + span {
  color: rgba(255, 255, 255, 0.5);
  position: absolute;
  left: 10px;
  top: 0;
  font-size: 0.9em;
  cursor: text;
  transition: 0.3s ease;
}

.form label .input:placeholder-shown + span {
  top: 13px;
  font-size: 0.9em;
}

.form label .input:focus + span,
.form label .input:valid + span {
  color: #00bfff;
  top: 0;
  font-size: 0.7em;
  font-weight: 600;
}

.input {
  font-size: medium;
}

.submit {
  border: none;
  outline: none;
  padding: 10px;
  border-radius: 10px;
  color: #fff;
  font-size: 16px;
  transition: transform 0.3s ease;
  background-color: #00bfff;
}

.submit:hover {
  background-color: #00bfff96;
}

@keyframes pulse {
  from {
    transform: scale(0.9);
    opacity: 1;
  }

  to {
    transform: scale(1.8);
    opacity: 0;
  }
}
</style>
