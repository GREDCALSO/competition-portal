<template>
    <el-card class="resource-card">
        <template #header>
            <div class="card-header">
                <span>{{ resource.title }}</span>
                <el-tag :type="getTagType(resource.classification)">{{ resource.classification }}</el-tag>
            </div>
        </template>

        <div class="card-content">
            <p>{{ truncateDescription(resource.description) }}</p>
            <div class="competition-tag">
                <el-tag type="warning" size="small">适用竞赛: {{ resource.applyCompetition }}</el-tag>
            </div>
            <div class="tags">
                <el-tag v-for="tag in resource.label.split(',')" :key="tag" type="info" size="small">
                    {{ tag.trim() }}
                </el-tag>
            </div>
        </div>

        <div class="card-actions">
            <el-button type="primary" size="small" @click="viewDetail">查看详情</el-button>
            <el-button 
                type="success" 
                size="small"
                :icon="favoriteIcon"
                @click="toggleFavorite"
                :loading="favoriteLoading"
            >
                {{ isFavorited ? '已收藏' : '收藏' }}
            </el-button>
        </div>
    </el-card>
</template>

<script>
// 新增图标导入
import { StarFilled, Star } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
    name: 'ResourceCard',
    props: {
        resource: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            isFavorited: false,
            favoriteLoading: false
        }
    },
    computed: {
        // 新增计算属性获取当前图标
        favoriteIcon() {
            return this.isFavorited ? StarFilled : Star
        }
    },
    async mounted() {
        // 检查是否已收藏
        const result = await this.$store.dispatch('checkFavorite', this.resource.refID)
        this.isFavorited = result.isFavorited
    },
    methods: {
        truncateDescription(desc) {
            return desc.length > 100 ? desc.substring(0, 100) + '...' : desc;
        },
        getTagType(classification) {
            const types = {
                '视频': 'danger',
                '博客': 'success',
                '文档': 'warning',
                '开源项目': 'primary'
            }
            return types[classification] || 'info';
        },
        viewDetail() {
            this.$router.push({ name: 'ResourceDetail', params: { id: this.resource.refID } })
        },
        async toggleFavorite() {
            if (!this.$store.state.user) {
                ElMessage.warning('请先登录')
                this.$router.push('/login')
                return
            }
            this.favoriteLoading = true
            try {
                if (this.isFavorited) {
                    // 取消收藏
                    const result = await this.$store.dispatch('removeFavorite', this.resource.refID)
                    if (result.success) {
                        this.isFavorited = false
                        ElMessage.success(result.message)
                    } else {
                        ElMessage.error(result.message)
                    }
                } else {
                    // 添加收藏
                    const result = await this.$store.dispatch('addFavorite', {
                        refId: this.resource.refID
                    })
                    if (result.success) {
                        this.isFavorited = true
                        ElMessage.success(result.message)
                    } else {
                        ElMessage.error(result.message)
                    }
                }
            } catch (error) {
                ElMessage.error(error.message || '操作失败')
            } finally {
                this.favoriteLoading = false
            }
        }
    }
}
</script>

<style scoped>
.resource-card {
    margin-bottom: 24px;
    height: 300px;
    display: flex;
    flex-direction: column;
    border-radius: 14px;
    box-shadow: 0 4px 24px rgba(60, 60, 60, 0.10);
    transition: box-shadow 0.3s, transform 0.3s;
    background: #fff;
}
.resource-card:hover {
    transform: translateY(-4px) scale(1.03);
    box-shadow: 0 8px 32px rgba(60, 60, 60, 0.18);
}
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
}
.card-content {
    flex: 1;
    overflow: hidden;
    padding-top: 8px;
}
.card-content p {
    color: #444;
    font-size: 15px;
    line-height: 1.7;
    margin-bottom: 10px;
    max-height: 3.4em;           /* 最多显示2行（1.7*2） */
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;       /* 显示2行，多余省略 */
    -webkit-box-orient: vertical;
}
.competition-tag {
    margin: 10px 0;
}
.tags {
    margin-top: 10px;
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
}
.card-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 10px;
}
</style>