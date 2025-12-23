<template>
    <div class="container">
        <div class="header">
            <el-page-header @back="cancelUpdateHandler">
                <template #content>
                    更新留言
                </template>
            </el-page-header>
        </div>
        <div class="inner-container">
            <el-card>
                <template #header>
                    更新留言
                </template>
                <template #default>
                    <el-form :model="createForm">
                        <el-form-item label="标题">
                            <el-input v-model="createForm.title" placeholder="请输入标题" />
                        </el-form-item>
                        <el-form-item label="内容">
                            <el-input :rows="12" v-model="createForm.content" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                    </el-form>
                </template>
                <template #footer>
                    <el-button type="primary" @click="confirmUpdateHandler">发布</el-button>
                    <el-button @click="cancelUpdateHandler">取消</el-button>
                </template>
            </el-card>
        </div>
    </div>
</template>

<script setup lang="ts">
import type { Message } from '@/core/entity/dbEntities';
import { get, post, put } from '@/core/util';
import { ElMessageBox, ElNotification } from 'element-plus';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

// 定义表单数据类型，title和content可为null，与Message接口保持一致
interface CreateForm {
    title: string | null;
    content: string | null;
}

const createForm = reactive<CreateForm>({
    title: '',
    content: '',
})

const router = useRouter();
const messageId = ref<number | null>(null)
onMounted(async () => {
    messageId.value = Number(router.currentRoute.value.params.id)
    await fetchMessageDetail();
})

const fetchMessageDetail = async () => {
    await get<Message>(`/api/messages/${messageId.value}`).then(res => {
        createForm.title = res.data.title
        createForm.content = res.data.content
    })
}


// 发布留言
const confirmUpdateHandler = () => {
    ElMessageBox.confirm('确定更新么？', '提示', {
    }).then(async () => {
        const title = createForm.title
        const content = createForm.content
        await put(`/api/messages/${messageId.value}`, { title, content }).then(() => {
        }).then(() => {
            ElNotification.success({
                title: '成功',
                message: '更新成功！',
                duration: 2000,
            })
            router.back()
            createForm.title = ''
            createForm.content = ''
        }).catch(() => {
            ElNotification.error({
                title: '错误',
                message: '更新失败！',
                duration: 2000,
            })
        })
    }).catch(() => {
        ElNotification.warning({
            title: '提示',
            message: '操作取消！',
            duration: 2000,
        })
    })
}
// 取消发布
const cancelUpdateHandler = () => {
    // 判断表单是否有内容
    ElMessageBox.confirm('确定关闭么？未提交内容将丢失！', '提示', {
    }).then(() => {
        router.back();
        createForm.title = ''
        createForm.content = ''
        return;
    }).catch(() => {
        return
    })
}
</script>

<style scoped lang="scss">
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
    }
}
</style>