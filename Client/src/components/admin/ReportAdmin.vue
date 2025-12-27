<template>
    <div>
        <el-table :data="reportData" style="width: 100%">
            <el-table-column prop="id" label="id" width="180" />
            <el-table-column prop="userId" label="提交用户" width="180" />
            <el-table-column label="文章id">
                <template #default="scope">
                    <el-link :href="`/message/${scope.row.messageId}`">{{ scope.row.messageId }}</el-link>
                </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" />
            <el-table-column label="当前状态" width="180">
                <template #default="scope">
                    <el-tag v-if="scope.row.auditStatus === 0">待审核</el-tag>
                    <el-tag v-if="scope.row.auditStatus === 1" type="success">审核通过</el-tag>
                    <el-tag v-if="scope.row.auditStatus === 2" type="danger">审核驳回</el-tag>
                </template>
            </el-table-column>
            <el-table-column>
                <template #default="scope">
                    <el-button type="primary" @click="updateReportHandler(scope.row.id, 1)">通过</el-button>
                    <el-button type="danger" @click="updateReportHandler(scope.row.id, 2)">驳回</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup lang="ts">
import type { Report } from '@/core/entity/dbEntities';
import { get, put } from '@/core/util';
import { ElNotification } from 'element-plus';
import { onMounted, ref } from 'vue';

const reportData = ref<Report[] | null>(null)
onMounted(async () => {
    await fetchReportData();
})

const updateReportHandler = async (id: number, newStatus: number) => {
    // 0 待审 1 通过 2 驳回
    const auditStatus = newStatus;
    await put(`/api/admin/reports/${id}/audit`,
        {
            auditStatus,
            remark: "更新状态"
        }
    ).then(async (res) => {
        if (res.code === 200) {
            ElNotification.success({
                title: '成功',
                message: '更新成功！',
                duration: 2000,
            })
            await fetchReportData();
        } else {
            ElNotification.error({
                title: '错误',
                message: '更新失败！',
                duration: 2000,
            })
        }
    })
}
const fetchReportData = async () => {
    await get('/api/admin/reports').then((res) => {
        reportData.value = res.data.list;
    })
}
</script>

<style scoped></style>