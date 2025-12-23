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
      path: '/admin',
      name: '/admin',
      component: () => import('@/views/AdminView.vue')
    },
    {
      path: '/user/login',
      name: 'login',
      component: () => import('@/views/user/LoginView.vue')
    },
    {
      path: '/user/register',
      name: 'register',
      component: () => import('@/views/user/RegisterView.vue')
    },
    {
      path: '/user/reset',
      name: 'reset',
      component: () => import('@/views/user/ResetView.vue')
    },
    {
      path: '/create',
      name: 'message-create',
      component: () => import('@/views/message/NewView.vue')
    },
    {
      path: '/update/:id',
      name: 'message-update',
      component: () => import('@/views/message/UpdateView.vue')
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
  if (to.name === 'login' || to.name === 'register' || to.name === 'reset') {
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