<template>
  <Navbar/>
  <SignUp />
  <Footer />
</template>

<script>
import { defineComponent } from "vue";

// Components
import SignUp from "../components/SignUp.vue";
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
export default defineComponent({
  name: "HomeView",

  components: {
    SignUp,
    Navbar,
    Footer,
  },
});
</script>
