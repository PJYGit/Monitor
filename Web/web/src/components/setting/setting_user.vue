<template>
<div>
    <div>
        <vec-title></vec-title>
        <vec-menu></vec-menu>
    </div>
    <div class="body">

        <div class="bodyTitle">
            <h2>用户设置界面</h2>
        </div>
        <vs-divider />
        <div class="bodyBody">
            <vs-row vs-justify="center">
                <vs-col type="flex" vs-justify="center" vs-align="center" vs-w="5">
                    <vs-card>
                        <div slot="header">
                            <h3>密码修改</h3>
                        </div>
                        <div>
                            <el-form :model="pswForm" :rules="pswRules" ref="pswForm">
                                <el-form-item label="请输入旧密码">
                                    <el-input show-password clearable v-model="oldPsw"></el-input>
                                </el-form-item>
                                <el-form-item label="请输入新密码" prop="psw">
                                    <el-input show-password clearable type="password" v-model="pswForm.psw"></el-input>
                                </el-form-item>
                                <el-form-item label="请再次输入新密码" prop="pswTwice">
                                    <el-input show-password clearable type="password" v-model="pswForm.pswTwice"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button type="primary" @click="changePsw">提交更改</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </vs-card>
                </vs-col>
            </vs-row>
        </div>
    </div>
</div>
</template>

<script>
import VecTitle from '../base/vec_title'
import VecMenu from '../base/vec_menu'
export default {
    name: 'setting_user',
    components: {
        VecMenu,
        VecTitle
    },
    data() {
        let validatePsw = (rule, value, cb) => {
            if (value === '') {
                cb(new Error('请输入密码'))
            } else {
                if (this.pswForm.pswTwice !== '') {
                    this.$refs.pswForm.validateField('pswTwice')
                }
                cb()
            }
        }
        let validatePswTwice = (rule, value, cb) => {
            if (value === '') {
                cb(new Error('请再次输入密码'))
            } else if (value !== this.pswForm.psw) {
                console.log(value)
                cb(new Error('两次输入密码不一致！'))
            } else {
                cb()
            }
        }
        return {
            oldPsw: '',
            changePswState: '',
            pswForm: {
                psw: '',
                pswTwice: '',
            },
            pswRules: {
                psw: [{
                    validator: validatePsw,
                    trigger: 'blur'
                }],
                pswTwice: [{
                    validator: validatePswTwice,
                    trigger: 'blur'
                }]
            }
        }
    },
    methods: {
        changePsw() {
            if (this.$store.state.pswMD5 === this.$md5(this.oldPsw + this.$str)) {
                this.$axios({
                    url: this.$prefix + 'user/pwchange',
                    method: 'post',
                    data: {
                        user: this.$store.state.userName,
                        token: this.$store.state.token,
                        newpw: this.$md5(this.pswForm.psw + 'DSYTQL%%%66619260817cptbtptpbcptdtptpYMFMgakki666tryqtylwwzzxym')
                    },
                    transformRequest: [function (data) {
                        let ret = ''
                        for (let it in data) {
                            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                        }
                        return ret
                    }],
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then(res => {
                    console.log(res)
                    this.changePswState = res.data.state
                    this.processState()
                }).catch(res => {
                    console.log(res)
                })
            } else {
                this.$vs.dialog({
                    color: 'warning',
                    title: '错误警告',
                    text: '旧密码输入错误'
                })
            }
        },
        processState() {
            if (this.changePswState === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '提示',
                    text: '修改成功'
                })
                this.oldPsw = ''
                this.pswForm.psw = ''
                this.pswForm.pswTwice = ''
            } else if (this.changePswState === -1) {
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
.body {
    margin: 5rem 5% 5% 18%;
}
</style>
