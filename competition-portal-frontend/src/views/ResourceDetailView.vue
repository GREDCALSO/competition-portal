<template>
  <div class="resource-detail-view">
      <MainHeader />
      <div class="detail-container">
          <h1>{{ resource.title }}</h1>
          <div class="meta">
              <el-tag>{{ resource.classification }}</el-tag>
              <el-tag v-for="tag in tags" :key="tag" type="info">{{ tag }}</el-tag>
          </div>
          
          <!-- 添加适用竞赛 -->
          <div class="competition-info">
              <el-tag type="warning">适用竞赛: {{ resource.applyCompetition }}</el-tag>
          </div>

          <div class="content">
              <p>{{ resource.description }}</p>
              <el-button type="primary" @click="goToResource">访问资源</el-button>
          </div>
      </div>
  </div>
</template>

<script>
import MainHeader from '@/components/MainHeader.vue'
import axios from 'axios'

export default {
  name: 'ResourceDetailView',
  components: {
      MainHeader
  },
  data() {
      return {
          resource: {} // 初始化为空对象
      }
  },
  computed: {
      tags() {
          // 安全处理，确保label存在
          return this.resource.label ? 
                 this.resource.label.split(',').map(tag => tag.trim()) : 
                 []
      }
  },
  methods: {
      goToResource() {
          // 使用实际的URL
          if (this.resource.url) {
              window.open(this.resource.url, '_blank')
          } else {
              this.$message.warning('该资源没有提供链接')
          }
      },
      fetchResourceDetail(id) {
          axios.get(`http://localhost:8080/api/resources/${id}`)
              .then(response => {
                  this.resource = response.data
              })
              .catch(error => {
                  console.error('获取资源详情失败:', error)
                  this.$message.error('获取资源详情失败')
              })
      }
  },
  created() {
      // 从路由参数获取ID并加载数据
      const id = this.$route.params.id
      if (id) {
          this.fetchResourceDetail(id)
      } else {
          this.$message.warning('资源ID不存在')
      }
  }
}
</script>

<style scoped>
.resource-detail-view {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.detail-container {
  max-width: 800px;
  margin: 30px auto;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.meta {
  margin: 15px 0;
}

.meta .el-tag {
  margin-right: 10px;
}

/* 新增的适用竞赛样式 */
.competition-info {
  margin: 15px 0;
}

.content {
  margin-top: 20px;
  line-height: 1.6;
}
</style>