<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Jobs" size="huge">
            <div class="job-container">
                <div class="job-list">
                    <n-list bordered>
                        <n-list-item v-for="job in jobs" :key="job.id">
                            <n-button @click="selectJob(job)">{{ job }}</n-button>
                        </n-list-item>
                    </n-list>
                </div>
                <div class="job-form" v-if="selectedJob">
                    <n-form ref="formRef" label-width="120px">
                        <n-form-item label="Parâmetros" path="parametros">
                            <n-input v-model="parametros" placeholder="Digite os parâmetros" type="text" />
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
import { NCard, NList, NListItem, NButton, NForm, NFormItem, NInput } from 'naive-ui';
import { api } from '@/network/axiosInstance';

const parametros = ref<string[]>([""]);
const jobs = ref<any[]>([]);
const selectedJob = ref<string | null>(null);

const executeJob = () => {
    console.log("Executando JOB.")
}

const selectJob = (job: string) => {
    selectedJob.value = job;
};

const getJobsDisponiveis = async () => {
    try {
        const token = localStorage.getItem('token');
        const result = await api.get("/api/v1/jobs/get-jobs", {
            headers: {
                Authorization: `${token}`
            }
        });
        console.log(result.data);
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
