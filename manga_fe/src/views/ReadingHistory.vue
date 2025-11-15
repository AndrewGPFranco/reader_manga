<template>
  <header>
    <MenuComponent/>
  </header>
  <main>
    <TimelineHistory />
  </main>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useChapterStore } from '@/store/ChapterStore'
import MenuComponent from '@/components/global/MenuComponent.vue'
import TimelineHistory from '@/components/history/TimelineHistory.vue'

const chapterStore = useChapterStore();

async function getAllHistorico() {
  return await chapterStore.getAllHistorico();
}

onMounted(async() => {
  const result = await getAllHistorico()
  console.log(result.content);
})
</script>
