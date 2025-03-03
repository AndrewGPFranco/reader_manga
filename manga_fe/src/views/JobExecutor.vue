<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card title="Jobs" size="huge">
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
              <input
                v-model="parametros"
                placeholder="Digite os parâmetros"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              />
            </n-form-item>
            <n-form-item label="Parâmetros" path="parametros" v-else>
              <input
                v-model="titleChapter"
                placeholder="Digite o título do capítulo"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              />
              <input
                v-model="titleManga"
                placeholder="Digite o nome do mangá"
                type="text"
                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
              />
              <n-upload
                multiple
                directory-dnd
                action="https://www.mocky.io/v2/5e4bafc63100007100d8b70f"
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
            <n-button type="primary" @click="executeJob">Executar Job</n-button>
          </n-form>
        </div>
      </div>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { NCard, NList, NListItem, NButton, NForm, NFormItem, useMessage } from 'naive-ui'
import { api } from '@/network/axiosInstance'
import type IJobType from '@/@types/IJobType'
import { ArchiveOutline as ArchiveIcon } from '@vicons/ionicons5'

const jobs = ref<IJobType[]>([]);
const message = useMessage();
const token = localStorage.getItem('token');
const selectedJob = ref<string>('');

let parametros = ref<string>('');
let tipoJob = ref<string>('');
let selectedFile = ref<File>();
let titleChapter = ref<string>();
let titleManga = ref<string>();

const handleFileChange = (fileList: any) => {
  if (fileList.fileList.length > 0) {
    selectedFile.value = fileList.fileList[0].file;
  }
};

const executeJob = async (e: MouseEvent) => {
  e.preventDefault()
  try {
    const isTipoParametros = tipoJob.value === "Parâmetros";
    const job = selectedJob.value.toUpperCase();

    let validation;

    if(isTipoParametros)
      validation = validaInput();
    else
      validation = true;

    if (validation) {
      let result;
      if(isTipoParametros) {
        result = await api.post(`/api/v1/job/${job}/${parametros.value}`, null, {
          headers: {
            Authorization: `${token}`
          }
        })
      } else {
        const dados = new FormData();
        if(selectedFile.value != null) dados.append("file", selectedFile.value);
        if(titleChapter.value != null) dados.append("titleChapter", titleChapter.value);
        if(titleManga.value != null) dados.append("titleManga", titleManga.value);

        result = await api.post(`/api/v1/job/upload-chapter`, dados, {
          headers: {
            Authorization: `${token}`
          }
        });
      }

      if (result.status === 200 && result.data != '') message.success('Job executado com sucesso!')
      else message.info('Houve um erro ao executar o Job.')
    } else message.error('Digite os parâmetros para executar o Job.')
  } catch (error: any) {
    message.error(error)
    throw new Error(error.response.data)
  } finally {
    limpaDados()
  }
}

const validaInput = () => {
  return parametros.value.length !== 0
}

const limpaDados = () => {
  selectedJob.value = ''
  parametros.value = ''
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

onMounted(() => {
  getJobsDisponiveis()
})
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
</style>
