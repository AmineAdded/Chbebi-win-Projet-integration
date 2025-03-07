<template>
  <v-app-bar color="white">
    <v-btn text to="/agenda">أجندة</v-btn>
    <v-btn text to="/islam">دين الإسلام</v-btn>
    <v-btn text to="/culture">الثقافة التونسية</v-btn>
    <v-btn text to="/time-management">إدارة الوقت</v-btn>
    <v-spacer></v-spacer>
    <v-btn text to="">الصفحة الرئيسية</v-btn>
    <img :src="logo" alt="logo" height="63px" width="80px" />
  </v-app-bar>
</template>

<script>
import logo from '../assets/logo.png'; 

export default {
  name: "Navbar",
  data() {
    return {
      logo: logo,
    };
  },
};
</script>

<style scoped>
.v-btn {
  font-size: 22px;
  font-weight: bold;
  font-family: "Segoe UI", sans-serif;
  color: #152538;
  transition: color 0.3s ease-in-out;
}
</style>
