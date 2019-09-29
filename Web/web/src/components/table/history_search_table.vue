<template>
<div>
    <div>

        <div>
            <div class="container">
                <!-- 日期选择 -->
                <el-date-picker v-model="datePick" type="daterange" align="left" unlink-panels start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" style="margin-top:12px;" @change="getDate"></el-date-picker>

                <!-- 设备选择 -->
                <vs-select label="拍摄设备编号" v-model="defaultDevIdSelect" style="margin-left:3%;">
                    <vs-select-item :key="index" :value="item.value" :text="item.deviceID" v-for="(item, index) in selectDeviceOp"></vs-select-item>
                </vs-select>
                <!-- 程度选择 -->
                <vs-select label="分类" v-model="defaultClassifySelect" style="margin-left:3%;">
                    <vs-select-item :key="index" :value="item.value" :text="item.classify" v-for="(item, index) in selectClassifyOP"></vs-select-item>
                </vs-select>

                <vs-button icon="check" size="small" @click="queryTotalMessage" style="margin-top:15px;margin-left:2%;">确认</vs-button>
            </div>
        </div>
        <vs-divider />

        <vs-row vs-justify="center">
            <vs-col type="flex" vs-justify="center" vs-w="12">
                <vs-card>
                    <div>
                        <vs-table stripe :data="historyPhoto">

                            <template slot="thead">
                                <vs-th class="tableHead">图片</vs-th>
                                <vs-th class="tableHead">照片序号</vs-th>
                                <vs-th class="tableHead">拍摄时间</vs-th>
                                <vs-th class="tableHead">拍摄设备</vs-th>
                                <vs-th class="tableHead">分类</vs-th>
                            </template>

                            <template slot-scope="{data}">
                                <vs-tr :data="tr" :key="index" v-for="(tr, index) in data">
                                    <img :src="data[index].url" alt="图片" />
                                    <vs-td>{{ data[index].id}}</vs-td>
                                    <vs-td>{{ data[index].time}}</vs-td>
                                    <vs-td>{{ data[index].devid}}</vs-td>
                                    <vs-td>{{ data[index].type}}</vs-td>
                                </vs-tr>
                            </template>

                        </vs-table>
                    </div>
                </vs-card>
            </vs-col>
        </vs-row>

    </div>
</div>
</template>

<script>
export default {
    name: 'history-search-table',
    data() {
        return {
            defaultDevIdSelect: 1,
            defaultClassifySelect: 1,
            datePick: '',
            startDate: '',
            endDate: '',
            picPF: 'http://127.0.0.1:8080/pic/',
            queryState: 2,
            deviceStatus: 2,
            /** 设备下拉框数组 */
            selectDeviceOp: [],
            /** 分类下拉框数组 */
            selectClassifyOP: [{
                    classify: '人',
                    value: 0
                },
                {
                    classify: '猫',
                    value: 1
                },
                {
                    classify: '其他',
                    value: 2
                },
                {
                    classify: '所有',
                    value: -1
                }
            ],
            historyPhoto: [/*{
                    id: '',
                    type: '',
                    url: '',
                    devid: '',
                    time: '',
                }*/
            ],
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
    mounted() {
        this.queryDeviceList()
    },
    watch: {
        
    },
    methods: {
        getDate() {
            this.startDate = this.datePick[0]
            this.endDate = this.datePick[1]
            console.log(this.startDate.getTime()/1000)
            console.log(this.endDate.getTime()/1000)
        },
        queryTotalMessage() {
            this.$axios({
                url: this.$prefix + 'picmessage/query',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    starttime: this.startDate.getTime()/1000,
                    endtime: this.endDate.getTime()/1000,
                    devid: this.onlineDevice[this.defaultDevIdSelect-1].devid,
                    type: this.defaultClassifySelect
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
                this.queryState = res.data.state
                this.historyPhoto = res.data.list
                this.processQueryState()
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
        processQueryState() {
            if (this.queryState === 0) {
                this.$vs.dialog({
                    color: 'success',
                    title: '查询成功',
                    text: '查询历史照片成功'
                })
                this.historyPhoto.forEach(item => {
                    item.time = this.transformTime(item.time)
                    item.type = this.transformClassify(item.type)
                })
            } else if (this.queryState === 1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '危险',
                    text: '身份验证失败'
                })
            } else if (this.queryState === -1) {
                this.$vs.dialog({
                    color: 'warning',
                    title: '警告',
                    text: '参数错误'
                })
            }
        },
        processDeState() {
            if (this.deviceStatus === 0) {
                
                let that = this
                let val = 1
                this.onlineDevice.forEach(items => {
                    that.selectDeviceOp.push({
                        deviceID: items.devid,
                        value: val
                    })
                    val++
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
        transformClassify(classify) {
            if (classify === 0) {
                return '人'
            } else if (classify === 1) {
                return '猫'
            } else if (classify === 2) {
                return '其他'
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

.container {
    width: 100%;
    display: flex;
    display: -webkit-flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: flex-start;
}
</style>
