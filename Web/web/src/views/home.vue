<template>
<div style="background-color: white; width: 100%; height: 100%;">
    <div>
        <vec-title></vec-title>
        <vec-menu v-bind:menu-index="pageIndex"></vec-menu>
    </div>
    <div class="body">
        <div style="margin-bottom: 20px;">
            <home-body-header></home-body-header>
        </div>

        <vs-row vs-justify="center">
            <vs-col type="flex" vs-justify="center" vs-w="12">
                <vs-card actionable>
                    <div slot="header">
                        在线设备
                    </div>
                    <div>
                        <home-online-device></home-online-device>
                    </div>
                </vs-card>
            </vs-col>
        </vs-row>

        <vs-row vs-justify="center">
            <vs-col type="flex" vs-justify="center" vs-w="12">
                <vs-card>
                    <div slot="header">
                        历史照片列表
                    </div>
                    <div>
                        <home-new-photo></home-new-photo>
                    </div>
                </vs-card>
            </vs-col>
        </vs-row>
    </div>
</div>
</template>

<script>
// @ is an alias to /src
import VecTitle from '../components/base/vec_title'
import VecMenu from '../components/base/vec_menu'
import Member_management from '../components/table/member_management'
import HomeOnlineDevice from '../components/table/home_online_device'
import HomeNewPhoto from '../components/table/home_new_photo'
import HomeBodyHeader from './home_body_header'

export default {
    name: 'home',
    data() {
        return {
            personType: 0,
            pageIndex: '1'
        }
    },
    components: {
        HomeBodyHeader,
        HomeNewPhoto,
        HomeOnlineDevice,
        Member_management,
        VecMenu,
        VecTitle,
    },
    methods: {

    }
}
</script>

<style scoped>
.body {
    margin: 5rem 5% 5% 18%;
    height: 100%;
    bottom: 0;
}
</style>
