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
        <input class="input" v-maska data-maska="##/##/####" v-model="dataNascimento" type="text" required />
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
  min-height: 100vh;
  background: linear-gradient(135deg, #292929 0%, #1a1a1a 100%);
  padding: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 400px;
  padding: 30px;
  border-radius: 25px;
  background: #1a1a1a;
  color: #fff;
  border: 1px solid #444;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.5);
}

.title {
  font-size: 32px;
  font-weight: 700;
  letter-spacing: -1px;
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 40px;
  color: #00bfff;
  text-transform: uppercase;
}

.title::before,
.title::after {
  position: absolute;
  content: '';
  height: 18px;
  width: 18px;
  border-radius: 50%;
  left: 10px;
  background-color: #00bfff;
  transition: all 0.3s ease;
}

.title::after {
  animation: pulse 1.5s ease-in-out infinite;
}

.message,
.signin {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  text-align: center;
}

.signin a {
  color: #00bfff;
  text-decoration: none;
  font-weight: 600;
}

.signin a:hover {
  text-decoration: underline;
  color: #00ccff;
}

.flex {
  display: flex;
  width: 100%;
  gap: 10px;
}

.form label {
  position: relative;
  margin-bottom: 10px;
}

.form label .input {
  background-color: #2a2a2a;
  color: #fff;
  width: 100%;
  padding: 22px 10px 8px 12px;
  outline: none;
  border: 1px solid rgba(105, 105, 105, 0.5);
  border-radius: 12px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form label .input:focus,
.form label .input:valid {
  border-color: #00bfff;
  box-shadow: 0 0 8px rgba(0, 191, 255, 0.3);
}

.form label .input+span {
  color: rgba(255, 255, 255, 0.6);
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 0.9em;
  pointer-events: none;
  transition: all 0.3s ease;
}

.form label .input:focus+span,
.form label .input:valid+span {
  color: #00bfff;
  top: 10px;
  font-size: 0.7em;
  font-weight: 600;
  transform: translateY(0);
}

.input {
  font-size: 16px;
}

.submit {
  border: none;
  outline: none;
  padding: 12px;
  border-radius: 12px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(90deg, #00bfff 0%, #007acc 100%);
  cursor: pointer;
  transition: transform 0.3s ease, background 0.3s ease;
}

.submit:hover {
  background: linear-gradient(90deg, #00ccff 0%, #0088dd 100%);
  transform: translateY(-2px);
}

.submit:active {
  transform: translateY(0);
}

@keyframes pulse {
  0% {
    transform: scale(0.9);
    opacity: 1;
  }

  100% {
    transform: scale(1.6);
    opacity: 0;
  }
}

@media (max-width: 480px) {
  .form {
    max-width: 100%;
    margin: 0 15px;
    padding: 20px;
  }

  .title {
    font-size: 28px;
    padding-left: 30px;
  }

  .flex {
    flex-direction: column;
    gap: 15px;
  }
}
</style>