<template>
  <Navbar />
  <About />
  <Login />
  <SignUp />
  <Footer />
</template>

<script>
import { defineComponent } from "vue";

// Components
import Login from "../components/Login.vue";
import SignUp from "../components/SignUp.vue";
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import About from "../components/About.vue";
export default defineComponent({
  name: "HomeView",

  components: {
    Login,
    SignUp,
    Navbar,
    Footer,
    About,
  },
});
</script>
