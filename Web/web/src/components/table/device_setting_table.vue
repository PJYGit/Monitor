<template>
<div>
    <vs-table stripe :data="onlineDevice">

        <template slot="thead">
            <vs-th class="tableHead"></vs-th>
            <vs-th class="tableHead">MAC</vs-th>
            <vs-th class="tableHead">设备序号</vs-th>
            <vs-th class="tableHead">设备状态</vs-th>
            <vs-th class="tableHead">IP地址</vs-th>
            <vs-th class="tableHead">设备启动时间</vs-th>
            <vs-th class="tableHead">设备上次心跳时间</vs-th>
            <vs-th class="tableHead"></vs-th>
        </template>

        <template slot-scope="{data}">
            <vs-tr :data="tr" :key="index" v-for="(tr, index) in data">
                <vs-td><vs-avatar icon="camera_alt"></vs-avatar></vs-td>
                <vs-td>{{ data[index].mac }}</vs-td>
                <vs-td>{{ data[index].devid }}</vs-td>
                <vs-td>{{ data[index].state }}</vs-td>
                <vs-td>{{ data[index].ip }}</vs-td>
                <vs-td>{{ data[index].time }}</vs-td>
                <vs-td>{{ data[index].lastbeat }}</vs-td>
                <vs-td>
                    <vs-button radius size="small" icon="settings" @click="$set(settingActive,index,true)"></vs-button>
                    <vs-popup title="设备管理" :active.sync="settingActive[index]">

                        <vs-row>
                            <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="12">
                                <vs-button v-if="data[index].state!='设备离线'"  @click="shupDownDevice(index)">关机</vs-button>
                                <vs-button v-if="data[index].state=='设备离线'" disabled>关机</vs-button>
                            </vs-col>
                        </vs-row>
                        <vs-divider />
                        <vs-row>
                            <vs-col vs-type="flex" vs-justify="center" vs-align="center" vs-w="12">
                                <vs-button v-if="data[index].state=='设备正常'" icon="pause" @click="pauseDevice(index)" style="margin-right:10px;">暂停设备</vs-button>
                                <vs-button v-if="data[index].state!='设备正常'" disabled icon="pause" style="margin-right:10px;">暂停设备</vs-button>
                                <vs-button v-if="data[index].state=='设备暂停'" icon="play_arrow" @click="restartDevice(index)">重新启动设备</vs-button>
                                <vs-button v-if="data[index].state!='设备暂停'" disabled icon="play_arrow">重新启动设备</vs-button>
                            </vs-col>
                        </vs-row>

                    </vs-popup>
                </vs-td>
            </vs-tr>
        </template>
    </vs-table>
</div>
</template>

<script>
export default {
    name: 'device-setting-table',
    data() {
        return {
            settingActive: [],
            deviceStatus: 2,
            deviceChangeStatus: 2,
            onlineDevice: [{
                mac: '',
                devid: '',
                state: '',
                ip: '',
                time: '',
                lastbeat: ''
            }],
            devState: []
        }
    },
    mounted() {
        /** 页面加载完成后调用 */
        this.queryDeviceList()
    },
    methods: {
        shupDownDevice(index) {
            console.log('device ID ' + index)
            this.$axios({
                url: this.$prefix + 'device/statechange',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    devid: this.onlineDevice[index].devid,
                    op: 0
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
                this.deviceChangeStatus = res.data.state
                this.processDeChangeState(index)
            }).catch(res => {
                console.log(res)
            })
        },
        pauseDevice(index) {
            console.log('device ID ' + index)
            this.$axios({
                url: this.$prefix + 'device/statechange',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    devid: this.onlineDevice[index].devid,
                    op: 1
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
                this.deviceChangeStatus = res.data.state
                this.processDeChangeState(index)
            }).catch(res => {
                console.log(res)
            })
        },
        restartDevice(index) {
            console.log('device index ' + index);
            this.$axios({
                url: this.$prefix + 'device/statechange',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    devid: this.onlineDevice[index].devid,
                    op: 2
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
                this.deviceChangeStatus = res.data.state
                this.processDeChangeState(index)
            }).catch(res => {
                console.log(res)
            })
        },
        queryDeviceList() {
            this.$axios({
                url: this.$prefix + 'device/devicelist',
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
                this.deviceStatus = res.data.state
                this.onlineDevice = res.data.list
                this.processDeState()
            }).catch(res => {
                console.log(res)
            })
        },
        processDeState() {
            if (this.deviceStatus === 0) {
                this.onlineDevice.forEach(item => {
                    item.lastbeat = this.transformTime(item.lastbeat)
                    item.time = this.transformTime(item.time)
                    item.state = this.transformState(item.state)
                });
            } else if (this.deviceStatus === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '警告',
                    text: '身份验证错误'
                })
            }
        },
        processDeChangeState(index) {
            if (this.deviceChangeStatus === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '成功',
                    text: '成功'
                })
                this.settingActive[index] = false
                // 操作成功后重新获取设备列表
                this.queryDeviceList()
            } else if (this.deviceChangeStatus === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '危险',
                    text: '身份验证错误'
                })
            } else if (this.deviceChangeStatus === 1) {
                this.$vs.dialog({
                    color: 'warning',
                    title: '警告',
                    text: '其他错误（设备不存在或非法操作）'
                })
            }
        },
        devOnClose(index) {
            if (this.onlineDevice[index].type == '设备离线')
                return true
            else if (this.onlineDevice[index].type == '设备暂停'||this.onlineDevice[index].type == '设备正常')
                return false

        },
        transformTime(time) {
            let newTime = time * 1000
            let date = new Date(newTime + 8 * 3600 * 1000) // 加上8小时
            return date.toJSON().substr(0, 19).replace('T', ' ')
        },
        transformState(type) {
            if (type === 0) {
                return '设备离线'
            } else if (type === 1) {
                return '设备暂停'
            } else if (type === 2) {
                return '设备正常'
            }
        }
    }
}
</script>

<style scoped>
</style><style>
.tableHead>div {
    justify-content: center;
}

.inputClass>div>i {
    font-size: 1.1rem;
}
</style>
