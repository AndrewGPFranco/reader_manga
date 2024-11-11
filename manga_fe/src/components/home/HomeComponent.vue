<template>
    <div class="home">
        <div class="banner"></div>

        <h2 class="section-title">Favorites mangás</h2>
        <div class="favorites-grid">
            <div v-for="(item, index) in favoriteMangas" :key="index" class="item-card"></div>
        </div>

        <h2 class="section-title">Wish List</h2>
        <div class="wishlist-grid">
            <div v-for="(item, index) in wishList" :key="index" class="item-card"></div>
        </div>
    </div>
</template>
  
<script setup lang="ts">
import { ref } from 'vue';
  
const favoriteMangas = ref(new Array(5).fill({}));
const wishList = ref(new Array(5).fill({}));
  
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
  
  .favorites-grid,
  .wishlist-grid {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 10px;
  }
  
  .item-card {
    width: 15vw;
    height: 20vh;
    background-color: #bbb;
    border-radius: 8px;
  }
</style>  