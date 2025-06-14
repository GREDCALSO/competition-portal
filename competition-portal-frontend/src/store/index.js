import { createStore } from 'vuex'
import api from '@/api'

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    resources: [],
    favorites: [], // 收藏的资源
    folders: []    // 收藏夹
  },
  mutations: {
    setUser(state, user) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    clearUser(state) {
      state.user = null
      localStorage.removeItem('user')
    },
    setResources(state, resources) {
      state.resources = resources
    },
    setFavorites(state, favorites) {
      state.favorites = favorites
    },
    setFolders(state, folders) {
      state.folders = folders
    },
    addFavorite(state, favorite) {
      if (!state.favorites.some(f => f.refID === favorite.refID)) {
        state.favorites.push(favorite)
      }
    },
    removeFavorite(state, refId) {
      state.favorites = state.favorites.filter(f => f.refID !== refId)
    }
  },
  actions: {
    async fetchResources({ commit }) {
      try {
        const response = await api.getResources()
        commit('setResources', response.data)
      } catch (error) {
        console.error('获取资源失败:', error)
      }
    },
    async register({ dispatch }, userData) {
      try {
        const response = await api.register({
          userName: userData.username,
          userKeyword: userData.password
        })
        
        const result = response.data
        
        if (result.success) {
          return dispatch('login', {
            username: userData.username,
            password: userData.password
          })
        } else {
          return { success: false, message: result.message || '注册失败，未知错误' }
        }
      } catch (error) {
        return { success: false, message: error.message || '注册失败，请稍后再试' }
      }
    },
    async login({ commit }, credentials) {
      try {
        const response = await api.login({
          userName: credentials.username,
          userKeyword: credentials.password
        })
        
        let result = response.data
        
        if (typeof result === 'string') {
          result = {
            success: result.includes('成功'),
            message: result
          }
        }
        
        if (result.success) {
          if (!result.user) {
            result.user = {
              userName: credentials.username
            }
          }
          
          commit('setUser', result.user)
          return { success: true, message: result.message }
        } else {
          return { 
            success: false, 
            message: result.message || '登录失败，未知错误' 
          }
        }
      } catch (error) {
        return { 
          success: false, 
          message: error.message || '登录失败，请稍后再试' 
        }
      }
    },
    logout({ commit }) {
      return new Promise((resolve) => {
        commit('clearUser')
        resolve()
      })
    },

    // 收藏相关 actions
    async addFavorite({ commit, state }, { refId, folderId = null }) {
      if (!state.user) {
        throw new Error('请先登录')
      }
      try {
        const response = await api.addFavorite({
          userId: state.user.userID,
          refId: refId,
          folderId: folderId
        })
        if (response.data.success) {
          const resource = state.resources.find(r => r.refID === refId)
          if (resource) {
            commit('addFavorite', resource)
          }
          return { success: true, message: response.data.message }
        } else {
          return { success: false, message: response.data.message }
        }
      } catch (error) {
        return { success: false, message: error.message || '收藏失败' }
      }
    },
    async removeFavorite({ commit, state }, refId) {
      if (!state.user) {
        throw new Error('请先登录')
      }
      try {
        const response = await api.removeFavorite(state.user.userID, refId)
        if (response.data.success) {
          commit('removeFavorite', refId)
          return { success: true, message: response.data.message }
        } else {
          return { success: false, message: response.data.message }
        }
      } catch (error) {
        return { success: false, message: error.message || '取消收藏失败' }
      }
    },
    async fetchUserFavorites({ commit, state }) {
      if (!state.user) return
      try {
        const response = await api.getUserFavorites(state.user.userID)
        if (response.data.success) {
          commit('setFavorites', response.data.favorites || [])
          commit('setFolders', response.data.folders || [])
          return { success: true }
        } else {
          return { success: false, message: response.data.message }
        }
      } catch (error) {
        return { success: false, message: error.message || '获取收藏失败' }
      }
    },
    async checkFavorite({ state }, refId) {
      if (!state.user) return { isFavorited: false }
      try {
        const response = await api.checkFavorite(state.user.userID, refId)
        return { isFavorited: response.data.success }
      } catch (error) {
        return { isFavorited: false }
      }
    }
  }
})