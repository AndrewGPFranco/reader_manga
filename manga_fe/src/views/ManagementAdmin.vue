<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="General Management" size="huge">
            <n-tabs style="height: 100%" type="card">
                <n-tab-pane name="Mangá Management" tab="Mangá Management" style="height: 95%">
                    <section class="container">
                        <MangaManagement />
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Chapter Management" tab="Chapter Management" style="height: 95%">
                    <section class="container">
                        <ChapterManagement />
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Pages Management" tab="Pages Management" style="height: 95%">
                    <section class="container">
                        <ChapterPagesManagement />
                    </section>
                </n-tab-pane>
            </n-tabs>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import ChapterManagement from '@/components/admin/ChapterManagement.vue';
import ChapterPagesManagement from '@/components/admin/ChapterPagesManagement.vue';
import MangaManagement from '@/components/admin/MangaManagement.vue';
import NavbarComponent from '@/components/global/NavbarComponent.vue';

</script>

<style scoped>
    main {
        padding: 15px;
    }

    .n-card {
        height: 95vh;
        box-sizing: border-box;
        overflow: scroll;
        overflow-x: hidden;
    }
</style>