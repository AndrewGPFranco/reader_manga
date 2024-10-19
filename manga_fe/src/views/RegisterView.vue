<template>
    <header>
        <NavbarComponent />
    </header>
    <main>
        <n-card title="Records management" style="height: 100%;" size="huge">
            <n-tabs style="height: 100%" type="card">
                <n-tab-pane name="Mangá" tab="Mangá register" style="height: 95%">
                    <section class="container">
                        <!-- <FormToRegister /> -->
                    </section>
                </n-tab-pane>
                <n-tab-pane name="Chapter" tab="Chapter register" style="height: 95%">
                    <section class="container">
                        <FormToChapterRegister />
                    </section>
                </n-tab-pane>
            </n-tabs>
        </n-card>
    </main>
</template>

<script setup lang="ts">
import NavbarComponent from '@/components/global/NavbarComponent.vue';
import FormToChapterRegister from '@/components/registerChapter/formToChapterRegister.vue';
import FormToRegister from '@/components/registerManga/formToRegister.vue';
</script>

<style scoped>
    main {
        padding: 15px;
    }
</style>