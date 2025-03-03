<template>
    <div class="home">
      <n-card>
        <div class="banner">
          <img src="../../assets/home/banner.jpeg" alt="Banner do site">
        </div>
        <h2 class="section-title">Mangás famosos</h2>

        <div class="favorites-grid" v-if="mangas.length != 0">
            <div v-for="(item, index) in mangas" :key="index" class="item-card">
              <a :href="item.urlReader" target="_blank"><img :src="item.urlImage" alt="Capa de mangá"></a>
            </div>
        </div>
      </n-card>
    </div>
</template>
  
<script setup lang="ts">
import type iCoversManga from '@/@types/iCoversManga';
import { useMangaStore } from '@/store/MangaStore';
import { useMessage } from 'naive-ui';
import { onMounted, ref } from 'vue';
  
const mangas = ref<iCoversManga[]>([]);
const message = useMessage();
const mangaStore = useMangaStore();

onMounted(() => getMangaRandom());

const getMangaRandom = async () => {
  try {
    mangas.value = await mangaStore.getFiveMangaRandom();
  } catch (error) {
    message.error("Error loading sleeves.");
  }
};
</script>
  
<style scoped>
  .home {
    padding: 1rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .banner img {
    width: 100%;
    height: 400px;
    border-radius: 5px;
  }
  
  .section-title {
    font-size: 1.2rem;
    font-weight: bold;
    margin-top: 1rem;
  }

  .favorites-grid {
    display: flex;
    justify-content: center;
    padding: 20px;
    flex-wrap: wrap;
    gap: 20px;
  }

  .item-card img {
    height: 250px;
    border-radius: 5px;
  }

  .n-card {
    height: 95vh;
    box-sizing: border-box;
    overflow: scroll;
    overflow-x: hidden;
  }
</style>  