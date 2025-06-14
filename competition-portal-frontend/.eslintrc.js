module.exports = {
    root: true,
    env: {
      node: true
    },
    extends: [
      'plugin:vue/vue3-essential',
      'eslint:recommended'
    ],
    rules: {
      'vue/multi-word-component-names': 'off' // 关闭组件名必须多单词的规则
    }
  }