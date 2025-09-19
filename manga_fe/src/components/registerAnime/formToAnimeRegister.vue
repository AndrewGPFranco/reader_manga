<template>
  <div class="p-6 rounded-2xl shadow-md">
    <n-form ref="formRef" :size="'medium'" label-placement="top">
      <n-grid :span="24" :x-gap="24" :y-gap="16">
        <n-form-item-gi :span="12" label="Nome" path="title">
          <n-input v-model:value="title" placeholder="Digite o nome do anime" clearable />
        </n-form-item-gi>

        <n-form-item-gi :span="12" label="URL imagem" path="uriImage">
          <n-input v-model:value="uriImage" placeholder="Digite a url da imagem" clearable />
        </n-form-item-gi>

        <n-form-item-gi :span="12" label="Data de Lançamento" path="releaseDate">
          <n-date-picker
            v-model:value="releaseDate"
            type="datetime"
            placeholder="Informe a data de lançamento"
          />
        </n-form-item-gi>

        <n-gi :span="24">
          <div class="flex justify-end mt-4">
            <n-button round type="primary" @click="realizarInsercaoAnime">
              {{ action }}
            </n-button>
          </div>
        </n-gi>
      </n-grid>
    </n-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useMessage } from 'naive-ui'
import { useAnimeStore } from '@/store/AnimeStore'
import type ResponseAPI from '@/class/api/ResponseAPI'

const toast = useMessage()
const title = ref<string>('')
const uriImage = ref<string>('')
const animeStore = useAnimeStore()
let action = ref<string>('Registrar')
const releaseDate = ref<number | null>(null)

const realizarInsercaoAnime = async () => {
  try {
    const response: ResponseAPI = await animeStore.registraAnime(
      title.value,
      uriImage.value,
      new Date(releaseDate.value!)
    )

    if (response.statusCode === 201) toast.success(response.message)

    resetForm()
  } catch (error) {
    toast.error(String(error))
  } finally {
    action.value = 'Registrar'
  }
}

const resetForm = () => {
  title.value = ''
  uriImage.value = ''
}
</script>
