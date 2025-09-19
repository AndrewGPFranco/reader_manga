<script setup lang="ts">
import MenuComponent from '@/components/global/MenuComponent.vue'
import HomeComponent from '@/components/home/HomeComponent.vue'
import { onMounted } from 'vue';

onMounted(() => document.title = 'Leitor de mangás')
</script>

<template>
  <header>
    <MenuComponent />
  </header>
  <main>
    <HomeComponent />
  </main>
</template>
