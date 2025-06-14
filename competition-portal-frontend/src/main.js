import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
const app = createApp(App)
app.use(store)
app.use(router)
app.use(ElementPlus)

// 设置axios基本URL
// axios.defaults.baseURL = 'http://localhost:8080/api'
// app.config.globalProperties.$axios = axios
app.mount('#app')