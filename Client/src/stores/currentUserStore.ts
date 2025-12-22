import { ref, onMounted, computed } from 'vue'
import { defineStore } from 'pinia'
import type { User } from '@/core/entity/dbEntities'
import { get, post } from '@/core/util/http/request'

export const useCurrentUserStore = defineStore('currentUserStore', () => {

  const currentUser = ref<User | null>(null)
  const isLogined = ref(false)

  const userRole = computed(() => {
    if (currentUser.value) {
      if (currentUser.value.role === 0) {
        return '普通用户'
      } else if (currentUser.value.role === 1) {
        return '管理员'
      }
    }
    return null
  })

  const userStatus = computed(() => {
    if (currentUser.value) {

      if (currentUser.value.status === 0) {
        return '正常'
      } else if (currentUser.value.status === -1) {
        return '停用'
      }
    }
    return null
  })


  // 尝试获取通过token当前用户信息
  const fetchData = async () => {
    await get<User>('/api/users/current').then((res) => {
      if (res.code === 200) {
        currentUser.value = res.data;
        isLogined.value = true;
      } else {
        localStorage.removeItem('token')
        currentUser.value = null;
        isLogined.value = false;
      }
    }).catch(() => {
      localStorage.removeItem('token')
      currentUser.value = null;
      isLogined.value = false;
    })
  }

  const logout = () => {
    localStorage.removeItem('token')
    currentUser.value = null;
  }
  const login = async (email: string, password: string): Promise<number | string> => {
    return await post<User>('/api/users/login',
      { email, password },
      { withToken: false })
      .then((res) => {
        if (res.code === 200) {
          currentUser.value = res.data;
          isLogined.value = true;
          return 0
        }
        return res.message
      })
      .catch((res) => {
        return res.message
      })
  }


  onMounted(async () => {
    await fetchData();
  });
  return { currentUser, isLogined, userRole, userStatus, fetchData, logout, login }
})