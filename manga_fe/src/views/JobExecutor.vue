<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Jobs" size="huge">
            <div class="job-container">
                <div class="job-list">
                    <n-list bordered>
                        <n-list-item v-for="job in jobs" :key="job">
                            <n-button @click="selectJob(job)">{{ job }}</n-button>
                        </n-list-item>
                    </n-list>
                </div>
                <div class="job-form" v-if="selectedJob">
                    <n-form ref="formRef" label-width="120px">
                        <n-form-item label="Parâmetros" path="parametros">
                            <input v-model="parametros" placeholder="Digite os parâmetros" type="text"
                                class="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition" />
                        </n-form-item>
                        <n-button type="primary" @click="executeJob">Executar Job</n-button>
                    </n-form>
                </div>
            </div>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import { NCard, NList, NListItem, NButton, NForm, NFormItem, useMessage } from 'naive-ui';
import { api } from '@/network/axiosInstance';

const jobs = ref<string[]>([]);
const message = useMessage();
let parametros = ref<string>("");
const token = localStorage.getItem('token');
const selectedJob = ref<string>("");

const executeJob = async (e: MouseEvent) => {
    e.preventDefault();
    try {
        const job = selectedJob.value;
        const result = await api.post(`/api/v1/job/${job}/${parametros.value}`, null, {
            headers: {
                Authorization: `${token}`
            }
        });
        if(result.status === 200)
            message.success("Job executado com sucesso!");
        else
            message.info("Houve um erro ao executar o Job.")
    } catch (error: any) {
        message.error(error);
        throw new Error(error.response.data);
    } finally {
        limpaDados();
    }
}

const limpaDados = () => {
    selectedJob.value = "";
    parametros.value = "";
}

const selectJob = (job: string) => {
    selectedJob.value = job;
};

const getJobsDisponiveis = async () => {
    try {
        const result = await api.get("/api/v1/job/get-jobs", {
            headers: {
                Authorization: `${token}`
            }
        });
        jobs.value = result.data;
    } catch (error: any) {
        throw new Error(error.response.data);
    }
}

onMounted(() => {
    getJobsDisponiveis();
})

</script>

<style lang="css" scoped>
main {
    padding: 15px;
}

.n-card {
    height: 95vh;
    box-sizing: border-box;
    overflow: scroll;
    overflow-x: hidden;
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
</style>
