<template>
    <div class="container">
        <div class="header">
            <el-page-header @back="router.back()">
                <template #content>
                    管理面板
                </template>
            </el-page-header>
        </div>
        <div class="inner-container">
            <el-tabs v-model="activeName" type="card">
                <el-tab-pane label="用户管理" name="user">
                    用户管理
                </el-tab-pane>
                <el-tab-pane label="留言管理" name="message">
                    留言管理
                </el-tab-pane>
                <el-tab-pane label="举报管理" name="report">
                    举报管理
                </el-tab-pane>
            </el-tabs>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router';
const activeName = ref('user')
const router = useRouter()
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
        width: 100%;
    }
}
</style>