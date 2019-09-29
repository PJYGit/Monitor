<template>
<div>
    <vs-table max-item="5" pagination stripe :data="historyPhoto" style="width:100%;height:20%;overflow:hidden;">

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
</template>

<script>
export default {
    name: 'home-new-photo',
    data() {
        return {
            state: 2,
            picPF: 'http://127.0.0.1:8080/pic/',
            historyPhoto: [{
                id: '1',
                type: '',
                url: '',
                devid: '',
                time: ''
            }]
        }
    },
    mounted() {
        this.queryHistoryPhoto()
    },
    methods: {
        queryHistoryPhoto() {
            this.$axios({
                url: this.$prefix + 'picmessage/query',
                method: 'post',
                data: {
                    user: this.$store.state.userName,
                    token: this.$store.state.token,
                    starttime: 0,
                    endtime: parseInt(new Date()/1000),
                    devid: -1,
                    type: -1
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
                this.historyPhoto = res.data.list
                this.processDeState()
            }).catch(res => {
                console.log(res)
            })
        },
        processDeState() {
            if (this.state === 0) {
                this.historyPhoto.forEach(item => {
                    item.time = this.transformTime(item.time)
                    item.type = this.transformClassify(item.type)
                })
            } else if (this.state === -1) {
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
</style>
