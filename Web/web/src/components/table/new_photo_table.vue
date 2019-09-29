<template>
<div>

    <div>
        <ul class="centerList">
            <li>
                <vs-checkbox size="large" icon="face" color="danger" vs-value="人" v-model="classifyItem">人</vs-checkbox>
            </li>
            <li>
                <vs-checkbox size="large" icon="pets" color="warning" vs-value="猫" v-model="classifyItem">猫</vs-checkbox>
            </li>
            <li>
                <vs-checkbox size="large" icon="report" color="warning" vs-value="其他" v-model="classifyItem">其他</vs-checkbox>
            </li>
        </ul>
        <vs-button icon="arrow_right_alt" size="small" to="/history_search" style="margin-bottom: 10px;margin-left:90%;">历史查询</vs-button>
    </div>

    <div class="tableBody">
        <vs-row vs-justify="center">
            <vs-col type="flex" vs-justify="center" vs-w="12">
                <vs-card>
                    <div id="management" class="vs-con-loading__container">
                        <vs-table stripe :data="newPhoto">

                            <template slot="thead">
                                <vs-th class="tableHead">图片</vs-th>
                                <vs-th class="tableHead">图片ID</vs-th>
                                <vs-th class="tableHead">拍摄时间</vs-th>
                                <vs-th class="tableHead">拍摄设备</vs-th>
                                <vs-th class="tableHead">分类</vs-th>
                            </template>

                            <template slot-scope="{data}">
                                <vs-tr :key="index" v-for="(tr, index) in data" v-if="inClassify(`${data[index].type}`)">
                                    <!-- <img :src="require(`${data[index].url}`)" alt="图片" /> -->
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
    name: 'new-photo-table',
    data() {
        return {
            ws: null,
            picPF: 'http://127.0.0.1:8080/pic/',
            sendTimer: null,
            sendCount: 0,
            classifyItem: ['人','猫','其他'],
            newPhoto: [{
                id: '',
                type: '',
                url: '',
                devid: '',
                time: ''
            }]
        }
    },
    created() {
        this.initWebSocket()
    },
    beforeDestroy() {
        this.WebSocketOnClose()
    },
    methods: {
        initWebSocket() {
            console.log(this.$prefix.split('/'));
            this.ws = new WebSocket('ws://' + this.$prefix.split('/')[2] + '/server/picsoc')
            this.ws.onopen = this.WebSocketOnOpen
            this.ws.onerror = this.WebSocketOnError
            this.ws.onmessage = this.WebSocketOnMessage
            this.ws.onclose = this.WebSocketOnClose
        },
        WebSocketOnOpen() {
            console.log('@open')
            console.log('WebSocket连接成功')
            console.log("WebSocket连接成功" + '   状态码：' + this.ws.readyState)
            this.ws.send(this.$store.state.token + this.$store.state.userName)
        },
        WebSocketOnError(e) {
            console.log('@error')
            console.log('WebSocket连接发送错误')
        },
        WebSocketOnMessage(e) {
            console.log('@message')
            let ipt = JSON.parse(e.data)
            
            if (ipt.type == '人')
                this.$vs.notify({
                    color: 'danger',
                    title: '你收到一条新消息',
                    text: `${ipt.type}`,
                    icon: 'face'
                })
            else if (ipt.type == '猫')
                this.$vs.notify({
                    color: 'danger',
                    title: '你收到一条新消息',
                    text: `${ipt.type}`,
                    icon: 'pets'
                })
            else if (ipt.type == '其他') 
                this.$vs.notify({
                    color: 'danger',
                    title: '你收到一条新消息',
                    text: `${ipt.type}`,
                    icon: 'report'
                })
            
            ipt.time = this.transformTime(ipt.time)
            ipt.type = this.transformClassify(ipt.type)
            this.newPhoto.unshift(ipt)

            console.log(this.newPhoto);
        },
        WebSocketOnClose(e) {
            console.log('@close')
        },
        send(message) {
            if (this.ws && this.ws != null) {
                this.ws.send(message)
                console.log("发送的消息：" + message);
            }
        },
        inClassify(val) {
            if (this.classifyItem.indexOf(val) !== -1) {
                return true
            } else {
                return false
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
.centerList {
    display: block;
}

.centerList li {
    float: left;
    list-style: none;
}
</style><style>
.tableHead>div {
    justify-content: center;
}
</style>
