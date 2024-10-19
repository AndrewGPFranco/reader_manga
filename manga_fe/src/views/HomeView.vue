<script setup lang="ts">
  import NavbarComponent from '@/components/global/NavbarComponent.vue';
</script>

<template>
  <div class="layout">
    <NavbarComponent />
    <main>
      
    </main>
  </div>
</template>

<style scoped>
  .layout {
    display: flex;
    width: 100vw;
    height: 100vh;
  }

  main {
    flex-grow: 1;
    padding: 16px;
    transition: margin-left 0.3s;
  }

  [aria-expanded="true"] + main {
    margin-left: 240px;
  }

  [aria-expanded="false"] + main {
    margin-left: 64px;
  }
</style>