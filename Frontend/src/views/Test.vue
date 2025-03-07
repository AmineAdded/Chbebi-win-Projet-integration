<template>
    <div v-if="isLoading" class="loader-container">
        <div class="loader-content">
            <v-progress-circular
                indeterminate
                color="primary"
                size="64"
            ></v-progress-circular>
            <span class="loader-text">...جاري تحميل الاختبار </span>
        </div>
    </div>
    <div v-else>
        <h1>Hello world</h1>
    </div>
</template>
<script>
export default {
    data() {
        return {
            isLoading: true
        }
    },
    created() {
        setTimeout(() => {
            this.isLoading = false;
        }, 1000);
    },
}
</script>
<style scoped>
  .loader-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: rgba(25, 118, 210, 0.03);
    z-index: 1001;
    backdrop-filter: blur(8px);
}
.loader-text {
    color: #1976d2;
    font-size: 2em;
    font-weight: 500;
    animation: pulse 2s infinite;
    padding-left: 14px;
    padding-top: 6px;
}
</style>