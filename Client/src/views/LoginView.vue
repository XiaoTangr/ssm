<template>
    <div class="container">
        <el-card>
            <template #header>
                <h2>绵城留言墙</h2>
            </template>
            <template #default>
                <div class="card">
                    <div class="info">
                        <p>欢迎使用！为规范管理，请先登录！</p>
                        <br>
                        <p>请勿发布任何违法信息！</p>
                        <p>我们将严肃追究违法责任！</p>
                    </div>
                    <div class="form">
                        <el-form :model="loginForm">
                            <el-form-item>
                                <el-input :prefix-icon="User" v-model="loginForm.email" placeholder="请输入邮箱" clearable
                                    style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-input :prefix-icon="Lock" type="password" v-model="loginForm.password"
                                    placeholder="请输入密码" show-password clearable style="width: 16rem;">
                                </el-input>
                            </el-form-item>
                            <div class="buttons">
                                <el-button class="button" type="primary" @click="loginHandler()">登 录</el-button>
                                <el-button class="button" @click="registerHandler()">注 册</el-button>
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
import { useCurrentUserStore } from '@/stores/currentUserStore';
import { Lock, Search, User } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';

const loginForm = reactive({
    email: '',
    password: ''
});
const currentUserStore = useCurrentUserStore();
const router = useRouter();
const loginHandler = async () => {

    if (!loginForm.email || !loginForm.password) {
        ElMessage.warning('请填写完整的登录信息！');
    }
    await currentUserStore.login(loginForm.email, loginForm.password).then((res: number | string) => {
        if (res === 0) {
            ElMessage.success(`登陆成功！`);
            router.push('/');
        } else {
            ElMessage.error(`登陆失败:${res}`)
        }
    });
}
const registerHandler = () => {
    router.push('/register');
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