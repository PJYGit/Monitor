<template>
<div>
    <div>
        <vec-title></vec-title>
        <vec-menu :menu-index="pageIndex"></vec-menu>
    </div>
    <div class="bodyClass">
        <div>
            <vs-alert active="true">
                <h2>最新照片</h2>
            </vs-alert>
        </div>
        
        <br />
        <vs-divider />
        
        <new-photo-table></new-photo-table>
    </div>
</div>
</template>

<script>
import VecTitle from '../components/base/vec_title'
import VecMenu from '../components/base/vec_menu'
import NewPhotoTable from '../components/table/new_photo_table'
export default {
    name: 'new-photo',
    data() {
        return {
            pageIndex: '2'
        }
    },
    components: {
        NewPhotoTable,
        VecMenu,
        VecTitle
    }
}
</script>

<style scoped>
.bodyClass {
    margin: 5rem 5% 5% 18%;
}

.centerList {
    display: block;
}

.centerList li {
    float: left;
    list-style: none;
}

.checkBoxClass>span>span>i {
    font-size: 0.9rem;
}
</style>
