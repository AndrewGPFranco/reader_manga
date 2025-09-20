<template>
  <header>
    <MenuComponent />
  </header>
  <main>
    <n-card title="Jobs" size="huge" style="height: 95vh; overflow-y: auto;">
      <div class="job-container">
        <div class="job-list">
          <n-list bordered>
            <n-list-item v-for="job in jobs" :key="job.nomeJob">
              <n-button @click="selectJob(job.nomeJob, job.tipoDoJob)">{{ job.nomeJob }}</n-button>
            </n-list-item>
          </n-list>
        </div>
        <div class="job-form" v-if="selectedJob">
          <n-form ref="formRef" label-width="120px">
            <n-form-item label="Parâmetros" path="parametros" v-if="tipoJob === 'Parâmetros'">
              <n-input
                v-model:value="parametros"
                placeholder="Digite os parâmetros"
                type="text"
              />
            </n-form-item>
            <n-form-item label="Número do Capítulo" path="parametros" v-if="tipoJob === 'Uploads'">
              <n-input
                clearable
                v-model:value="titleChapter"
                placeholder="Digite o número do capítulo"
              />
            </n-form-item>
            <n-form-item label="Título do Mangá" path="parametros" v-if="tipoJob === 'Uploads'">
              <n-select v-model:value="titleManga" placeholder="Escolha o nome do mangá" :options="generalOptionsChapter" />
            </n-form-item>
            <n-form-item label="PDF do capítulo" path="parametros" v-if="tipoJob === 'Uploads'">
              <n-upload
                multiple
                directory-dnd
                :max="5"
                @change="handleFileChange"
              >
                <n-upload-dragger>
                  <div style="margin-bottom: 12px">
                    <n-icon size="48" :depth="3">
                      <ArchiveIcon />
                    </n-icon>
                  </div>
                  <n-text style="font-size: 16px">
                    Clique ou arraste um arquivo para esta área para fazer upload.
                  </n-text>
                </n-upload-dragger>
              </n-upload>
            </n-form-item>
            <div id="events" v-if="isExibirProgresso">
              <h3>Progresso do Job:</h3>
              <n-progress
                type="line"
                :percentage="progressoEmTempoReal"
                indicator-placement="inside"
                processing
              />
            </div>
            <n-button type="primary" @click="executeJob">Executar Job</n-button>
          </n-form>
        </div>
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import { URL_SSE } from '@/utils/utils'
import type IJobType from '@/@types/IJobType'
import { api } from '@/network/axiosInstance'
import { useMangaStore } from '@/store/MangaStore'
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'
import MenuComponent from '@/components/global/MenuComponent.vue'
import { NCard, NList, NListItem, NButton, NForm, NFormItem, useMessage } from 'naive-ui'

const message = useMessage()
const mangaStore = useMangaStore()
const jobs = ref<IJobType[]>([])
const selectedJob = ref<string>('')
const mangasNomes = ref<string[]>([]);
const token = localStorage.getItem('token')

let selectedFile = ref<File>()
let titleManga = ref<string>()
let titleChapter = ref<string>()
let tipoJob = ref<string>('')
let parametros = ref<string>('')
let eventSource: EventSource | null = null;
let progressoEmTempoReal = ref<number>(0);
let isExibirProgresso = ref<boolean>(false);

const handleFileChange = (fileList: any) => {
  if (fileList.fileList.length > 0) {
    selectedFile.value = fileList.fileList[0].file
  }
}

const generalOptionsChapter = computed(() => {
  if(mangasNomes.value) {
    return mangasNomes.value.map(manga => ({
      label: manga,
      value: manga
    }));
  }
  return [];
});

const executeJob = async (e: MouseEvent) => {
  e.preventDefault()
  try {
    const isTipoParametros = tipoJob.value === 'Parâmetros'
    const job = selectedJob.value.toUpperCase()

    let validation

    if (isTipoParametros) validation = validaInput()
    else validation = true

    if (validation) {
      let result
      isExibirProgresso.value = true
      if (isTipoParametros) {
        result = await api.post(`/api/v1/job/${job}/${parametros.value}`, null, {
          headers: {
            Authorization: `${token}`
          }
        })
      } else {
        executaProgressoJob();
        const dados = new FormData()
        if (selectedFile.value != null) dados.append('file', selectedFile.value)
        if (titleChapter.value != null) dados.append('titleChapter', titleChapter.value)
        if (titleManga.value != null) dados.append('titleManga', titleManga.value)

        result = await api.post(`/api/v1/job/upload-chapter`, dados, {
          headers: {
            Authorization: `${token}`
          }
        })
      }

      if (result.status === 200 && result.data != '') {
        progressoEmTempoReal.value = 100;
        message.success('Job executado com sucesso!')
        fechaEventSource();
        limpaDados();
      }
      else message.info('Houve um erro ao executar o Job.')
    } else message.error('Digite os parâmetros para executar o Job.')
  } catch (error: any) {
    message.error(error)
    throw new Error(error.response.data)
  } finally {
    progressoEmTempoReal.value = 0;
    isExibirProgresso.value = false;
  }
}

const executaProgressoJob = () => {
  eventSource = new EventSource(URL_SSE);

  eventSource.onmessage = (event) => {
    const data = event.data;
    if (data === "Job concluído com sucesso!") {
      progressoEmTempoReal.value = 100;
      fechaEventSource();
    } else {
      progressoEmTempoReal.value = parseInt(data);
    }
  };

  eventSource.onerror = (error) => {
    console.error("Error occurred:", error);
    if(eventSource != null) {
      eventSource.close();
      progressoEmTempoReal.value = 0;
    }
  };
}

const fechaEventSource = () => {
  if (eventSource) {
    eventSource.close()
    eventSource = null
    progressoEmTempoReal.value = 0;
  }
}

const validaInput = () => {
  return parametros.value.length !== 0
}

const limpaDados = () => {
  selectedJob.value = ''
  parametros.value = ''
  titleChapter.value = ''
  titleManga.value = ''
}

const selectJob = (job: string, tipoDoJob: string) => {
  selectedJob.value = job
  tipoJob.value = tipoDoJob
}

const getJobsDisponiveis = async () => {
  try {
    const result: IJobType[] = await api.get('/api/v1/job/get-jobs', {
      headers: {
        Authorization: `${token}`
      }
    })
    // @ts-ignore
    jobs.value = result.data
  } catch (error: any) {
    throw new Error(error.response.data)
  }
}

const getNomeDosMangasDisponiveis = async () => {
  mangasNomes.value = await mangaStore.getApenasNomeDosMangas();
}

onMounted(() => {
  getJobsDisponiveis();
  getNomeDosMangasDisponiveis();
  document.title = 'Leitor de mangás - Jobs'
});

onUnmounted(() => fechaEventSource());
</script>

<style lang="css" scoped>
main {
  padding: 15px;
}

.job-container {
  display: flex;
  gap: 20px;
}

.job-list {
  flex: 1;
}

.job-form {
  flex: 2;
  border-left: 1px solid #ddd;
  padding-left: 20px;
}

.n-card {
  height: 95vh;
}

#events {
  margin: 30px;
}
</style>
