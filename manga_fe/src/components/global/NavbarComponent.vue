<template>
  <n-layout-sider bordered collapse-mode="width" :collapsed-width="250" width="17vw"
    position="absolute" top="0" left="0" height="100%" :aria-expanded="true">
    <n-menu :collapsed-width="64" :collapsed-icon-size="30" :options="menuOptions"
      key-field="whateverKey" label-field="whateverLabel" children-field="whateverChildren" />
  </n-layout-sider>
</template>

<script lang="ts">
import type { Component } from 'vue'
import { defineComponent, h } from 'vue'
import { NIcon } from 'naive-ui'
import type { MenuOption } from 'naive-ui'
import { ScanCircleOutline as ScanCircle, BookOutline as Library, GridOutline as NewChapter } from '@vicons/ionicons5'
import { RouterLink } from 'vue-router'

function renderIcon(icon: Component) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions: MenuOption[] = [
{
    whateverLabel: () =>
      h(RouterLink, { to: '/' }, { default: () => 'Home' }),
    icon: renderIcon(ScanCircle),
    path: '/'
  },
  {
    whateverLabel: () =>
      h(RouterLink, { to: '/register' }, { default: () => 'Records' }),
    icon: renderIcon(NewChapter),
    path: '/register/chapter'
  },
  {
    whateverLabel: () =>
      h(RouterLink, { to: '/manga/all' }, { default: () => 'My Library' }),
    icon: renderIcon(Library),
    path: '/manga/all'
  }
]

export default defineComponent({
  setup() {
    return {
      menuOptions
    }
  }
})
</script>