<!-- src/views/FavoritesView.vue -->
<template>
    <div class="favorites-view">
        <MainHeader />
        
        <div class="favorites-container">
            <h1>我的收藏</h1>
            
            <div v-if="folders.length > 0" class="folders-section">
                <h2>收藏夹</h2>
                <div class="folders-list">
                    <div 
                        v-for="folder in folders" 
                        :key="folder.folderID"
                        class="folder-item"
                        @click="selectFolder(folder)"
                    >
                        <el-card shadow="hover">
                            <h3>{{ folder.folderName }}</h3>
                            <p>{{ folder.folderDesc || '暂无描述' }}</p>
                        </el-card>
                    </div>
                </div>
            </div>
            
            <div class="favorites-section">
                <h2>{{ selectedFolder ? selectedFolder.folderName : '所有收藏' }}</h2>
                
                <div v-if="loading" class="loading-container">
                    <el-icon class="is-loading" :size="50"><Loading /></el-icon>
                    <p>加载中...</p>
                </div>
                
                <div v-else-if="filteredFavorites.length === 0" class="empty-container">
                    <el-empty :description="selectedFolder ? '该收藏夹为空' : '您还没有收藏任何资源'" />
                </div>
                
                <div v-else class="resources-container">
                    <el-row :gutter="20">
                        <el-col 
                            v-for="resource in filteredFavorites" 
                            :key="resource.refID" 
                            :xs="24" :sm="12" :md="8" :lg="6"
                        >
                            <ResourceCard :resource="resource" />
                        </el-col>
                    </el-row>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue'
import ResourceCard from '@/components/ResourceCard.vue'
import { Loading } from '@element-plus/icons-vue'
import { mapState } from 'vuex'

export default {
    name: 'FavoritesView',
    components: {
        MainHeader,
        ResourceCard,
        Loading
    },
    data() {
        return {
            loading: true,
            selectedFolder: null
        }
    },
    computed: {
        ...mapState(['favorites', 'folders']),
        filteredFavorites() {
            // 根据选中的收藏夹过滤资源
            if (!this.selectedFolder) return this.favorites
            
            // 实际项目中应根据收藏夹筛选，这里简化处理
            return this.favorites
        }
    },
    async mounted() {
        if (this.$store.state.user) {
            await this.$store.dispatch('fetchUserFavorites')
        }
        this.loading = false
    },
    methods: {
        selectFolder(folder) {
            this.selectedFolder = folder
        }
    }
}
</script>

<style scoped>
.favorites-view {
    padding: 20px;
}

.favorites-container {
    max-width: 1200px;
    margin: 0 auto;
}

.folders-section {
    margin-bottom: 30px;
}

.folders-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin-top: 15px;
}

.folder-item {
    width: 250px;
    cursor: pointer;
}

.folder-item:hover {
    transform: translateY(-5px);
    transition: transform 0.3s;
}

.favorites-section {
    margin-top: 30px;
}

.resources-container {
    margin-top: 20px;
}

.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
}

.loading-container p {
    margin-top: 20px;
    color: #666;
}

.empty-container {
    margin-top: 50px;
}
</style>