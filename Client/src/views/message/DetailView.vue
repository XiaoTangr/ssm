<template>
    <div>
        {{ id }}
        {{ data }}
    </div>
</template>

<script setup lang="ts">

import type { Message } from '@/core/entity/dbEntities';
import { get } from '@/core/util';
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router';


// 获取路径参数
const route = useRoute()
const id = ref()

const data = ref<Message>()

onMounted(() => {
    id.value = route.params.id
    fetchMessageDetail()
})


const fetchMessageDetail = async () => {
    await get<Message>('/api/messages/' + id.value).then(res => {
        data.value = res.data
    })
}

</script>

<style scoped></style>