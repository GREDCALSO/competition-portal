import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import RegisterView from '@/views/RegisterView.vue'
import ResourceDetailView from '@/views/ResourceDetailView.vue'
import FavoritesView from '@/views/FavoritesView.vue' // 新增

const routes = [
  { path: '/', name: 'Home', component: HomeView },
  { path: '/login', name: 'Login', component: LoginView },
  { path: '/register', name: 'Register', component: RegisterView },
  { path: '/resource/:id', name: 'ResourceDetail', component: ResourceDetailView },
  { path: '/favorites', name: 'Favorites', component: FavoritesView } // 新增
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router