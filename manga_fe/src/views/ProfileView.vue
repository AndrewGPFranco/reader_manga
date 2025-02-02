<template>
    <header>
        <NavbarComponent />
    </header>
    <main class="text-black bg-white p-10 shadow-lg max-h-screen overflow-y-auto">
        <n-card>
            <section class="max-w-md mx-auto p-6 bg-white shadow-md rounded-2xl">
                <div class="flex flex-col items-center mb-6">
                    <img src="../assets/home/op.jpeg" alt="Foto de Perfil" class="w-32 h-32 object-cover rounded-full shadow-lg" />
                    <h2 class="text-2xl font-semibold mt-4">{{ name }}</h2>
                    <p class="text-gray-500">{{ email }}</p>
                    <p class="text-gray-500">Nickname: {{ username }}</p>
                    <p class="text-gray-500">Data de Nascimento: {{ dateBirth }}</p>
                </div>

                <div class="flex justify-between mt-4">
                    <button @click="efetuarLogout" class="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600">
                        Logout
                    </button>
                    <button @click="changePassword"
                        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600">
                        Alterar Senha
                    </button>
                </div>
            </section>
        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import router from '@/router';
import { useAuthStore } from '@/store/AuthStore';
import { onMounted, ref } from 'vue';

const auth = useAuthStore();
const name = ref<string>("");
const email = ref<string>("");
const username = ref<string>("");
const dateBirth = ref<Date | null>(null);

const efetuarLogout = () => {
    auth.efetuarLogout();
    router.push({ name: 'login' });
}

const changePassword = () => {
    alert('Redirecionando para alterar a senha...');
};

onMounted(async () => {
    const result = await auth.getUser();
    name.value = result.firstName;
    email.value = result.email;
    dateBirth.value = result.dateBirth;
    username.value = result.username;
});

</script>

<style scoped>
main {
    padding: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.n-card {
    height: 95vh;
    box-sizing: border-box;
    overflow: scroll;
    overflow-x: hidden;
}

section {
    background-color: #f9fafb;
    color: #333;
}

button {
    transition: background-color 0.3s ease;
}
</style>