<template>
    <header>
        <NavbarComponent />
    </header>
    <main class="text-black bg-white p-10 shadow-lg max-h-screen overflow-y-auto">
        <n-card>
            <button @click="efetuarLogout">Logout</button>
        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';
import router from '@/router';
import { useAuthStore } from '@/store/AuthStore';

const authStore = useAuthStore();

const efetuarLogout = () => {
    authStore.efetuarLogout();
    router.push({ name: 'login' });
}

</script>

<style scoped>
   main {
      padding: 15px;
   }
   
   .n-card {
        height: 95vh;
        box-sizing: border-box;
        overflow: scroll;
        overflow-x: hidden;
    }
</style>