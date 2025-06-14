<template>
    <header class="app-header">
        <div class="logo">竞赛资料库</div>
        <nav class="main-nav">
            <router-link to="/">首页</router-link>
            <router-link to="/">视频</router-link>
            <router-link to="/">博客</router-link>
            <router-link to="/">文档</router-link>
            <router-link to="/">开源项目</router-link>
        </nav>
        <div class="user-actions">
            <template v-if="user">
                <el-dropdown>
                    <span class="user-info">
                        <el-avatar :size="30"
                            src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"></el-avatar>
                        {{ user.userName }}
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="$router.push('/favorites')">我的收藏</el-dropdown-item>
                            <el-dropdown-item @click="performLogout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </template>
            <template v-else>
                <el-button type="primary" @click="$router.push('/login')">登录</el-button>
                <el-button @click="$router.push('/register')">注册</el-button>
            </template>
        </div>
    </header>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { ElMessage } from 'element-plus'

export default {
    name: 'MainHeader',
    computed: {
        ...mapState(['user'])
    },
    methods: {
        ...mapActions(['logout']),
        
        async performLogout() {
            try {
                await this.logout()
                ElMessage.success('已退出登录')
                this.$router.push('/login')
            } catch (error) {
                console.error('退出登录失败:', error)
                ElMessage.error('退出登录失败')
            }
        }
    }
}
</script>

<style scoped>
.app-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    height: 60px;
    background: linear-gradient(135deg, #1e5799 0%, #207cca 51%, #2989d8 100%);
    color: white;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.logo {
    font-size: 24px;
    font-weight: bold;
}

.main-nav {
    display: flex;
    gap: 30px;
}

.main-nav a {
    color: rgba(255, 255, 255, 0.8);
    text-decoration: none;
    font-size: 16px;
    transition: color 0.3s;
}

.main-nav a:hover,
.main-nav a.router-link-exact-active {
    color: white;
    font-weight: 500;
}

.user-actions {
    display: flex;
    align-items: center;
    gap: 15px;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: white;
}
</style>