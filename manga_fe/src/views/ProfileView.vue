<template>
  <header>
    <MenuComponent />
  </header>
  <main class="flex p-4">
    <n-card class="w-full mt-1" :bordered="false">
      <section>
        <div class="p-8">
          <div class="flex gap-8 items-start profile-container">
            <div class="relative flex-shrink-0">
              <img
                :src="profilePhoto || profilePhotoDefault"
                alt="Foto de Perfil"
                class="w-72 h-72 object-cover rounded-full transition-all duration-300 cursor-pointer hover:scale-105"
              />
              <div
                @click="mostrarContainerAlterarFoto"
                class="absolute inset-0 bg-black bg-opacity-50 rounded-full flex items-center justify-center opacity-0 hover:opacity-100 transition-all duration-300 cursor-pointer"
              >
                <NIcon :size="32" class="text-white drop-shadow-md">
                  <EnterOutline />
                </NIcon>
              </div>
            </div>

            <div class="flex-1 min-w-0 user-info-container">
              <h1 class="text-3xl font-bold mb-6 leading-tight">{{ name }}</h1>

              <div class="flex flex-col gap-4">
                <div class="flex items-start gap-3">
                  <NIcon :size="18" class="mt-0.5 opacity-60">
                    <EnterOutline />
                  </NIcon>
                  <div class="flex flex-col gap-1 min-w-0">
                    <span class="text-sm font-medium opacity-70 uppercase tracking-wide"
                      >Email</span
                    >
                    <span class="text-base font-semibold break-words">{{ email }}</span>
                  </div>
                </div>

                <div class="flex items-start gap-3">
                  <NIcon :size="18" class="mt-0.5 opacity-60">
                    <EnterOutline />
                  </NIcon>
                  <div class="flex flex-col gap-1 min-w-0">
                    <span class="text-sm font-medium opacity-70 uppercase tracking-wide"
                      >Nickname</span
                    >
                    <span class="text-base font-semibold break-words">{{ username }}</span>
                  </div>
                </div>

                <div class="flex items-start gap-3">
                  <NIcon :size="18" class="mt-0.5 opacity-60">
                    <EnterOutline />
                  </NIcon>
                  <div class="flex flex-col gap-1 min-w-0">
                    <span class="text-sm font-medium opacity-70 uppercase tracking-wide"
                      >Data de Nascimento</span
                    >
                    <span class="text-base font-semibold break-words">{{ dateBirth }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="isPasswordRecovery" class="flex-1 min-w-0 max-w-md password-container">
              <div class="rounded-lg p-6 border">
                <h3 class="text-xl font-semibold mb-6 text-center">Alterar Senha</h3>
                <PasswordRecovery @atualiza-form="atualizaForm" />
              </div>
            </div>
          </div>
        </div>

        <n-divider />

        <div class="px-8 py-6 flex gap-4 justify-center flex-wrap">
          <n-button @click="acionaFormDeTrocaDeSenha" type="primary" size="large" class="min-w-36">
            <template #icon>
              <NIcon><EnterOutline /></NIcon>
            </template>
            {{ isPasswordRecovery ? 'Ocultar Alterar Senha' : 'Alterar Senha' }}
          </n-button>

          <n-button @click="efetuarLogout" type="error" size="large" class="min-w-36">
            <template #icon>
              <NIcon><EnterOutline /></NIcon>
            </template>
            Logout
          </n-button>
        </div>
      </section>
    </n-card>

    <n-modal v-model:show="mostrarAlterarFoto">
      <n-card
        style="width: 500px"
        title="Alterar Foto de Perfil"
        :bordered="false"
        size="huge"
        role="dialog"
        aria-modal="true"
      >
        <template #header-extra>
          <n-button quaternary circle @click="mostrarAlterarFoto = false">
            <template #icon>
              <NIcon><EnterOutline /></NIcon>
            </template>
          </n-button>
        </template>

        <div class="my-4">
          <n-upload multiple directory-dnd :max="1" @change="handleFileChange">
            <n-upload-dragger>
              <div class="flex flex-col items-center gap-3 p-6 text-center">
                <n-icon size="48" class="opacity-60">
                  <ArchiveIcon />
                </n-icon>
                <n-text class="text-base font-medium"> Clique ou arraste uma imagem aqui </n-text>
                <n-text depth="3" class="text-sm">
                  Formatos aceitos: JPG, PNG, GIF (máx. 5MB)
                </n-text>
              </div>
            </n-upload-dragger>
          </n-upload>
        </div>

        <template #footer>
          <div class="flex gap-3 justify-end">
            <n-button @click="mostrarAlterarFoto = false"> Cancelar </n-button>
            <n-button type="primary" @click="alterarFoto" :loading="uploading">
              Salvar Foto
            </n-button>
          </div>
        </template>
      </n-card>
    </n-modal>
  </main>
</template>

<script setup lang="ts">
import router from '@/router'
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/store/AuthStore'
import MenuComponent from '@/components/global/MenuComponent.vue'
import PasswordRecovery from '@/components/profile/PasswordRecovery.vue'
import { EnterOutline, ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'
import { useMessage, NModal, NUpload, NButton, NCard, NDivider } from 'naive-ui'
import { formatDate } from '@/utils/utils'

const toast = useMessage()
const auth = useAuthStore()
let selectedFile = ref<File>()
const name = ref<string>('')
const email = ref<string>('')
const username = ref<string>('')
const uploading = ref<boolean>(false)
const dateBirth = ref<string | null>(null)
let isPasswordRecovery = ref<boolean>(false)
const profilePhoto = ref<string | null>(null)
const mostrarAlterarFoto = ref<boolean>(false)
const profilePhotoDefault = new URL('../assets/home/op.jpeg', import.meta.url).href

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

const mostrarContainerAlterarFoto = () => {
  mostrarAlterarFoto.value = !mostrarAlterarFoto.value
}

const handleFileChange = (fileList: any) => {
  if (fileList.fileList.length > 0) {
    selectedFile.value = fileList.fileList[0].file
  }
}

const alterarFoto = async () => {
  if (!selectedFile.value) {
    toast.warning('Selecione uma imagem primeiro')
    return
  }

  uploading.value = true
  const dados = new FormData()
  dados.append('file', selectedFile.value)

  try {
    await auth.changeProfilePhoto(dados)
    await atualizaUsuarioLogado()
    mostrarAlterarFoto.value = false
    toast.success('Foto alterada com sucesso!')
  } catch (error) {
    toast.error(error as string)
  } finally {
    uploading.value = false
  }
}

const atualizaUsuarioLogado = async () => {
  const result = await auth.getUser()
  name.value = result.firstName
  email.value = result.email
  dateBirth.value = formatDate(result.dateBirth)
  username.value = result.username
  profilePhoto.value = `http://localhost:8080${result.profilePhoto}?t=${Date.now()}`
}

onMounted(async () => {
  document.title = 'Leitor de mangás - Perfil'

  try {
    await atualizaUsuarioLogado()
  } catch (error: any) {
    toast.error(error.message)
  }
})
</script>

<style scoped>
.profile-container {
  min-height: 300px;
}

.password-container {
  animation: slideInRight 0.3s ease-out;
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@media (max-width: 1200px) {
  .profile-container {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 2rem;
  }

  .password-container {
    max-width: 100%;
    width: 100%;
  }

  .user-info-container {
    max-width: 500px;
  }
}

@media (max-width: 768px) {
  .profile-container {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 1.5rem;
  }

  .w-72.h-72 {
    width: 12rem;
    height: 12rem;
  }

  .text-3xl {
    font-size: 1.75rem;
  }

  .px-8.py-6 {
    padding: 1.25rem 1.5rem;
  }

  .flex.gap-4.justify-center.flex-wrap {
    flex-direction: column;
    align-items: stretch;
  }

  .min-w-36 {
    width: 100%;
    min-width: unset;
  }
}

@media (max-width: 500px) {
  .w-72.h-72 {
    width: 7.5rem;
    height: 7.5rem;
  }

  .text-3xl {
    font-size: 1.5rem;
  }
}
</style>
