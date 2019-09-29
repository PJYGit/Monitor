<template>
<div id="app">
    <transition name="transitionRouter">
        <router-view></router-view>
    </transition>
</div>
</template>

<script>
export default {}
</script>

<style>
#app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
}

#nav {
    padding: 30px;
}

#nav a {
    font-weight: bold;
    color: #2c3e50;
}

#nav a.router-link-exact-active {
    color: #42b983;
}

.transitionRouter-enter-active,
.transitionRouter-leave-active {
    transition: opacity 1s;
}

.transitionRouter-enter,
.transitionRouter-leave {
    opacity: 0;
}

/* 滚动槽 */
::-webkit-scrollbar {
    width: 6px;
    height: 6px;
}

::-webkit-scrollbar-track {
    border-radius: 3px;
    background: rgba(0, 0, 0, 0.06);
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.08);
}

/* 滚动条滑块 */
::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background: rgba(0, 0, 0, 0.12);
    box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.2);
}
</style>
