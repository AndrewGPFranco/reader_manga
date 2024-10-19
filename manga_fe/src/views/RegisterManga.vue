<template>
    <NavbarComponent />
    <main class="flex justify-center p-5 bg-orange-400">
        <formToRegister />
    </main>
</template>

<script setup lang="ts">
    import formToRegister from '@/components/registerManga/formToRegister.vue';
    import NavbarComponent from '@/components/global/NavbarComponent.vue';
</script>

<style scoped>
    main {
        max-height: 100vh;
    }
</style>