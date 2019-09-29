<template>
<div>
    <el-table :data="onlineDevice" border style="width: 100%;height:30%;overflow:scroll;overflow-x:hidden;">
        <el-table-column align="center" prop="mac" label="MAC"></el-table-column>
        <el-table-column align="center" prop="devid" label="编号"></el-table-column>
        <el-table-column align="center" prop="state" label="运行状态"></el-table-column>
        <el-table-column align="center" prop="ip" label="IP地址"></el-table-column>
        <el-table-column align="center" prop="time" label="启动时间"></el-table-column>
        <el-table-column align="center" prop="lastbeat" label="上次心跳时间"></el-table-column>
    </el-table>
</div>
</template>

<script>
export default {
    name: 'home-online-device',
    data() {
        return {
            onlineDevice: [{
                mac: '',
                devid: '',
                state: '',
                ip: '',
                time: '',
                lastbeat: ''
            }]
        }
    },
    mounted() {
        /** 页面加载完成后调用 */
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
                    item.time = this.transformTime(item.time)
                    item.lastbeat = this.transformTime(item.lastbeat)
                    item.state = this.transformState(item.state)
                });
            } else if (this.deviceStatus === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '警告',
                    text: '获取最新照片时身份验证错误'
                })
            }
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
.tableTitle {
    display: table-cell;
    font-size: 15px;
    padding: 5px;
    font-family: 黑体;
    font-weight: bold;

}

.tableCell {
    display: table-cell;
    font-size: 15px;
    padding: 5px;
}
</style>
