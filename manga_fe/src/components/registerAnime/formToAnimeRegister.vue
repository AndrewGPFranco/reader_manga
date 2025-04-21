<template>
  <div class="p-6 bg-white rounded-2xl shadow-md">
    <n-form ref="formRef" :size="'medium'" label-placement="top">
      <n-grid :span="24" :x-gap="24" :y-gap="16">
        <n-form-item-gi :span="12" label="Nome" path="title">
          <n-input
            v-model:value="title"
            placeholder="Digite o nome do anime"
            clearable
            @keydown.enter="realizarInsercaoAnime"
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

const toast = useMessage();
const animeStore = useAnimeStore();
const title = ref<string>('');
let action = ref<string>('Registrar');

const realizarInsercaoAnime = async () => {
  try {
    const response: ResponseAPI = await animeStore.registraAnime(title.value);

    if(response.statusCode === 201)
      toast.success(response.message);

    resetForm();
  } catch (error) {
    toast.error(String(error));
  } finally {
    action.value = 'Registrar';
  }
}

const resetForm = () => {
  title.value = '';
}

</script>