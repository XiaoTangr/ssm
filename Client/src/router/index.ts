import { useCurrentUserStore } from '@/stores/currentUserStore'
import { ElMessage } from 'element-plus'
import { storeToRefs } from 'pinia'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue')
    },
    {
      path: '/message/new',
      name: 'message-new',
      component: () => import('@/views/message/NewView.vue')
    },
    {
      path: '/message/:id',
      name: 'message-detail',
      component: () => import('@/views/message/DetailView.vue')
    },
  ],
})

// 检查登录状态
// 需要确保Store加载完成
router.beforeEach(async (to, from, next) => {
  // 如果是登录页面或注册页面，直接放行
  if (to.name === 'login' || to.name === 'register') {
    next()
    return;
  }

  const currentUserStore = useCurrentUserStore()

  const { isLogined } = storeToRefs(currentUserStore)


  if (isLogined.value === false) {
    await currentUserStore.fetchData()
  }

  if (isLogined.value !== true || currentUserStore.currentUser === null) {
    ElMessage.error('请先登录')
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router