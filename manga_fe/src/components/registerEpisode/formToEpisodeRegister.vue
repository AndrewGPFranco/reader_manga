<template>
  <div class="p-6 bg-white rounded-2xl shadow-md">
    <n-form ref="formRef" :size="'medium'" label-placement="top">
      <n-grid :span="24" :x-gap="24" :y-gap="16">
        <n-form-item-gi :span="12" label="Título" path="title">
          <n-input
            v-model:value="title"
            placeholder="Digite o título do episódio"
            clearable
          />
        </n-form-item-gi>

        <n-form-item-gi :span="12" label="ID" path="id">
          <n-input
            v-model:value="id"
            placeholder="Digite um id para o episódio"
            clearable
          />
        </n-form-item-gi>

        <n-form-item-gi :span="12" label="Episódio" path="episode">
          <n-upload
            multiple
            directory-dnd
            :max="5"
            @change="handleFileChange"
            class="w-full"
          >
            <n-upload-dragger class="p-6 border-dashed border-2 border-gray-300 rounded-xl bg-gray-50 hover:bg-gray-100 transition">
              <div class="flex flex-col items-center space-y-3">
                <n-icon size="48" :depth="3">
                  <ArchiveIcon />
                </n-icon>
                <n-text class="text-base text-gray-700 text-center">
                  Clique ou arraste arquivos aqui para fazer o upload
                </n-text>
              </div>
            </n-upload-dragger>
          </n-upload>
        </n-form-item-gi>

        <n-gi :span="24">
          <div class="flex justify-end mt-4">
            <n-button round type="primary" @click="realizarUploadEpisodio">
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
import { useEpisodeStore } from '@/store/EpisodeStore'
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'

const id = ref<string>('');
const toast = useMessage();
const title = ref<string>('');
const episode = ref<File[]>([]);
const selectedFile = ref<File>();
const action = ref<string>('Enviar');
const episodeStore = useEpisodeStore();

const handleFileChange = (fileList: any) => {
  if (fileList.fileList.length > 0) {
    selectedFile.value = fileList.fileList[0].file
    episode.value = fileList.fileList.map((item: any) => item.file)
  }
}

const realizarUploadEpisodio = async () => {
  try {
    if(selectedFile.value !== undefined) {
      const formData = new FormData();
      formData.append("file", selectedFile.value);
      formData.append("id", id.value);
      formData.append("title", title.value);

      await episodeStore.uploadEpisode(formData);
    }
  } catch(error) {
    toast.error(String(error));
  }
}
</script>