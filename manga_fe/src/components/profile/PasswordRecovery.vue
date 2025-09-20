<template>
  <section class="form-container">
    <form class="form">
      <h2 id="heading">Alteração de Senha</h2>
      <div class="field">
        <svg
          class="input-icon"
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 16 16"
        >
          <path
            d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"
          ></path>
        </svg>
        <input
          autocomplete="off"
          placeholder="Senha anterior"
          v-model="senhaAntiga"
          class="input-field"
          type="password"
        />
      </div>
      <div class="field">
        <svg
          class="input-icon"
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 16 16"
        >
          <path
            d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"
          ></path>
        </svg>
        <input
          autocomplete="off"
          placeholder="Nova senha"
          v-model="novaSenha"
          class="input-field"
          type="password"
        />
      </div>
      <div class="field">
        <svg
          class="input-icon"
          xmlns="http://www.w3.org/2000/svg"
          width="16"
          height="16"
          viewBox="0 0 16 16"
        >
          <path
            d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2z"
          ></path>
        </svg>
        <input
          autocomplete="off"
          placeholder="Repita a nova senha"
          v-model="novaSenhaRepetida"
          class="input-field"
          type="password"
        />
      </div>
      <div class="btn">
        <button class="button2" @click.prevent="efetuarTrocaSenha">Alterar</button>
      </div>
    </form>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useAuthStore } from '@/store/AuthStore'

const auth = useAuthStore()
const message = useMessage()
const novaSenha = ref<string>('')
const senhaAntiga = ref<string>('')
const novaSenhaRepetida = ref<string>('')
const emit = defineEmits(['atualizaForm'])

const efetuarTrocaSenha = async (e: MouseEvent) => {
  e.preventDefault()

  if (novaSenha.value === novaSenhaRepetida.value) {
    const validatePassword = validationPassword(novaSenha.value)
    if (typeof validatePassword != 'string') {
      const result = await auth.changePassword(senhaAntiga.value, novaSenha.value)
      if (result.keys().next().value) {
        message.success(result.values().next().value ?? 'Success')
        clearForm()
        emit('atualizaForm', true)
      } else message.error(result.values().next().value ?? 'An error occurred')
    } else return message.error(validatePassword)
  } else message.info('As senhas precisam ser iguais.')
}

const validationPassword = (password: string): string | boolean => {
  if (!password.trim()) {
    return 'Senha é obrigatória!'
  }

  if (password.length < 8) {
    return 'Senha deve ter no mínimo 8 caracteres!'
  }

  const senhaRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/
  if (!senhaRegex.test(password)) {
    return 'Senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial!'
  }

  return true
}

const clearForm = () => {
  senhaAntiga.value = ''
  novaSenha.value = ''
  novaSenhaRepetida.value = ''
}
</script>

<style lang="css" scoped>
.form-container {
  display: flex;
  justify-content: center;
  margin: 20px;
  padding: 2em;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
  width: 40vw;
  max-width: 500px;
  transition: transform 0.3s ease;
}

.form:hover {
  transform: translateY(-5px);
}

#heading {
  text-align: center;
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1em;
}

.field {
  display: flex;
  align-items: center;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 12px;
  box-shadow: inset 2px 4px 8px rgba(0, 0, 0, 0.05);
}

.input-icon {
  height: 20px;
  width: 20px;
  fill: #6b7280;
  margin-right: 10px;
}

.input-field {
  background: transparent;
  border: none;
  outline: none;
  width: 100%;
  font-size: 1rem;
}

.field:focus-within {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.5);
}

.btn {
  display: flex;
  justify-content: center;
  margin-top: 1.5em;
}

.button2 {
  background-color: #2563eb;
  color: white;
  padding: 0.8em 2em;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.button2:hover {
  background-color: #1d4ed8;
  box-shadow: 0 8px 16px rgba(37, 99, 235, 0.3);
}
</style>
