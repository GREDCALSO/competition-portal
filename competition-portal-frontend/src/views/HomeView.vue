<template>
    <div class="home-view">
        <MainHeader />

        <div class="search-section">
            <el-input v-model="searchKeyword" placeholder="搜索竞赛资料..." class="search-input" @keyup.enter="search">
                <template #append>
                    <el-button icon="el-icon-search" @click="search"></el-button>
                </template>
            </el-input>

            <div class="filters">
                <el-select v-model="selectedClassify" placeholder="所有分类" @change="filter">
                    <el-option label="所有分类" value=""></el-option>
                    <el-option label="视频" value="视频"></el-option>
                    <el-option label="博客" value="博客"></el-option>
                    <el-option label="文档" value="文档"></el-option>
                    <el-option label="开源项目" value="开源项目"></el-option>
                </el-select>
            </div>
        </div>

        <!-- 添加加载状态和空状态 -->
        <div v-if="loading" class="loading-container">
            <el-icon class="is-loading" :size="50"><Loading /></el-icon>
            <p>加载中...</p>
        </div>
        
        <div v-else-if="filteredResources.length === 0" class="empty-container">
            <el-empty description="没有找到相关资源" />
        </div>
        
        <div v-else class="resources-container">
            <el-row :gutter="20">
                <el-col v-for="resource in filteredResources" :key="resource.refID" :xs="24" :sm="12" :md="8" :lg="6">
                    <ResourceCard :resource="resource" />
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue'
import ResourceCard from '@/components/ResourceCard.vue'
import { mapState } from 'vuex'
import { Loading } from '@element-plus/icons-vue'

export default {
    name: 'HomeView',
    components: {
        MainHeader,
        ResourceCard,
        Loading
    },
    data() {
        return {
            searchKeyword: '',
            selectedClassify: '',
            loading: true // 添加加载状态
        }
    },
    computed: {
        ...mapState(['resources']),
        filteredResources() {
            let result = this.resources;

            // 关键词搜索
            if (this.searchKeyword) {
                const keyword = this.searchKeyword.toLowerCase();
                result = result.filter(r =>
                    (r.title && r.title.toLowerCase().includes(keyword)) ||
                    (r.description && r.description.toLowerCase().includes(keyword)))
        }

            // 分类筛选
            if (this.selectedClassify) {
                result = result.filter(r => r.classification === this.selectedClassify);
            }

            return result;
        }
    },
    async mounted() {
        await this.$store.dispatch('fetchResources');
        this.loading = false; // 加载完成
    },
    methods: {
        search() {
            // 搜索逻辑已在计算属性中实现
        },
        filter() {
            // 过滤逻辑已在计算属性中实现
        }
    }
}
</script>

<style scoped>
.home-view {
    padding: 20px;
}

.search-section {
    max-width: 800px;
    margin: 20px auto;
    display: flex;
    gap: 20px;
}

.search-input {
    flex: 1;
}

.resources-container {
    margin-top: 30px;
}

/* 加载状态样式 */
.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 300px;
}

.loading-container p {
    margin-top: 20px;
    color: #666;
}

/* 空状态样式 */
.empty-container {
    margin-top: 50px;
}
</style>