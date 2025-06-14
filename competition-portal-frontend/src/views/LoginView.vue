<template>
    <div class="login-view">
        <MainHeader />
        <div class="login-container">
            <h1>用户登录</h1>
            <el-form :model="loginForm" label-width="80px">
                <el-form-item label="用户名">
                    <el-input v-model="loginForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="loginForm.password" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue'
import { mapActions } from 'vuex'
import { ElMessage } from 'element-plus'

export default {
    name: 'LoginView',
    components: {
        MainHeader
    },
    data() {
        return {
            loginForm: {
                username: '',
                password: ''
            },
            loading: false
        }
    },
    methods: {
        ...mapActions(['login']),
        async handleLogin() {
            // 验证输入
            if (!this.loginForm.username.trim()) {
                ElMessage.warning('请输入用户名')
                return
            }

            if (!this.loginForm.password) {
                ElMessage.warning('请输入密码')
                return
            }

            this.loading = true
            console.log('[LoginView] 提交登录表单:', this.loginForm)

            try {
                const result = await this.login(this.loginForm)
                console.log('[LoginView] 登录结果:', result)
                
                if (result.success) {
                    ElMessage.success(result.message)
                    this.$router.push('/')
                } else {
                    ElMessage.error(result.message || '登录失败')
                }
            } catch (error) {
                console.error('[LoginView] 登录异常:', error)
                ElMessage.error(error.message || '登录失败，请稍后再试')
            } finally {
                this.loading = false
            }
        }
    }
}
</script>

<style scoped>
.login-view {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.login-container {
    max-width: 500px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #eee;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>