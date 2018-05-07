import Vue from 'vue'
import App from './App'
import router from './router'
import VueResource from 'vue-resource'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.use(VueResource)

/* eslint-disable no-new */
// 这灵活得亮瞎了
new Vue({
  el: '#app',
  template: '<App/>',
  router,
  components: {App}
})
