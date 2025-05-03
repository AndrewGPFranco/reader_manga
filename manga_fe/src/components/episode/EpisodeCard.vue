<template>
  <article class="episode-card" @click="onClick">
    <div class="play-overlay">
      <n-icon size="36">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path fill="currentColor" d="M8 5v14l11-7z" />
        </svg>
      </n-icon>
    </div>
    <div class="episode-info">
      <span class="episode-number">EP {{ episode.numberEpisode }}</span>
      <h3 class="episode-title">{{ episode.titleEpisode }}</h3>
    </div>
  </article>
</template>

<script setup lang="ts">
import { NIcon } from 'naive-ui'
import type { EpisodeToAnimesVO } from '@/@types/iEpisodeToAnimesVO';

const props = defineProps<{
  episode: EpisodeToAnimesVO
}>()

const emit = defineEmits<(e: 'play', episode: EpisodeToAnimesVO) => void>()

const onClick = () => emit('play', props.episode)
</script>

<style scoped>
.episode-card {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  background-color: #fff;
  transition: transform 0.2s ease;
}

.episode-card:hover {
  transform: translateY(-4px);
}

.play-overlay {
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.episode-card:hover .play-overlay {
  opacity: 1;
}

.episode-info {
  padding: 12px;
  z-index: 1;
}

.episode-number {
  display: inline-block;
  background-color: #ff5722;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  margin-bottom: 6px;
}

.episode-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
