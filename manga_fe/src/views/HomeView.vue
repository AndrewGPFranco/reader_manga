<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import HomeComponent from '@/components/home/HomeComponent.vue'
import { onMounted } from 'vue';

onMounted(() => document.title = 'Leitor de mangás')
</script>

<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <HomeComponent />
  </main>
</template>
