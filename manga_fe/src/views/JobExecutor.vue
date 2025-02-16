<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Jobs" size="huge">

        </n-card>
    </main>
</template>

<script setup lang="ts">

import NavbarComponent from '@/components/global/NavbarComponent.vue';

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
</style>