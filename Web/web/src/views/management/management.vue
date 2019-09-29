<template>
    <div style="background-color: #F6F6F6; width: 100%; height: 100%; position: fixed;">
        <div>
            <vec-title></vec-title>
            <vec-menu v-bind:menu-index="menuIndex"></vec-menu>
        </div>
        <div class="bodyClass">
            <member-management class="tableClass"></member-management>
        </div>
    </div>
</template>

<script>
import MemberManagement from '../../components/table/member_management'
import VecTitle from '../../components/base/vec_title'
import VecMenu from '../../components/base/vec_menu'
export default {
    components: { VecMenu, VecTitle, MemberManagement },
    data () {
        return {
            menuIndex: "5"
        }
    },
    methods: {

    }
}
</script>

<style scoped>
.tableClass {
    margin: 5rem 5% 5% 18%;
    width: 80%;
    box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.16);
}
</style>
