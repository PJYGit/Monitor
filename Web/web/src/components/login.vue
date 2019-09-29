<template>
<div class="body">
    <div class="bacImg">
        <img src="../assets/background.jpg" alt="背景图片" height="100%" width="100%">
    </div>
    <div class="loginForm">
        <p>
            入侵查询系统
            <span style="display: inline-block; padding-left: 100%"></span>
        </p>
        <el-form :model="loginForm" :rules="loginRule" ref="loginForm">
            <el-form-item prop="userName" label="账号">
                <el-input placeholder="请输入账号..." v-model="loginForm.userName"></el-input>
            </el-form-item>
            <el-form-item prop="password" label="密码">
                <el-input type="password" placeholder="请输入密码..." v-model="loginForm.password" show-password clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="summitForm('loginForm')">登录</el-button>
            </el-form-item>
        </el-form>
        <vs-button type="flat" @click="forgotPsw=true">忘记密码？</vs-button>
        <vs-prompt color="primary" accept-text="确定" title="忘记密码？" :active.sync="forgotPsw" buttons-hidden>
            如果忘记密码请重置设备，详情请联系设备管理员
        </vs-prompt>
    </div>
</div>
</template>

<script>
export default {
    name: 'login',
    data() {
        let validateName = (rule, value, cb) => {
            let blank = /^\S*$/
            let userNamePattern = /^[0-9a-zA-Z_]{1,18}$/
            if (!userNamePattern.test(value) || !blank.test(value)) {
                cb(new Error('输入包括数字，字母和下划线并且不超过18位的正确用户名'))
            } else {
                cb()
            }
        }
        let validatePsw = (rule, value, cb) => {
            let blank = /^\S*$/
            let userPswPattern = /^[0-9a-zA-Z]{6,18}$/
            if (!userPswPattern.test(value) || !blank.test(value)) {
                cb(new Error('输入包括数字，字母并且超过6位不超过18位的密码'))
            } else {
                cb()
            }
        }
        return {
            /**
             *  状态码
             *  0 为登录成功
             *  -1 为密码错误或用户名错误
             */
            state: -1,
            /** 点击按钮后表单验证是否正确 */
            localState: 0,
            /** 表单 */
            loginForm: {
                userName: '',
                password: ''
            },
            forgotPsw: false,
            /** 验证规则 */
            loginRule: {
                userName: [{
                        min: 1,
                        message: '最短用户名是一位',
                        trigger: 'blur'
                    },
                    {
                        validator: validateName,
                        trigger: 'blur'
                    }
                ],
                password: [{
                        min: 6,
                        message: '最小密码6位',
                        trigger: 'blur'
                    },
                    {
                        validator: validatePsw,
                        trigger: 'blur'
                    }
                ]
            }
        }
    },
    mounted: function () {

    },
    methods: {
        summitForm(formName) {
            // console.log(this.$refs[formName].validate());
            this.$refs[formName].validate((valid) => {
                if (valid && this.localState === 0) {
                    this.localState = 1
                } else {
                    this.localState = 0
                }
            })

            if (this.localState === 1) {
                this.$axios({
                    url: this.$prefix + 'user/login',
                    method: 'post',
                    data: {
                        user: this.loginForm.userName.toString(),
                        pw: this.$md5(this.loginForm.password + this.$str)
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
                    this.state = res.data.state
                    this.$store.state.userName = this.loginForm.userName
                    this.$store.state.token = res.data.token
                    this.$store.state.group = res.data.group

                    // this.$setLocalToken('isLogin', 100)
                    // this.$setLocalToken('user', this.$store.state.userName)
                    // this.$setLocalToken('token', this.$store.state.token)
                    this.$store.state.isLogin = 100
                    console.log(this.$store.state.isLogin)
                    this.processLogin()
                }).catch(res => {
                    console.log(res)
                })
            }
        },
        processLogin() {
            if (this.state === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '欢迎 ~ ',
                    text: `欢迎您 ${this.loginForm.userName}`,
                    accept: this.toHome(),
                })
                this.$store.state.pswMD5 = this.$md5(this.loginForm.password + this.$str)
            } else if (this.state === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误提示',
                    text: '用户名错误或密码错误',
                    accept: this.acceptFunc,
                    close: this.acceptFunc
                })
            }
        },
        // openAlert() {
        //     this.$vs.dialog({
        //         color: 'primary',
        //         title: '忘记密码？',
        //         text: '如果忘记密码请重置设备，详情请联系设备管理员'
        //     })
        // },
        acceptFunc() {
            this.localState = 0
        },
        toHome() {
            this.$router.push({
                path: '/home'
            })
        }
    }
}
</script>

<style scoped>
.bacImg {
    width: 100%;
    height: 100%;
    position: absolute;
    z-index: -1;
    overflow: hidden;
}

.loginForm {
    margin: auto;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 20rem;
    position: absolute;
    background-color: white;
    padding: 20px 20px 10px 20px;
    border-radius: 5px;
}
</style>
