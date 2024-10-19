<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <formToRegister />
    </main>
</template>

<script setup lang="ts">
    import formToRegister from '@/components/registerManga/formToRegister.vue';
    import NavbarComponent from '@/components/global/NavbarComponent.vue';
</script>

<style scoped>
    main {
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>