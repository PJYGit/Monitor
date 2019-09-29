<template>
<div>
    <vs-row vs-justify="center">
        <vs-col type="flex" vs-justify="center" vs-align="center" vs-w="12">
            <vs-card>
                <div slot="header">
                    <h3>
                        在线设备
                    </h3>
                </div>

                <div id="management" class="vs-con-loading__container">
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
                                <vs-avatar icon="camera_alt"></vs-avatar>
                                <vs-td>{{ data[index].mac }}</vs-td>
                                <vs-td>{{ data[index].devid }}</vs-td>
                                <vs-td>{{ data[index].state }}</vs-td>
                                <vs-td>{{ data[index].ip }}</vs-td>
                                <vs-td>{{ data[index].time }}</vs-td>
                                <vs-td>{{ data[index].lastbeat }}</vs-td>
                            </vs-tr>
                        </template>

                    </vs-table>
                </div>

            </vs-card>
        </vs-col>
    </vs-row>
</div>
</template>

<script>
export default {
    name: 'online-device-table',
    data() {
        return {
            deviceStatus: 1,
            onlineDevice: [{
                    mac: '',
                    devid: '',
                    state: '',
                    ip: '',
                    time: '',
                    lastbeat: ''
                }
            ]
        }
    },
    watch: {
        deviceStatus(val, oldVal) {
            if (val === 0) {
                this.$vs.loading.close('#management> .con-vs-loading')
            }
        }
    },
    mounted() {
        this.queryDeviceList()
    },
    methods: {
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
        transformTime(time) {
            let newTime = time * 1000
            let date = new Date(newTime + 8 * 3600 * 1000) // 加上8小时
            return date.toJSON().substr(0, 19).replace('T', ' ')
        },
        transformState(state) {
            if (state === 0) {
                return '设备已离线'
            } else if (state === 1) {
                return '设备正常运行中'
            } else if (state === 2) {
                return '设备暂停运行'
            } else if (state === -1) {
                return '设备离线'
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
</style>
