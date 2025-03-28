<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card
      class="containerCard"
      title="Leituras em andamento"
      size="huge"
      style="height: 95vh; overflow-y: auto"
    >
    </n-card>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import { NCard } from 'naive-ui'
import { onMounted } from 'vue';

onMounted(() => document.title = 'Leitor de mangás - Leituras em andamento');
</script>

<style scoped>

main {
  padding: 15px;
}

</style>
