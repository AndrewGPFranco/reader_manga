<template>
  <div class="history">
    <n-timeline horizontal>
      <n-timeline-item content="Oops" />
      <n-timeline-item
        type="success"
        title="Success"
        content="Success content"
        time="2018-04-03 20:46"
      />
      <n-timeline-item type="error" content="Error content" time="2018-04-03 20:46" />
      <n-timeline-item
        type="warning"
        title="Warning"
        content="Warning content"
        time="2018-04-03 20:46"
      />
      <n-timeline-item type="info" title="Info" content="Info content" time="2018-04-03 20:46" />
    </n-timeline>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped>
.history {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
