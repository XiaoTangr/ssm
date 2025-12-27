<template>
    <div class="container">
        <div class="header">
            <el-page-header @back="goBackHandler">
                <template #content>
                    留言详情
                </template>
            </el-page-header>
        </div>
        <div class="inner-container">
            <el-card class="message-card">
                <template #header>
                    <div style="display: flex; justify-content: space-between;align-items: center;">
                        <div>
                            <h2>{{ messageData?.title }}</h2>
                            <el-text>发布者：{{ messageData?.creatorId }} </el-text>
                        </div>
                        <div>
                            <el-text> 发布于 {{ formatDateTime(messageData?.createTime ?? 0) }}</el-text><br>
                            <el-text> 更新于 {{ formatDateTime(messageData?.updateTime ?? 0) }}</el-text>
                        </div>
                    </div>
                </template>
                <template #default>
                    <!-- 显示留言内容 -->
                    <el-scrollbar class="message-content">
                        <div v-html="messageData?.content"></div>
                    </el-scrollbar>
                </template>
                <template #footer>
                    <div class="footer">
                        <!-- 显示点赞数 点赞 以及 举报 -->
                        <!-- 如果是当前用户的留言，则显示删除按钮和编辑按钮 -->
                        <div class="liked-container">

                            <el-button class="btn" @click="toggleLikeHandler()" v-if="isLiked" plain
                                :icon="RedHeartIcon" circle />
                            <el-button class="btn" @click="toggleLikeHandler()" v-else plain :icon="HeartIcon" circle />
                            <el-text class="liked-count">
                                {{ messageData?.likeCount }}
                            </el-text>
                        </div>
                        <div class="message-operation">
                            <el-button class="btn" :icon="ChatDotRound" @click="replyHandler(null)" circle />
                            <el-button plain class="btn" :icon="Warning" @click="reportFormVisible = true" circle
                                type="warning" />
                            <el-button @click="updateHandler()" plain class="btn"
                                v-if="messageData?.creatorId == currentUser?.id" type="primary" :icon="Edit" circle />
                            <el-button @click='delMessageHandler' plain class="btn"
                                v-if="messageData?.creatorId === currentUser?.id || currentUser?.role == 1"
                                type="danger" :icon="Delete" circle />
                        </div>
                    </div>
                </template>
            </el-card>
            <div class="replay-container">
                <p style="text-align: center;padding: .5rem;">
                    <el-text>网友回复</el-text>
                </p>

                <!-- 在此展示回复列表，使用el-tree -->
                <el-tree :data="replyDataFormated" :props="treeProps" node-key="id" default-expand-all
                    :expand-on-click-node="false">
                    <template #default="{ node, data }">
                        <div class="tree-body">
                            <el-scrollbar class="replay-item commit">
                                网友 <strong>{{ (data as Reply).creatorId }} </strong>
                                {{ data.parentId == null ? "认为：" : "的看法：" }}
                                {{ (data as Reply).content }}
                            </el-scrollbar>
                            <div class="replay-item operation">
                                <el-button plain class="btn" :icon="ChatDotRound" circle
                                    @click="replyHandler(data.id)" />

                                <el-button @click="delReplayHandler(data.id)" plain class="btn"
                                    v-if="data.creatorId == currentUser?.id" type="danger" :icon="Delete" circle />
                            </div>
                        </div>

                    </template>
                </el-tree>

            </div>

        </div>

        <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" v-model="reportFormVisible"
            title="举报留言">
            <el-form :model="reportForm" label-width="80px">
                <el-form-item label="举报理由">
                    <el-input v-model="reportForm.reason" placeholder="请输入举报理由" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="confirmReportHandler()">确定</el-button>
                <el-button @click="cancelReportHandler()">取消</el-button>
            </template>
        </el-dialog>

        <el-dialog :close-on-click-modal="false" :close-on-press-escape="false" v-model="replayFormVisible"
            title="回复留言">
            <el-form :model="replayForm" label-width="80px">
                <el-form-item label="回复内容">
                    <el-input v-model="replayForm.content" placeholder="请输入回复内容" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="confirmReplayHandler()">确定</el-button>
                <el-button @click="cancelReplayHandler()">取消</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">

import type { Message, Reply } from '@/core/entity/dbEntities';
import { del, formatDateTime, get, post } from '@/core/util';
import { useCurrentUserStore } from '@/stores/currentUserStore';
import { storeToRefs } from 'pinia';
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import { ElDialog, ElMention, ElMessageBox, ElNotification, type TreeNodeData } from 'element-plus'
import { ChatDotRound, Delete, Edit, Warning } from '@element-plus/icons-vue';
import RedHeartIcon from '@/components/icon/RedHeartIcon.vue';
import HeartIcon from '@/components/icon/HeartIcon.vue';
const router = useRouter()

// 获取路径参数
const route = useRoute()
const messageID = ref()

// 留言数据
const messageData = ref<Message>();
// 回复数据
const replyTotal = ref(0);
const replyData = ref<Reply[]>();

const treeNodeClass = (({ }: TreeNodeData) => {
    const classArray: string[] = [];

    classArray.push('all-tree-node')
    return classArray;
});

const treeProps = {
    children: 'children',
    label: 'label',
    class: treeNodeClass
}


// 构建树形数据结构用于显示回复
const replyDataFormated = computed(() => {
    if (!replyData.value || !Array.isArray(replyData.value)) {
        return []
    }
    // 创建一个映射，用于快速查找子回复
    const replyMap: Record<number, any[]> = {}

    // 遍历所有回复，构建映射关系
    replyData.value.forEach(reply => {
        // 初始化当前回复的子回复数组
        if (!replyMap[reply.id!]) {
            replyMap[reply.id!] = []
        }

        // 如果是顶级回复（parentId为null），则直接处理
        if (reply.parentId === null) {
            // 不需要做任何特殊处理，后面会单独处理顶级回复
        } else {
            // 如果是子回复，将其添加到对应父回复的children中
            if (!replyMap[reply.parentId]) {
                replyMap[reply.parentId] = []
            }
            replyMap[reply.parentId]!.push(reply)  // 使用非空断言操作符告诉TypeScript这个值存在
        }
    }, { deep: true })

    // 构建树形结构
    const buildTree = (parentId: number | null): any[] => {
        const replies = replyData.value!
            .filter(reply => reply.parentId === parentId)
            .map(reply => ({
                ...reply,
                children: buildTree(reply.id!)
            }))

        // 确保即使没有子项也返回空数组而不是undefined
        return replies || []
    }

    // 返回顶级回复（parentId为null的回复）
    return buildTree(null)
})

const currentUserStore = useCurrentUserStore()

const { currentUser } = storeToRefs(currentUserStore)


onMounted(async () => {
    messageID.value = route.params.id
    await fetchMessageDetail().then(async () => {
        await fetchReplyDetail();
        await fetchLikeRecord();
    });

})
// 返回
const goBackHandler = () => {
    router.back()
}


// ------------------------更新功能---------------------------------

const updateHandler = () => {
    router.push(`/update/${messageID.value}`);
}

// ------------------------获取数据---------------------------------

// 获取点赞状态
async function fetchLikeRecord() {
    await get(`/api/messages/${messageID.value}/like/status`).then(res => {
        isLiked.value = res.data.isLiked;
    });
}


// 获取留言详情
const fetchMessageDetail = async () => {
    await get<Message>(`/api/messages/${messageID.value}`).then(res => {
        // 检查留言是否存在
        messageData.value = res.data;

    }).catch(async () => {
        ElMessageBox.alert('此留言不存在！', '提示', {
            confirmButtonText: '返回',
            type: 'warning',
        }).finally(() => {
            goBackHandler();
        });
    })
}
// 获取回复列表
const fetchReplyDetail = async () => {
    await get(`/api/messages/${messageID.value}/replies`).then(res => {
        replyData.value = res.data.list
    })
}

// ------------------------点赞相关---------------------------------

const isLiked = ref<boolean>();

// 点赞功能
const toggleLikeHandler = async () => {
    await post(`/api/messages/${messageID.value}/like`,
        { isCancel: isLiked.value === true ? true : false }
    ).then(async (res) => {
        // 刷新点赞状态
        messageData.value!.likeCount = res.data.likeCount
        isLiked.value = !isLiked.value
        // fetchLikeRecord();
    })
}
// ------------------------删除文章相关---------------------------------
const delMessageHandler = () => {
    ElMessageBox.confirm('确定删除此留言么？删除后，此留言将无法恢复！', '提示', {
    }).then(async () => {
        await del(`/api/messages/${messageID.value}`).then(async () => {
            ElNotification.success({
                title: '成功',
            })
            router.push('/')
        })
    }).catch(() => {
        ElNotification.warning({
            title: '提示',
            message: '操作取消！'
        })
    })
}


// ------------------------删除回复相关---------------------------------

const delReplayHandler = (id: number) => {
    ElMessageBox.confirm('确定删除此回复么？删除后，此回复将无法恢复！', '提示', {
    }).then(async () => {
        await del(`/api/replies/${id}`).then(async () => {
            ElNotification.success({
                title: '成功',
            })
            await fetchReplyDetail();
        }).catch(() => {
            ElNotification.error({
                title: '错误',
                message: '删除失败！'
            })
        })
    }).catch(() => {
        ElNotification.warning({
            title: '提示',
            message: '操作取消！'
        })
    })
}



// ------------------------创建举报相关---------------------------------

const reportFormVisible = ref(false)
const reportForm = reactive({ reason: '' })

const confirmReportHandler = () => {
    const messageId = messageData.value?.id
    const reason = reportForm.reason

    if (reportForm.reason.trim() !== '') {
        ElMessageBox.confirm('确定举报么？举报后，管理员将处理！', '提示', {
        }).then(async () => {
            await post('/api/reports', { messageId, reason }).then(() => {
                ElNotification.success({
                    title: '成功',
                    message: '举报成功！'
                })
                reportFormVisible.value = false
                reportForm.reason = ''
            })
        })
    } else {
        ElNotification.error({
            title: '错误',
            message: '请填写举报理由！'
        })
    }

}
const cancelReportHandler = () => {
    if (reportForm.reason.trim() !== '') {
        ElMessageBox.confirm('确定关闭么？未提交的内容将丢失！', '提示', {
        }).then(() => {
            ElNotification.warning({
                title: '提示',
                message: '操作取消！'
            })
            reportFormVisible.value = false
            reportForm.reason = ''

        }).catch(() => {
            return
        })
    } else {
        reportFormVisible.value = false
        reportForm.reason = ''
    }
}

// ------------------------创建回复相关---------------------------------
const replayParentId = ref<number | null>(null)
const replayFormVisible = ref(false)
const replayForm = reactive({ content: '' })

const replyHandler = (parentId: number | null) => {
    replayParentId.value = parentId
    replayFormVisible.value = true
}

const confirmReplayHandler = async () => {
    const messageId = messageData.value?.id
    const parentId = replayParentId.value
    const content = replayForm.content

    if (replayForm.content.trim() !== '') {
        await post('/api/replies', { messageId, parentId, content }).then(async () => {
            ElNotification.success({
                title: '成功',
                message: '回复成功！'
            })
            replayFormVisible.value = false
            replayForm.content = ''
            replayParentId.value = null
            await fetchReplyDetail();
        })
    }

}
const cancelReplayHandler = () => {
    if (replayForm.content.trim() !== '') {
        ElMessageBox.confirm('确定关闭么？未提交的内容将丢失！', '提示', {
        }).then(() => {
            replayFormVisible.value = false
            replayForm.content = ''
        }).catch(() => {
            // 取消操作
            return
        })
    }
    replayFormVisible.value = false
    replayParentId.value = null
}

</script>

<style lang="scss" scoped>
.container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;

    .header {
        z-index: 99;
        position: sticky;
        top: 0;
        backdrop-filter: blur(1rem);
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        background-color: rgba(255, 255, 255, 0.5);
        padding: 1rem;
        width: 100%;
    }

    .inner-container {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        min-width: 600px;
        width: 700px;
        max-width: 800px;


        .message-card {
            .message-content {
                height: 24rem;
            }

            .footer {
                display: flex;
                width: 100%;

                .liked-container {
                    display: flex;
                    gap: .5rem;
                    border-radius: 100vw;
                    background: rgba(225, 185, 185, 0.5);
                    padding-right: 0.5rem;
                }

                .message-operation {
                    display: flex;
                    flex: 1;
                    justify-content: end;
                    align-items: center;
                }
            }
        }

        .replay-container {
            width: 100%;
            overflow: hidden;
        }
    }
}
</style>
<style lang="scss">
.all-tree-node {
    min-width: 600px;
    width: 700px;
    max-width: 800px;



    .el-tree-node__content {
        height: 3rem;
        padding: 0.5rem;
        border-radius: 100vw;
        width: 100%;

        .tree-body {

            width: 100%;
            display: flex;
            align-items: center;
            gap: 1rem;
            overflow-y: hidden;
            overflow-x: auto;

            .commit {
                flex: 1;
            }

            .operation {
                position: sticky;
                right: 0;
                display: flex;
                justify-content: end;
                align-items: center;

                .btn {
                    height: 2rem;
                    width: 2rem;
                }
            }
        }





    }
}
</style>