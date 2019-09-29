<template lang="html">
<div class="bodyClass">

    <vs-sidebar :reduce="screenWidth<755" :reduce-not-hover-expand="true" hidden-background :default-index="menuIndex" color="primary" class="sidebar" spacer v-model="active">
        <vs-divider icon="list" position="left">功能</vs-divider>
        <vs-sidebar-item v-if="personType===0 || personType===1" index="1" icon="dashboard" to="/home">
            <p v-if="screenWidth>=755">仪表盘</p>
        </vs-sidebar-item>
        <vs-sidebar-item v-if="personType===0 || personType===1" index="2" icon="photo" to="/new_photo">
            <p v-if="screenWidth>=755">最新照片</p>
        </vs-sidebar-item>
        <vs-sidebar-item v-if="personType===0 || personType===1" index="3" icon="photo_camera" @click="toDevice">
            <p v-if="screenWidth>=755">在线设备</p>
        </vs-sidebar-item>

        <vs-divider v-if="personType===1" icon="group" position="left">管理</vs-divider>
        <vs-sidebar-item v-if="personType===1" index="5" icon="person_add" to="/person_management">
            <p v-if="screenWidth>=755">成员管理</p>
        </vs-sidebar-item>

        <vs-spacer></vs-spacer>

    </vs-sidebar>
</div>
</template>

<script>
export default {
    name: 'vec-menu',
    props: {
        menuIndex: String
    },
    data() {
        return {
            active: true,
            activeIndex: '1',
            screenWidth: document.documentElement.clientWidth,    // 浏览器宽度
            /**
             * 0 是管理员,
             * 1 是普通用户
             */
            personType: -1
        }
    },
    created () {
        this.personType = this.$store.state.group
    },
    mounted () {
        this.screenWidth = document.documentElement.clientWidth
        const scope = this;
        window.onresize = function () {
            scope.screenWidth = `${document.documentElement.clientWidth}`
        }
    },
    methods: {
        toHome() {
            this.$router.push({
                path: '/home'
            })
        },
        toSetting() {
            if (this.personType === 1) {
                this.$router.push({
                    path: '/setting_admin'
                })
            } else if (this.personType === 0) {
                this.$router.push({
                    path: '/setting_user'
                })
            }
        },
        toDevice() {
            if (this.personType === 1) {
                this.$router.push({
                    path: '/device_setting'
                })
            } else if (this.personType === 0) {
                this.$router.push({
                    path: '/online_device'
                })
            }
        }
    }
}
</script>

<style scoped>
.bodyClass {
    background-color: white;
    z-index: -1;
}

.sidebar {
    position: fixed;
    top: 3rem;
    bottom: 0;
    left: 0;
    width: 15%;
}

.header-sidebar {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    width: 100%;
}

.header-sidebar h4 {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
}

.header-sidebar h4>button {
    margin-left: 10px;
}

.footer-sidebar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
}

.footer-sidebar>button {
    border: 0 solid rgba(0, 0, 0, 0) !important;
    border-left: 1px solid rgba(0, 0, 0, .07) !important;
    border-radius: 0 !important;
}
</style>

<style>
    .sidebar>div>div  {
        overflow: hidden;
    }
</style>
