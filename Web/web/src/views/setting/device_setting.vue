<template>
<div>
    <div>
        <vec-title></vec-title>
        <vec-menu :menu-index="pageIndex"></vec-menu>
    </div>
    <div class="body">
        <vs-row vs-justify="center">
            <vs-col type="flex" vs-justify="center" vs-w="10">
                <vs-card>
                    <div slot="header">
                        在线设备
                    </div>
                    <div>
                        <device-setting-table></device-setting-table>
                    </div>
                </vs-card>
            </vs-col>
        </vs-row>
    </div>
</div>
</template>

<script>
import VecTitle from '../../components/base/vec_title'
import VecMenu from '../../components/base/vec_menu'
import DeviceSettingTable from '../../components/table/device_setting_table'
export default {
    name: 'device-setting',
    data () {
        return {
            pageIndex: '3'
        }
    },
    components: {
        DeviceSettingTable,
        VecMenu,
        VecTitle
    }
}
</script>

<style scoped>
.body {
    margin: 5rem 5% 0 18%;
    height: 100%;
    bottom: 0;
}
</style>
