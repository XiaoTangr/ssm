<template>
    <div class="container">
        <el-card>
            <template #header>
                <h2>绵城留言墙-重置密码</h2>
            </template>
            <template #default>
                <div class="card">
                    <div class="info">
                        <p>欢迎使用！为规范管理，请先重置账号！</p>
                        <br>
                        <p>请勿使用任何违法信息作为账号资料！</p>
                        <p>我们将严肃追究违法行为责任！</p>
                    </div>
                    <div class="form">
                        <el-form :model="resetForm">
                            <el-form-item>
                                <el-input :prefix-icon="User" v-model="resetForm.email" placeholder="请输入邮箱" clearable
                                    style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input :prefix-icon="User" v-model="resetForm.nickname" placeholder="请输入昵称" clearable
                                    style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input :prefix-icon="Lock" type="password" v-model="resetForm.password"
                                    placeholder="请输入密码" show-password clearable style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input :prefix-icon="Lock" type="password" v-model="resetForm.confirmPassword"
                                    placeholder="请确认密码" show-password clearable style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <div class="buttons">
                                <el-button class="button" type="primary" @click="registerHandler()">重 置</el-button>
                                <el-button class="button" @click="loginHandler()">登 录</el-button>
                            </div>

                        </el-form>
                    </div>

                </div>
            </template>
            <template #footer>
                <div class="footer">Copyright © 2025 JavaT</div>
            </template>

        </el-card>

    </div>
</template>

<script setup lang="ts">
import { Lock, User } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { post } from '@/core/util/http/request';

const resetForm = reactive({
    email: '',
    nickname: '',
    password: '',
    confirmPassword: ''
});

const router = useRouter();

const registerHandler = async () => {
    if (!resetForm.email || !resetForm.nickname || !resetForm.password || !resetForm.confirmPassword) {
        ElMessage.warning('请填写完整的重置信息！');
        return;
    }

    if (resetForm.password !== resetForm.confirmPassword) {
        ElMessage.warning('两次输入的密码不一致！');
        return;
    }

    // 调用后端重置接口
    await post('/api/users/reset', {
        email: resetForm.email,
        nickName: resetForm.nickname,
        password: resetForm.password,
        confirmPassword: resetForm.confirmPassword
    }, { withToken: false }).then((res) => {
        if (res.code === 200) {
            ElMessage.success('重置成功！');
            router.push('/user/login');
        } else {
            ElMessage.error(`重置失败: ${res.message}`);
        }
    }).catch((err) => {
        ElMessage.error(`重置失败: ${err.message}`);
    });
}

const loginHandler = () => {
    router.push('/user/login');
}
</script>

<style scoped lang="scss">
@use '@/core/styles/variable.scss';

.container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;

    .card {
        width: 36rem;
        display: flex;
        justify-content: center;
        align-items: center;

        .info {
            margin-right: .5rem;
            flex: 1;
        }

        .form {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-left: .5rem;

            .buttons {
                display: flex;
                width: 100%;

                .button {
                    flex: 1;
                    margin: 0 .5rem;

                    &:first-child {
                        margin-left: 0;
                    }

                    &:last-child {
                        margin-right: 0;
                    }
                }
            }
        }
    }

    .footer {
        text-align: center;
    }
}
</style>