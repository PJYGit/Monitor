<template>
<div id="management" class="vs-con-loading__container">
    <vs-table stripe pagination max-items="10" :data="users">
        <template slot="header">
            <h2 class="titleClass">人员管理</h2>
            <vs-button radius size="small" icon="person_add" style="margin-right: 20px;" @click="addPerson=true"></vs-button>
            <!-- 弹出界面 -->
            <vs-popup title="添加用户" :active.sync="addPerson" style="">
                <vs-row>
                    <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="12">
                        <vs-input icon-no-border icon="account_circle" label-placeholder="账号" v-model="userName" class="inputClass"></vs-input>
                    </vs-col>
                </vs-row>
                <br />
                <vs-row>
                    <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="12">
                        <vs-input icon-no-border icon="remove_red_eye" label-placeholder="密码" v-model="userPsw" class="inputClass"></vs-input>
                    </vs-col>
                </vs-row>
                <vs-divider />
                <!--todo 添加用户功能-->
                <vs-row>
                    <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="12">
                        <vs-button color="primary" type="relief" @click="addUser">添加用户</vs-button>
                    </vs-col>
                </vs-row>

            </vs-popup>
        </template>

        <template slot="thead">
            <vs-th class="tableHead"></vs-th>
            <vs-th class="tableHead">UID</vs-th>
            <vs-th class="tableHead">用户名</vs-th>
            <vs-th class="tableHead">注册时间</vs-th>
            <vs-th class="tableHead">上次登陆时间</vs-th>
            <vs-th class="tableHead"></vs-th>
        </template>

        <template slot-scope="{data}">
            <vs-tr :data="tr" :key="index" v-for="(tr, index) in data">
                <vs-avatar />
                <vs-td :data="data[index].id">{{ data[index].uid }}</vs-td>
                <vs-td :data="data[index].name">{{ data[index].username }}</vs-td>
                <vs-td :data="data[index].time">{{ data[index].regtime }}</vs-td>
                <vs-td :data="data[index].lastTime">{{ data[index].lastlogin }}</vs-td>
                <vs-td>
                    <vs-button radius size="small" icon="delete" color="danger" @click="removeUser(index)" v-if="data[index].username!=='admin'"></vs-button>
                </vs-td>
            </vs-tr>
        </template>

    </vs-table>

</div>
</template>

<script>
export default {
    name: 'member-management',
    data() {
        return {
            addPerson: false,
            userName: '',
            userPsw: '',
            /** 新建用户的用户名是否已经存在 */
            exist: false,
            /** 返回状态判断 */
            userState: 2,
            addUserState: 2,
            removeUserState: 2,
            /** 删除用户在成员列表里的索引 */
            userIndex: '',
            /** 用户列表 */
            users: [{
                uid: '1',
                username: 'dalao',
                regtime: '2019',
                lastlogin: '2020'
            }, {
                uid: '2',
                username: 'sdlwsl',
                regtime: '2019',
                lastlogin: '2020'
            }],
            userNamePattern: /^[0-9a-zA-Z_]{1,18}$/,
            userPswPattern: /^[0-9a-zA-Z]{6,18}$/,
            chinese: /^[\u4E00-\u9FA5]+$/
        }
    },
    watch: {
        userState(val, oldVal) {
            if (val === 0) {
                this.$vs.loading.close('#management> .con-vs-loading')
            }
        }
    },
    mounted() {
        /** 页面加载完成后调用 */
        this.getUserList()
        this.openLoading()
    },
    methods: {
        /**
         * 获取用户列表
         */
        getUserList() {
            this.$axios({
                url: this.$prefix + 'user/memberlist',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token
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
                this.userState = res.data.state
                this.users = res.data.list
                this.processUsers()
                this.toNormalTime()
                this.toNormalTime2()
            }).catch(res => {
                console.log(res)
            })
        },
        addUser() {
            console.log(this.userName);
            console.log(this.userPsw);
            if (this.userName === '' || this.userPsw === '') {
                this.$vs.dialog({
                    color: 'warning',
                    title: '输入错误',
                    text: '用户名为空或密码为空',
                })
            } else if (!this.userNamePattern.test(this.userName) || !this.userPswPattern.test(this.userPsw) || this.chinese.test(this.userName) || this.chinese.test(this.userPsw)) {
                this.$vs.dialog({
                    color: 'warning',
                    title: '输入非法',
                    text: '用户名格式错误或者密码格式错误、正确用户名格式为包括数字，字母和下划线并且不超过18位、正确密码格式为包括数字，字母并且超过6位不超过18位的密码'
                })
            } else {
                if (this.users) {
                    // 如果用户列表存在
                    this.users.forEach(element => {
                        if (element.username === this.userName) {
                            this.exist = true
                        }
                    })
                } else {
                    this.$vs.dialog({
                        color: 'danger',
                        title: '危险',
                        text: '成员列表不存在'
                    })
                    console.log('成员列表不存在')
                }
            }

            if (!this.exist)
                this.addUserFunc()
        },
        addUserFunc() {
            if (!this.exist) {
                // 添加用户
                this.$axios({
                    url: this.$prefix + 'user/newmember',
                    method: 'post',
                    data: {
                        user: this.$store.state.userName,
                        token: this.$store.state.token,
                        newuser: this.userName,
                        newpw: this.$md5(this.userPsw + this.$str)
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
                    this.addUserState = res.data.state
                    this.processAddUser()
                }).catch(res => {
                    console.log(res)
                })
            }
        },
        removeUser(index) {
            // index 为被删除的用户 在数组中的索引
            console.log(index);
            this.userIndex = index;
            // 询问是否要删除此用户
            this.$vs.dialog({
                type: 'confirm',
                color: 'danger',
                title: `警告！是否确认删除用户${this.users[index].username}`,
                text: '点击 Accept 之后会删除该用户',
                accept: this.removeAccept
            })
        },
        /**
         * 确认删除用户后的方法
         */
        removeAccept() {
            this.$axios({
                url: this.$prefix + 'user/delmember',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    duid: this.users[this.userIndex].uid
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
                this.removeUserState = res.data.state
                this.processRemoveUser()
                this.users.splice(this.userIndex, 1)
            }).catch(res => {
                console.log(res)
            })
        },
        processUsers() {
            if (this.userState === 0) {
                
            } else if (this.userState === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误',
                    text: '身份验证错误'
                })
            } else if (this.userState === 1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误',
                    text: '其他错误'
                })
            }
        },
        processAddUser() {
            if (this.addUserState === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '成功',
                    text: '添加成员成功'
                })
                console.log('成功');
                // 更新列表
                this.userState = 2
                this.getUserList()
                this.userName = ''
                this.userPsw = ''
            } else if (this.addUserState === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误',
                    text: '身份验证错误'
                })
            } else if (this.addUserState === 1) {
                this.$vs.dialog({
                    color: 'warning',
                    title: '错误',
                    text: '其他错误（可能产生了重名）'
                })
            }
        },
        processRemoveUser() {
            if (this.removeUserState === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '成功',
                    text: '删除成员成功'
                })
            } else if (this.removeUserState === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误',
                    text: '身份验证错误'
                })
            } else if (this.removeUserState === 1) {
                this.$vs.dialog({
                    color: 'warning',
                    title: '错误',
                    text: '其他错误（产生了意料之外的错误）'
                })
            }
        },
        /**
         * 得到用户列表前 加载 loading
         */
        openLoading() {
            this.$vs.loading({
                container: '#management',
                type: 'material',
                scale: .8
            })
        },
        toNormalTime() {
            this.users.forEach(item => {
                item.regtime = this.transformTime(item.regtime)
            })
        },
        toNormalTime2() {
            this.users.forEach(item => {
                item.lastlogin = this.transformTime(item.lastlogin)
            })
        },
        transformTime(time) {
            let newTime = time * 1000
            let date = new Date(newTime + 8 * 3600 * 1000) // 加上8小时
            return date.toJSON().substr(0, 19).replace('T', ' ')
        }
    }
}
</script>

<style scoped>
.titleClass {
    height: 50px;
    padding: 10px 10px 0 10px;
}
</style><style>
.tableHead>div {
    justify-content: center;
}

.inputClass>div>i {
    font-size: 1.1rem;
}
</style>
