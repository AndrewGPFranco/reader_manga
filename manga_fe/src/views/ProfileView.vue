<template>
  <header>
    <MenuComponent />
  </header>
  <main>
    <n-card style="height: 95vh; overflow-y: auto">
      <section class="card-content">
        <div class="profile-container">
          <img :src="profilePhoto || profilePhotoDefault" alt="Foto de Perfil" class="profile-image" />
          <h2 class="profile-name">{{ name }}</h2>
          <p class="profile-info">{{ email }}</p>
          <p class="profile-info">Nickname: {{ username }}</p>
          <p class="profile-info">Data de Nascimento: {{ dateBirth }}</p>
        </div>

        <div class="button-group">
          <button @click="efetuarLogout" class="logout-button">Logout</button>
          <button @click="acionaFormDeTrocaDeSenha" class="change-password-button">
            Alterar Senha
          </button>
        </div>
      </section>

      <section class="containerPasswordRecovery" v-if="isPasswordRecovery">
        <PasswordRecovery @atualiza-form="atualizaForm" />
      </section>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import router from '@/router'
import { onMounted, ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useAuthStore } from '@/store/AuthStore'
import MenuComponent from '@/components/global/MenuComponent.vue'
import PasswordRecovery from '@/components/profile/PasswordRecovery.vue'

const auth = useAuthStore()
const toast = useMessage()
const name = ref<string>('')
const email = ref<string>('')
const username = ref<string>('')
const dateBirth = ref<Date | null>(null)
let isPasswordRecovery = ref<boolean>(false)
const profilePhoto = ref<string | null>(null)
const profilePhotoDefault = new URL("../assets/home/op.jpeg", import.meta.url).href;

/**
 * Emit recuperado pelo componente PasswordRecovery para esconder novamente o formulário.
 */
const atualizaForm = () => {
  isPasswordRecovery.value = false
}

const efetuarLogout = () => {
  auth.efetuarLogout()
  router.push({ name: 'login' })
}

const acionaFormDeTrocaDeSenha = () => {
  isPasswordRecovery.value = !isPasswordRecovery.value
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Perfil'

  try {
    const result = await auth.getUser()
    name.value = result.firstName
    email.value = result.email
    dateBirth.value = result.dateBirth
    username.value = result.username
    profilePhoto.value = result.profilePhoto
  } catch (error: any) {
    console.error(error.message)
    toast.error(error.message)
  }
})
</script>

<style scoped>
main {
  padding: 15px;
}

.card-content {
  width: 75vw;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
}

.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.profile-image {
  width: 400px;
  height: 400px;
  object-fit: cover;
  border-radius: 50%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.profile-name {
  font-size: 1.75rem;
  font-weight: 600;
  margin-top: 10px;
}

.profile-info {
  margin: 5px 0;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

button {
  padding: 10px 15px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-button {
  background-color: #e74c3c;
}

.logout-button:hover {
  background-color: #c0392b;
}

.change-password-button {
  background-color: #3498db;
}

.change-password-button:hover {
  background-color: #2980b9;
}
</style>
