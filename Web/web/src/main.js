import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Vuesax from 'vuesax'
import 'vuesax/dist/vuesax.css'
import 'material-icons/iconfont/material-icons.css'
import axios from 'axios'
import { createCookie, readCookie, eraseCookie } from './util/cookie'
import md5 from 'js-md5'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(Vuesax)

Vue.prototype.$axios = axios
Vue.prototype.$md5 = md5
Vue.prototype.createCookie = createCookie
Vue.prototype.readCookie = readCookie
Vue.prototype.eraseCookie = eraseCookie
Vue.prototype.$imgPrefix = ''
Vue.prototype.$prefix = 'http://127.0.0.1:8081/server/'
Vue.prototype.$picPF = 'http://127.0.0.1:8080/pic/'
Vue.prototype.$str = 'DSYTQL%%%66619260817cptbtptpbcptdtptpYMFMgakki666tryqtylwwzzxym'

/** 设置 localStorage 保持登录状态 */
// Vue.prototype.$setLocalToken = function (tokenName, tokenValue) {
//   if (window.localStorage) {
//     localStorage.setItem(tokenName, tokenValue)
//   } else {
//     alert('This browser does NOT support localStorage')
//   }
// }
// Vue.prototype.$getLocalToken = function (name) {
//   let token = localStorage.getItem(name)
//   if (token) {
//     return token
//   } else {
//     return ''
//   }
// }

// todo 刷新提示信息
/** 未登录拦截 */
router.beforeEach((to, from, next) => {
  if (to.path === '/') {
    next()
  } else {
    if (!store.state.token) {
      next({ path: '/' })
      alert('您还没有登录')
    } else {
      next()
    }
  }
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App),
  created () {
    // 判断是否有本地存储中是否有isLogin，并更新vuex仓库
    // if (this.$getLocalToken('isLogin') == null) {
    //   this.$setLocalToken('isLogin', '')
    // }
    // this.$store.state.isLogin = this.$getLocalToken('isLogin')
    // this.$store.state.userName = this.$getLocalToken('user')
    // this.$store.state.token = this.$getLocalToken('token')
  }
}).$mount(App)
