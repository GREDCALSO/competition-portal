<!-- src/views/UploadView.vue -->
<template>
    <div class="upload-view">
      <MainHeader />
      <div class="upload-container">
        <h1>上传资源</h1>
        <el-form :model="form" label-width="120px" :rules="rules" ref="formRef">
          <el-form-item label="资源标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入资源标题"></el-input>
          </el-form-item>
          
          <el-form-item label="资源描述" prop="description">
            <el-input 
              type="textarea" 
              v-model="form.description" 
              :rows="4"
              placeholder="请输入资源描述"></el-input>
          </el-form-item>
          
          <el-form-item label="资源类型" prop="classification">
            <el-select v-model="form.classification" placeholder="请选择资源类型">
              <el-option label="视频" value="视频"></el-option>
              <el-option label="博客" value="博客"></el-option>
              <el-option label="文档" value="文档"></el-option>
              <el-option label="开源项目" value="开源项目"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="适用竞赛" prop="applyCompetition">
            <el-input v-model="form.applyCompetition" placeholder="如：蓝桥杯、ACM等"></el-input>
          </el-form-item>
          
          <el-form-item label="资源标签" prop="label">
            <el-input 
              v-model="form.label" 
              placeholder="多个标签用逗号分隔，如：Python,算法,数据结构"></el-input>
          </el-form-item>
          
          <el-form-item label="资源链接" prop="url">
            <el-input v-model="form.url" placeholder="请输入资源URL"></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitForm" :loading="loading">提交</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </template>
  
  <script>
import MainHeader from '@/components/MainHeader.vue'
import { ElMessage } from 'element-plus'
import api from '@/api' // 导入API模块

export default {
  name: 'UploadView',
  components: {
    MainHeader
  },
  data() {
    return {
      form: {
        title: '',
        description: '',
        classification: '',
        applyCompetition: '',
        label: '',
        url: ''
      },
      rules: {
        title: [{ required: true, message: '请输入资源标题', trigger: 'blur' }],
        description: [{ required: true, message: '请输入资源描述', trigger: 'blur' }],
        classification: [{ required: true, message: '请选择资源类型', trigger: 'change' }],
        url: [{ required: true, message: '请输入资源链接', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    submitForm() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            // 使用导入的api模块调用createResource方法
            const response = await api.createResource(this.form)
            
            if (response.status === 200) {
              ElMessage.success('资源上传成功！')
              this.resetForm()
              // 刷新资源列表
              this.$store.dispatch('fetchResources')
            }
          } catch (error) {
            console.error('上传失败:', error)
            ElMessage.error('资源上传失败: ' + (error.response?.data?.message || error.message))
          } finally {
            this.loading = false
          }
        }
      })
    },
    resetForm() {
      this.$refs.formRef.resetFields()
    }
  }
}
</script>
  
  <style scoped>
  .upload-view {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }
  
  .upload-container {
    max-width: 800px;
    margin: 40px auto;
    padding: 30px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  }
  
  .upload-container h1 {
    text-align: center;
    margin-bottom: 30px;
    color: #207cca;
    font-size: 28px;
    font-weight: 600;
  }
  
  .el-form-item {
    margin-bottom: 25px;
  }
  </style>