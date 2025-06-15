import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
})

// 请求拦截器 - 添加请求日志
api.interceptors.request.use(config => {
  console.log(`[API] 请求: ${config.method.toUpperCase()} ${config.url}`, config.data)
  return config
}, error => {
  console.error('[API] 请求错误:', error)
  return Promise.reject(error)
})

// 响应拦截器 - 添加响应日志
api.interceptors.response.use(response => {
  console.log(`[API] 响应: ${response.config.url}`, response.data)
  
  // 处理纯文本响应
  if (typeof response.data === 'string') {
    console.warn('[API] 收到纯文本响应，尝试转换为JSON')
    try {
      // 尝试解析为JSON
      response.data = JSON.parse(response.data)
    } catch (e) {
      console.warn('[API] 解析JSON失败，创建包装对象')
      // 创建基础响应对象
      response.data = {
        success: response.data.includes('成功'),
        message: response.data
      }
    }
  }
  
  return response
}, error => {
  if (error.response) {
    console.error(`[API] 错误响应: ${error.config.url}`, {
      status: error.response.status,
      data: error.response.data,
      headers: error.response.headers
    })
    
    let message = '请求错误'
    // const status = error.response.status
    
    // 尝试从错误响应中提取消息
    if (error.response.data && typeof error.response.data === 'object') {
      message = error.response.data.message || message
    } else if (typeof error.response.data === 'string') {
      try {
        const parsed = JSON.parse(error.response.data)
        message = parsed.message || message
      } catch (e) {
        message = error.response.data
      }
    }
    
    return Promise.reject(new Error(message))
  } else if (error.request) {
    console.error('[API] 无响应:', error.request)
    return Promise.reject(new Error('服务器未响应'))
  } else {
    console.error('[API] 请求设置错误:', error.message)
    return Promise.reject(new Error('请求设置错误'))
  }
})

export default {
  getResources() {
    return api.get('/resources')
  },
  login(credentials) {
    return api.post('/auth/login', credentials)
  },
  register(userData) {
    return api.post('/auth/register', userData)
  },

  // 收藏相关API
  addFavorite(data) {
    return api.post('/favorites', data)
  },
  removeFavorite(userId, refId) {
    return api.delete(`/favorites/${userId}/${refId}`)
  },
  getUserFavorites(userId) {
    return api.get(`/favorites/${userId}`)
  },
  checkFavorite(userId, refId) {
    return api.get(`/favorites/check/${userId}/${refId}`)
  },
  createResource(data) {
    return api.post('/resources', data)
  }
}