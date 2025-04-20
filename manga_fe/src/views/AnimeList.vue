<template>
  <NavbarComponent />
  <main>
    <AllAnimes />
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import AllAnimes from '@/components/animes/AllAnimes.vue'
</script>

<style scoped>
main {
  padding: 15px;
}
</style>
