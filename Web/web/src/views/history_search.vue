<template>
<div>
    <div>
        <vec-title></vec-title>
        <vec-menu></vec-menu>
    </div>

    <div class="body">
        <div>

            <vs-button icon="arrow_back" size="small" to="/new_photo" style="float:left;">最新照片</vs-button>
            <h2>历史照片搜索</h2>

        </div>
        <vs-divider />
        <history-search-table></history-search-table>
    </div>
</div>
</template>

<script>
import VecTitle from '../components/base/vec_title'
import VecMenu from '../components/base/vec_menu'
import HistorySearchTable from '../components/table/history_search_table'
export default {
    name: 'history-search',
    data() {
        return {
            
        }
    },
    components: {
        HistorySearchTable,
        VecMenu,
        VecTitle
    },
    mounted() {
        
    },
    methods: {
        
    }
}
</script>

<style scoped>
.body {
    margin: 5rem 5% 5% 18%;
}
</style>
