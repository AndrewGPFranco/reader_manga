<template>
    <div class="home">
      <n-card>
        <div class="banner"></div>
        <h2 class="section-title">Famous Mangá</h2>

        <div class="favorites-grid" v-if="mangas.length != 0">
            <div v-for="(item, index) in mangas" :key="index" class="item-card">
              <img :src="item" alt="Capa de mangá">
            </div>
        </div>
      </n-card>
    </div>
</template>
  
<script setup lang="ts">
import { useMangaStore } from '@/store/MangaStore';
import { useMessage } from 'naive-ui';
import { onMounted, ref } from 'vue';
  
const mangas = ref<string[]>([]);
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
  
  .banner {
    width: 100%;
    height: 30vh;
    background-image: url("../../assets/home/bannerHome.jpeg");
    border-radius: 8px;
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