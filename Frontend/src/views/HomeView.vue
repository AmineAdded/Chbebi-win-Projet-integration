<template>
  <Navbar />
  <About @openSignUp="showSignUp = true" />
  
  <v-dialog v-model="showLogin" persistent width="auto">
    <Login @closeLogin="showLogin = false, showSignUp = true" />
  </v-dialog>

  <v-dialog v-model="showSignUp" persistent width="auto">
    <SignUp @closeSignUp="showSignUp = false" @openLogin="showLogin = true, showSignUp = false" />
  </v-dialog>

  <Footer />
</template>

<script>
import { defineComponent } from "vue";
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
  data() {
    return {
      showSignUp: false,
      showLogin: false,
    };
  },
});
</script>
<style>
.v-dialog {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
</style>
