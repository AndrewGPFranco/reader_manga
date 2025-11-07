<template>
  <n-layout-sider bordered collapse-mode="width" :collapsed="collapsed" @collapse="updateMenuState(true)"
                  @expand="updateMenuState(false)"
                  position="absolute" top="0" left="0" height="100%" :aria-expanded="!collapsed" class="pt-6 pb-6"
                  show-trigger>
    <n-menu :collapsed="collapsed" :collapsed-icon-size="20" :options="menuOptions" key-field="whateverKey"
            label-field="whateverLabel" children-field="whateverChildren" v-model:value="activeKey"/>
  </n-layout-sider>
</template>

<script lang="ts">
import type {Component} from 'vue'
import {defineComponent, h, onMounted, onUnmounted, ref} from 'vue'
import {NButton, NIcon} from 'naive-ui'
import type {MenuOption} from 'naive-ui'
import {
  ScanCircleOutline as ScanCircle,
  BookOutline as Library,
  GridOutline as New,
  SettingsOutline as Management,
  Bookmark as Favorites,
  PersonOutline as Profile,
  NewspaperOutline as Mangas,
  CodeWorkingOutline as Jobs,
  HourglassOutline as ProgressReadings,
  SettingsOutline
} from '@vicons/ionicons5'
import {RouterLink} from 'vue-router'
import {useAuthStore} from '@/store/AuthStore'
import {useSystemStore} from '@/store/SystemStore'
import {VideocamOutline} from '@vicons/ionicons5/lib'
import {useMenu} from '@/composables/menu'

const renderIcon = (icon: Component) => () =>
    h(NIcon, null, {default: () => h(icon)})

export default defineComponent({
  setup() {
    const auth = useAuthStore()
    const systemStore = useSystemStore()
    const {setMenuCollapsed} = useMenu()

    const role = auth.getRoleUser()
    const collapsed = ref<boolean>(true)
    const activeKey = ref<string | null>(null)

    const updateMenuState = (newState: boolean) => {
      collapsed.value = newState
      setMenuCollapsed(newState)
    }

    const execKey = (e: KeyboardEvent) => {
      if (e.altKey && e.key.toLowerCase() === 'q') {
        updateMenuState(!collapsed.value)
      }
    }

    const menuOptions: MenuOption[] = [
      {
        whateverKey: 'inicio',
        whateverLabel: () => h(RouterLink, {to: '/'}, {default: () => 'Inicio'}),
        icon: renderIcon(ScanCircle),
        path: '/'
      },
      ...(role.includes('ADMIN')
          ? [
            {
              whateverKey: 'jobs',
              whateverLabel: () =>
                  h(RouterLink, {to: '/admin/jobs'}, {default: () => 'Jobs'}),
              icon: renderIcon(Jobs),
              path: '/admin/jobs'
            }
          ]
          : []),
      {
        whateverKey: 'anime',
        whateverLabel: () =>
            h(RouterLink, {to: '/anime'}, {default: () => 'Animes'}),
        icon: renderIcon(VideocamOutline),
        path: '/anime'
      },
      {
        whateverKey: 'mangas',
        whateverLabel: () =>
            h(RouterLink, {to: '/mangas'}, {default: () => 'Mangás'}),
        icon: renderIcon(Mangas),
        path: '/mangas'
      },
      ...(role.includes('ADMIN')
          ? [
            {
              whateverKey: 'registros',
              whateverLabel: () =>
                  h(RouterLink, {to: '/register'}, {default: () => 'Registros'}),
              icon: renderIcon(New),
              path: '/register'
            }
          ]
          : []),
      {
        whateverKey: 'perfil',
        whateverLabel: () =>
            h(RouterLink, {to: '/profile'}, {default: () => 'Meu perfil'}),
        icon: renderIcon(Profile),
        path: '/profile'
      },
      ...(role.includes('ADMIN')
          ? [
            {
              whateverKey: 'gerenciamento',
              whateverLabel: () =>
                  h(RouterLink, {to: '/management/admin'}, {default: () => 'Gerenciamento'}),
              icon: renderIcon(Management),
              path: '/management/admin'
            }
          ]
          : []),
      {
        whateverKey: 'biblioteca',
        whateverLabel: () =>
            h(RouterLink, {to: '/manga/all'}, {default: () => 'Minha biblioteca'}),
        icon: renderIcon(Library),
        path: '/manga/all'
      },
      {
        whateverKey: 'favoritos',
        whateverLabel: () =>
            h(RouterLink, {to: '/manga/favorites'}, {default: () => 'Mangás favoritos'}),
        icon: renderIcon(Favorites),
        path: '/manga/favorites'
      },
      {
        whateverKey: 'leituras-em-andamento',
        whateverLabel: () =>
            h(RouterLink, {to: '/progress-readings'}, {
              default: () => 'Leituras em andamento'
            }),
        icon: renderIcon(ProgressReadings),
        path: '/progress-readings'
      },
      {
        whateverKey: 'alterar-tema',
        whateverLabel: () =>
            h(
                NButton,
                {
                  type: 'primary',
                  tertiary: systemStore.theme === null,
                  onClick: () => systemStore.alterTheme(),
                  style: {width: '100%', justifyContent: 'flex-start'}
                },
                {
                  default: () =>
                      systemStore.theme === null
                          ? 'Ativar Dark Mode'
                          : 'Ativar Light Mode'
                }
            ),
        icon: () => h(NIcon, null, {default: () => h(SettingsOutline)})
      }
    ]

    onMounted(() => {
      globalThis.addEventListener('keydown', execKey)
      setMenuCollapsed(collapsed.value)
    })
    onUnmounted(() => globalThis.removeEventListener('keydown', execKey))

    return {menuOptions, collapsed, activeKey, updateMenuState}
  }
})
</script>