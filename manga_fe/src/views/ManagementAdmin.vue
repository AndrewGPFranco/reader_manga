<template>
  <header>
    <NavbarComponent />
  </header>
  <main>
    <n-card title="Centro de Gerenciamento" size="huge" style="height: 95vh; overflow-y: auto;">
      <n-tabs style="height: 100%" type="card">
        <n-tab-pane name="Gerenciar mangás" tab="Gerenciar mangás" style="height: 95%">
          <section class="container">
            <MangaManagement />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Gerenciar capítulos" tab="Gerenciar capítulos" style="height: 95%">
          <section class="container">
            <ChapterManagement />
          </section>
        </n-tab-pane>
        <n-tab-pane name="Gerenciar páginas" tab="Gerenciar páginas" style="height: 95%">
          <section class="container">
            <ChapterPagesManagement />
          </section>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </main>
</template>

<script setup lang="ts">
import ChapterManagement from '@/components/admin/ChapterManagement.vue'
import ChapterPagesManagement from '@/components/admin/ChapterPagesManagement.vue'
import MangaManagement from '@/components/admin/MangaManagement.vue'
import NavbarComponent from '@/components/global/NavbarComponent.vue'
</script>

<style scoped>
main {
  padding: 15px;
}
</style>
