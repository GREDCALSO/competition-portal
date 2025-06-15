<!-- src/views/FavoritesView.vue -->
<template>
    <div class="favorites-view">
        <MainHeader />
        
        <div class="favorites-container">
            <h1>我的收藏</h1>
            
            <!-- 添加搜索和筛选区域 -->
            <div class="search-section">
                <el-input 
                    v-model="searchKeyword" 
                    placeholder="搜索收藏资源..." 
                    class="search-input" 
                    @keyup.enter="search">
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
            selectedFolder: null,
            searchKeyword: '',
            selectedClassify: ''
        }
    },
    computed: {
        ...mapState(['favorites', 'folders']),
        filteredFavorites() {
            let result = this.favorites;
            // 根据选中的收藏夹过滤资源
            // if (this.selectedFolder) {
            //     // 实际项目中应根据收藏夹筛选，这里简化处理
            //     result = result
            // }
            // 关键词搜索
            if (this.searchKeyword) {
                const keyword = this.searchKeyword.toLowerCase();
                result = result.filter(r => 
                    (r.title && r.title.toLowerCase().includes(keyword)) ||
                    (r.description && r.description.toLowerCase().includes(keyword))
                )
            }
            // 分类筛选
            if (this.selectedClassify) {
                result = result.filter(r => r.classification === this.selectedClassify);
            }
            return result;
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
        },
        search() {
            // 搜索逻辑已在计算属性中实现
        },
        async filter() {
            this.loading = true;
            try {
                await this.$store.dispatch('fetchFavoritesByClassify', this.selectedClassify);
            } catch (error) {
                console.error('获取收藏失败:', error);
            } finally {
                this.loading = false;
            }
        }
    }
}
</script>

<style scoped>
/* 添加搜索区域样式 */
.search-section {
  max-width: 800px;
  margin: 20px auto 30px;
  display: flex;
  gap: 20px;
  align-items: center;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.07);
  padding: 20px;
}
.search-input {
  flex: 1;
}
.filters {
  min-width: 160px;
}

/* ...原有样式... */
.favorites-view {
    padding: 32px 0;
    min-height: 100vh;
    background: linear-gradient(135deg, #f5faff 0%, #e3f0fc 100%);
}
.favorites-container {
    max-width: 1200px;
    margin: 0 auto;
    background: #fff;
    border-radius: 16px;
    box-shadow: 0 4px 32px rgba(60, 60, 60, 0.08);
    padding: 32px 40px 40px 40px;
}
.folders-section {
    margin-bottom: 36px;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 18px;
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
    border-radius: 12px;
    overflow: hidden;
    transition: box-shadow 0.3s, transform 0.3s;
    box-shadow: 0 2px 8px rgba(60, 60, 60, 0.08);
    background: #fff;
}
.folder-item.selected {
    border: 2px solid #409EFF;
    background: #f0f8ff;
}
.folder-item:hover {
    transform: translateY(-5px) scale(1.03);
    box-shadow: 0 8px 24px rgba(60, 60, 60, 0.18);
}
.folder-item h3 {
    font-size: 18px;
    font-weight: bold;
    color: #409EFF;
    margin-bottom: 8px;
}
.folder-item p {
    color: #888;
    font-size: 14px;
}
.favorites-section {
    margin-top: 30px;
}
.favorites-section h2 {
    font-size: 22px;
    font-weight: 600;
    color: #222;
    margin-bottom: 18px;
    border-left: 4px solid #409EFF;
    padding-left: 12px;
    background: linear-gradient(90deg, #e3f0fc 0%, #fff 100%);
    border-radius: 4px;
}
.resources-container {
    margin-top: 20px;
    padding-bottom: 40px;
}
.el-col {
    margin-bottom: 24px;
}
.loading-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
    color: #409EFF;
}
.loading-container p {
    margin-top: 20px;
    color: #666;
}
.empty-container {
    margin-top: 50px;
    text-align: center;
}
</style>