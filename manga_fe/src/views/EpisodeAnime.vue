<template>
  <NavbarComponent />
  <main>
    <n-card size="huge" style="height: 95vh; overflow-y: auto">
      <VideoEpisodeAnime />
    </n-card>
  </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue'
import VideoEpisodeAnime from '@/components/animes/VideoEpisodeAnime.vue'
import { NCard } from 'naive-ui'
</script>

<style scoped>
main {
  padding: 15px;
}
</style>
