<template>
  <div v-if="isLoading" class="loader-container">
    <div class="loader-content">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
    </div>
  </div>
  <div v-else>
    <Navbar />
    <About @openSignUp="showSignUp = true" />

    <v-dialog v-model="showLogin" persistent width="auto">
      <Login @closeLogin="showLogin = false, showSignUp = true" />
    </v-dialog>

    <v-dialog v-model="showSignUp" persistent width="auto">
      <SignUp @closeSignUp="showSignUp = false" @openLogin="showLogin = true, showSignUp = false" />
    </v-dialog>

    <Footer />
  </div>

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
      isLoading: true,
    };
  },
  created() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  },
});
</script>
<style>
.v-dialog {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

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
</style>
