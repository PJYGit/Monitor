<template>
    <div style="background-color: #F6F6F6; width: 100%; height: 100%; position: fixed;">
        <div>
            <vec-title></vec-title>
            <vec-menu></vec-menu>
        </div>
        <div class="body">
            <vs-button @click="openAlert" color="primary" type="filled">TODO</vs-button>
        </div>
    </div>
</template>

<script>
import VecTitle from '../components/base/vec_title'
import VecMenu from '../components/base/vec_menu'

export default {
    name: 'home',
    data() {
        return {
            personType: 0,
            pageIndex: '5'
        }
    },
    components: {
        VecMenu,
        VecTitle
    }
}
</script>

<style scoped>
.home {
    display: block;
}
</style>
