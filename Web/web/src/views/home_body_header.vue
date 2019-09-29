<template>
<div>
    <vs-row vs-justify="space-between">
        <vs-col type="flex" vs-justify="center" vs-align="center" vs-w="3.7">
            <!-- TODO 根据具体时间选择图片 时间提醒 -->
            <vs-card fixedHeight actionable>
                <div slot="header">
                    <h3>时间提醒</h3>
                </div>

                <div class="clockBody">
                    <div id="clock">
                        <p class="date">{{ clockDate }}</p>
                        <p class="time">{{ clockTime }}</p>
                    </div>
                </div>
            </vs-card>
        </vs-col>
        <vs-col type="flex" vs-justify="center" vs-align="center" vs-w="3.7">
            <!-- 个人资料 -->
            <vs-card fixedHeight actionable>
                <div slot="header">
                    <h3>
                        个人资料
                    </h3>
                </div>
                <div slot="media" class="iconClass">
                    <vs-avatar size="80px" color="primary" icon="portrait"></vs-avatar>
                </div>
                <br />
                <div id="management" class="vs-con-loading__container">
                    <vs-alert color="primary" active="true">
                        UID：<strong>{{ uid }}</strong>
                        <br />
                    </vs-alert>
                </div>
            </vs-card>
        </vs-col>
        <!-- 入侵物品 -->
        <vs-col type="flex" vs-justify="center" vs-align="center" vs-w="3.7">
            <vs-card fixedHeight actionable>
                <div slot="header">
                    <h3>
                        入侵物体图片
                    </h3>
                </div>
                <div slot="media" class="iconClass">
                    <vs-avatar size="80px" color="primary" icon="panorama"></vs-avatar>
                </div>
                <br />
                <div id="management" class="vs-con-loading__container">
                    <vs-alert color="primary" active="true">
                        总共照片数： <strong>{{ totalPic }}</strong>
                        <br />
                    </vs-alert>
                </div>
            </vs-card>
        </vs-col>
    </vs-row>
</div>
</template>

<script>
export default {
    name: 'home-body-header',
    data() {
        return {
            date: new Date(),
            hours: '',
            infoState: -1,
            uid: '',
            totalPic: '',
            clockTime: '',
            clockDate: '',
            week: ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']
        }
    },
    watch: {
        infoState(val, oldVal) {
            if (val === 0) {
                this.$vs.loading.close('#management> .con-vs-loading')
            }
        }
    },
    mounted() {
        /** 更新主页的时间 */
        setInterval(this.updateTime, 1000);
        this.updateTime()

        /** 获取主页信息 */
        this.getInfoMessage()
        this.openLoading()
    },
    methods: {
        getInfoMessage() {
            this.$axios({
                url: this.$prefix + 'user/info',
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
                this.infoState = res.data.state
                this.uid = res.data.uid
                this.totalPic = res.data.totalpic
                this.$store.state.pushAddr = res.data.newps
                this.processInfo()
            }).catch(res => {
                console.log(res)
            })
        },
        processInfo() {
            if (this.userState === 0) {

            } else if (this.userState === -1) {
                this.$vs.dialog({
                    color: 'danger',
                    title: '错误',
                    text: '身份验证错误'
                })
            }
        },
        updateTime() {
            let that = this
            let cd = new Date()
            that.clockTime = that.zeroPadding(cd.getHours(), 2) + ':' + that.zeroPadding(cd.getMinutes(), 2) + ':' + that.zeroPadding(cd.getSeconds(), 2)
            that.clockDate = that.zeroPadding(cd.getFullYear(), 4) + '-' + that.zeroPadding(cd.getMonth() + 1, 2) + '-' + that.zeroPadding(cd.getDate(), 2) + ' ' + that.week[cd.getDay()]
        },
        zeroPadding(num, digit) {
            let zero = ''
            for (let i = 0; i < digit; i++)
                zero += '0'
            return (zero + num).slice(-digit)
        },
        openLoading() {
            this.$vs.loading({
                container: '#management',
                type: 'material',
                scale: .8
            })
        }
    },
    beforeDestory() {
        clearInterval()
    }
}
</script>

<style scoped>
.imageClass {
    border-radius: .5rem !important;
    margin-top: 5%;
}

.iconClass {
    margin-top: 20%;
}

.clockBody {
    background: #0f3854 !important;
    background: -webkit-radial-gradient(center ellipse, #0a2e38 0%, #000000 70%);
    background: radial-gradient(ellipse at center, #0a2e38 0%, #000000 70%);
    background-size: 100%;
}

/* 时钟 */
#clock {
    font-family: 'Share Tech Mono', monospace;
    color: #ffffff;
    text-align: center;
    position: absolute;
    left: 50%;
    top: 50%;
    -webkit-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    color: #1f74ff;
    /*text-shadow: 0 0 2px #c79395, 0 0 2px #102a5b;*/
}

#clock .time {
    letter-spacing: 0.05em;
    font-size: 2.7em;
    padding: 5px 0;
}

#clock .date {
    letter-spacing: 0.1em;
    font-size: 24px;
}
</style>
