<template>
  <n-layout-sider
      bordered
      collapse-mode="width"
      :collapsed="collapsed"
      @collapse="handleCollapse"
      @expand="handleExpand"
      position="absolute"
      top="0"
      left="0"
      height="100%"
      :aria-expanded="!collapsed"
      class="pt-6 pb-6"
      show-trigger
  >
    <n-menu
        :collapsed="collapsed"
        :collapsed-icon-size="20"
        :options="menuOptions"
        key-field="whateverKey"
        label-field="whateverLabel"
        children-field="whateverChildren"
        v-model:value="activeKey"
    />
  </n-layout-sider>

  <NotificationsModal v-model:isShowNotifications="isShowNotifications" />
</template>

<script lang="ts">
import type {Component} from 'vue'
import {defineComponent, h, onMounted, onUnmounted, ref, watch, nextTick} from 'vue'
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
  SettingsOutline,
  NotificationsOutline as Notification
} from '@vicons/ionicons5'
import {RouterLink} from 'vue-router'
import {useAuthStore} from '@/store/AuthStore'
import {useSystemStore} from '@/store/SystemStore'
import {VideocamOutline} from '@vicons/ionicons5/lib'
import {useMenu} from '@/composables/menu'
import NotificationsModal from "@/components/global/NotificationsModal.vue";

const renderIcon = (icon: Component) => () => h(NIcon, null, {default: () => h(icon)})

export default defineComponent({
  components: {NotificationsModal},
  setup() {
    const auth = useAuthStore()
    const systemStore = useSystemStore()
    const {menuCollapsed, setMenuCollapsed} = useMenu()

    const isShowNotifications = ref<boolean>(false);

    const role = auth.getRoleUser()
    const collapsed = ref<boolean>(menuCollapsed.value)
    const activeKey = ref<string | null>(null)
    const isInitialized = ref<boolean>(false)

    const updateMenuState = (newState: boolean) => {
      collapsed.value = newState
      setMenuCollapsed(newState)
    }

    const handleCollapse = () => {
      if (isInitialized.value) {
        updateMenuState(true)
      }
    }

    const handleExpand = () => {
      if (isInitialized.value) {
        updateMenuState(false)
      }
    }

    watch(
        menuCollapsed,
        (newValue) => {
          if (!isInitialized.value) {
            collapsed.value = newValue
          }
        },
        {immediate: true}
    )

    const execKey = (e: KeyboardEvent) => {
      if (e.altKey && e.key.toLowerCase() === 'q') updateMenuState(!collapsed.value)
      else if (e.altKey && e.key.toLowerCase() === 'a') systemStore.alterTheme()
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
              whateverLabel: () => h(RouterLink, {to: '/admin/jobs'}, {default: () => 'Jobs'}),
              icon: renderIcon(Jobs),
              path: '/admin/jobs'
            }
          ]
          : []),
      {
        whateverKey: 'anime',
        whateverLabel: () => h(RouterLink, {to: '/anime'}, {default: () => 'Animes'}),
        icon: renderIcon(VideocamOutline),
        path: '/anime'
      },
      {
        whateverKey: 'mangas',
        whateverLabel: () => h(RouterLink, {to: '/mangas'}, {default: () => 'Mangás'}),
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
        whateverLabel: () => h(RouterLink, {to: '/profile'}, {default: () => 'Meu perfil'}),
        icon: renderIcon(Profile),
        path: '/profile'
      },
      {
        key: 'notifications',
        label: 'Notifications',
        whateverLabel: () => "Notificações",
        icon: renderIcon(Notification),
        onClick: () => {
          isShowNotifications.value = !isShowNotifications.value
        }
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
                  default: () => (systemStore.theme === null ? 'Ativar Dark Mode' : 'Ativar Light Mode')
                }
            ),
        icon: () => h(NIcon, null, {default: () => h(SettingsOutline)})
      }
    ]

    onMounted(async () => {
      globalThis.addEventListener('keydown', execKey)
      await nextTick()
      collapsed.value = menuCollapsed.value
      isInitialized.value = true
    })
    onUnmounted(() => globalThis.removeEventListener('keydown', execKey))

    return {menuOptions, collapsed, activeKey, updateMenuState, handleCollapse, handleExpand, isShowNotifications}
  }
})
</script>
