<template>
  <MenuComponent />
  <main>
    <AllAnimes />
  </main>
</template>

<script setup lang="ts">
import MenuComponent from '@/components/global/MenuComponent.vue'
import AllAnimes from '@/components/anime/AllAnimes.vue'
</script>

<style scoped>
main {
  padding: 15px;
}
</style>
