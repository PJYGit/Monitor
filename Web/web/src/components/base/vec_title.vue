<template>
<div>
    <vs-navbar color="white" v-model="activeItem" text-color="#5b5777" class="titleBar">
        <div slot="title">
            <vs-navbar-title style="font-size: 30px">
                入侵检测系统
            </vs-navbar-title>
        </div>

        <vs-navbar-item style="margin-right:20px;">
            <vs-chip color="primary" size="large">
                <vs-avatar></vs-avatar>
                <p>
                    {{ this.$store.state.userName }}
                </p>
            </vs-chip>
        </vs-navbar-item>

        <vs-button icon="settings" size="small" type="relief" @click="toSetting">设置</vs-button>
        <p style="margin-left:10px;"></p>
        <vs-button icon="reply" size="small" type="relief" color="danger" @click="logout">注销</vs-button>

    </vs-navbar>
</div>
</template>

<script>
import VecMenu from './vec_menu'

export default {
    name: 'vec-title',
    components: {
        VecMenu
    },
    data() {
        return {
            activeItem: 0,
            /**
             * 0 是管理员,
             * 1 是普通用户
             */
            personType: -1,
            logoutState: -1,
        }
    },
    mounted: function () {
        this.personType = this.$store.state.group
    },
    methods: {
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
        logout () {
            this.$axios({
                url: this.$prefix+'user/logout',
                method: 'post',
                data: {
                    user: this.$store.state.userName.toString(),
                    token: this.$store.state.token.toString()
                },
                transformRequest: [function(data) {
                    let ret = ''
                    for(let it in data) {
                        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                }],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(res => {
                console.log(res)
                console.log(this.$store.state.token)
                console.log(this.$store.state.userName)
                this.logoutState = res.data.state
                this.processState()
            }).catch(res => {
                console.log(res)
            })
        },
        processState () {
            if (this.logoutState===0) {
                this.$router.push({
                    path:'/'
                })
                this.$store.state.token = ''
                // this.$setLocalToken('isLogin', null)
            } else if (this.logoutState===1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误提示',
                    text: '用户身份验证错误'
                })
            }
        }
    }
}
</script>

<style scoped>
.titleBar {
    position: fixed;
    top: 0;
    height: 3rem !important;
    padding: .4rem 1rem;
    line-height: 1.5rem;
    align-items: center;
    justify-content: space-between;
    color: #5b5777;
    margin: 0 0 20px 0;
    border-radius: 5px;
    box-shadow: 0 0 15px 0 rgba(0, 0, 0, .04) !important;
}
</style>
