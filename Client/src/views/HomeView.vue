<template>
    <div class="container">
        <div class="inner-container">
            <div class="search-container">
                <el-input class="search" @change="searchChangeHandler" placeholder="请输入内容" :suffix-icon="Search"
                    v-model="searchContent"></el-input>
            </div>
            <div class="main-container">
                <div class="list-container">
                    <el-card class="message-item-card" v-for="item in messageList">
                        <template #header>
                            {{ item.title }}
                        </template>
                        <template #default>
                            <!-- 展示留言内容前30个字 -->
                            <el-text>
                                <el-text truncated>{{ getContent(item.content ?? "") }}</el-text>
                            </el-text>
                        </template>
                        <template #footer>
                            <div class="message-item-footer">
                                <div class="star-num">
                                    <el-text v-if="item.likeCount && item.likeCount > 0">
                                        共 {{ item.likeCount }} 人点赞
                                    </el-text>
                                </div>
                                <el-button type="primary" @click="toDetailHandler(item.id)"
                                    :disabled="item.id === null">查看详情</el-button>
                            </div>
                        </template>
                    </el-card>
                </div>
                <div class="userinfo-container">
                    <el-card>
                        <template #header>
                            欢迎用户：{{ currentUser?.nickname }}
                        </template>
                        <template #default>
                            <p> UID: {{ currentUser?.id }} </p>
                            <p> 邮箱: {{ currentUser?.email }} </p>
                            <p> 角色: {{ userRole }} </p>
                            <p> 状态: {{ userStatus }} </p>
                        </template>
                        <template #footer>
                            <div class="userinfo-footer">
                                <el-button type="primary" @click="logoutHandler()">更新信息</el-button>
                                <el-button type="danger" @click="logoutHandler()">退出登录</el-button>
                            </div>
                        </template>
                    </el-card>
                </div>

            </div>
            <div class="pagination-container">
                <div class="pagination">
                    <el-pagination class="pagination" @change="paginationChangHandler"
                        v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[5, 10, 20, 50, 100]"
                        layout=" prev, pager, next,total,sizes" :total="messageTotal" />
                </div>
            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import HeartIcon from '@/components/icon/HeartIcon.vue';
import type { Message } from '@/core/entity/dbEntities';
import { get } from '@/core/util';
import { useCurrentUserStore } from '@/stores/currentUserStore';
import { Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const currentUserStore = useCurrentUserStore();
const { currentUser, userRole, userStatus } = storeToRefs(currentUserStore);

const messageList = ref<Message[] | null>(null);
const messageTotal = ref<number | null>(null);
const pageSize = ref<number>(10);
const pageNum = ref<number>(1);

// 将函数改为计算属性
const getContent = computed(() => (message: string) => {
    const size = 50;
    if (!message) return '';
    if (message.length <= size) return message;
    if (message.length > size) message = message.substring(0, size - 3) + '...';
    return message;
});



const searchContent = ref<string | null>(null);

const fetchMessageList = async () => {
    const url = `/api/messages?pageSize=${pageSize.value}&pageNum=${pageNum.value}`
    await get(url).then(res => {
        messageList.value = res.data.list;
        messageTotal.value = res.data.total;
    });
};


const searchChangeHandler = async () => {
    // 字符串需要处理编码
    const str = encodeURIComponent(searchContent.value ?? '');

    const url = `/api/messages?pageSize=${pageSize.value}&pageNum=${pageNum.value}&keyword=${str}`
    await get(url).then(res => {
        messageList.value = res.data.list;
        messageTotal.value = res.data.total;
    });
};

const paginationChangHandler = async () => {
    await fetchMessageList();
};

const toDetailHandler = (id: number | null) => {
    if (id === null) {
        // TODO: 导航到新建页面
        router.push('/message/create');
    } else {
        router.push(`/message/${id}`);
    }
    // TODO: 实现查看详情逻辑
};


onMounted(async () => {
    await fetchMessageList();
})



const router = useRouter();
const logoutHandler = () => {
    currentUserStore.logout();
    ElMessage.success('退出登录成功');
    router.push('/login');
};

</script>

<style scoped lang="scss">
@import '@/core/styles/variable.scss';

.container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    padding: 1rem;

    .inner-container {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        width: 900px;

        .main-container,
        .search-container,
        .pagination-container {
            width: 900px;
        }


        .search,
        .pagination {
            box-shadow: $box-shadow-light ;
        }

        .search-container {
            position: sticky;
            top: 1rem;
            display: flex;
            justify-content: center;
            align-items: center;

            .search {
                width: 50%;
            }
        }

        .pagination-container {
            display: flex;
            position: sticky;
            bottom: 1rem;
            // 居中
            justify-content: center;
            align-items: center;

            .pagination {
                background: $background-color-white;
            }
        }

        .main-container {
            display: flex;
            gap: 1rem;

            .list-container {
                flex: 1;
                max-width: 600px;
                display: flex;
                flex-direction: column;
                gap: 1rem;

                .message-item-card {
                    width: 100%;


                    .message-item-footer {
                        width: 100%;
                        // 两端对齐
                        justify-content: space-between;
                        align-items: center;
                        display: flex;
                        gap: 1rem;
                    }
                }
            }

            .userinfo-container {
                width: 300px;
            }
        }
    }
}


.userinfo-footer {
    display: flex;
    gap: 1rem;

    .el-button {
        flex: 1;
    }
}
</style>