import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: '',
      component: () => import('./views/index.vue')
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('./views/home.vue')
    },
    {
      path: '/setting',
      name: 'setting',
      component: () => import('./views/setting.vue')
    },
    {
      path: '/person_management',
      name: 'person_management',
      component: () => import('./views/management/management.vue')
    },
    {
      path: '/new_photo',
      name: 'new_photh',
      component: () => import('./views/new_photo.vue')
    },
    {
      path: '/online_device',
      name: 'online_device',
      component: () => import('./views/online_device.vue')
    },
    {
      path: '/history_search',
      name: 'history_search',
      component: () => import('./views/history_search.vue')
    },
    {
      path: '/device_setting',
      name: 'device_setting',
      component: () => import('./views/setting/device_setting.vue')
    },
    {
      path: '/setting_admin',
      name: 'setting_admin',
      component: () => import('./components/setting/setting_admin.vue')
    },
    {
      path: '/setting_user',
      name: 'setting_user',
      component: () => import('./components/setting/setting_user.vue')
    }
  ]
})
