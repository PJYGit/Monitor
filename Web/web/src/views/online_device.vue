<template>
<div>
    <div>
        <vec-title></vec-title>
        <vec-menu :menu-index="pageIndex"></vec-menu>
    </div>

    <div class="bodyClass">
        <online-device-table></online-device-table>
    </div>
</div>
</template>

<script>
import VecTitle from '../components/base/vec_title'
import VecMenu from '../components/base/vec_menu'
import OnlineDeviceTable from '../components/table/online_device_table'
export default {
    name: 'online-device',
    data () {
        return {
            pageIndex: '3'
        }
    },
    components: {
        OnlineDeviceTable,
        VecMenu,
        VecTitle
    }
}
</script>

<style scoped>
    .bodyClass {
        margin: 5rem 5% 5% 18%;
    }
</style>
