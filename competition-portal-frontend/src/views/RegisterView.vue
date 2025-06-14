<template>
    <div class="register-view">
        <MainHeader />
        <div class="register-container">
            <h1>用户注册</h1>
            <el-form :model="registerForm" label-width="80px">
                <el-form-item label="用户名">
                    <el-input v-model="registerForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="registerForm.password" type="password"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                    <el-input v-model="registerForm.confirmPassword" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleRegister" :loading="loading">注册</el-button>
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
    name: 'RegisterView',
    components: {
        MainHeader
    },
    data() {
        return {
            registerForm: {
                username: '',
                password: '',
                confirmPassword: ''
            },
            loading: false
        }
    },
    methods: {
        ...mapActions(['register']),
        async handleRegister() {
            // 验证输入
            if (!this.registerForm.username.trim()) {
                ElMessage.warning('请输入用户名')
                return
            }

            if (!this.registerForm.password) {
                ElMessage.warning('请输入密码')
                return
            }

            if (this.registerForm.password !== this.registerForm.confirmPassword) {
                ElMessage.warning('两次输入的密码不一致')
                return
            }

            this.loading = true
            console.log('[RegisterView] 提交注册表单:', this.registerForm)

            try {
                const result = await this.register({
                    username: this.registerForm.username,
                    password: this.registerForm.password
                })
                
                console.log('[RegisterView] 注册结果:', result)
                
                if (result.success) {
                    ElMessage.success(result.message)
                    // 注册成功后自动跳转到登录页
                    this.$router.push('/login')
                } else {
                    ElMessage.error(result.message || '注册失败')
                }
            } catch (error) {
                console.error('[RegisterView] 注册异常:', error)
                ElMessage.error(error.message || '注册失败，请稍后再试')
            } finally {
                this.loading = false
            }
        }
    }
}
</script>

<style scoped>
.register-view {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.register-container {
    max-width: 500px;
    margin: 50px auto;
    padding: 20px;
    border: 1px solid #eee;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>