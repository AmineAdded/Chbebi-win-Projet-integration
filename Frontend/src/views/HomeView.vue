<template>
  <Navbar/>
  <SignUp />
</template>

<script>
import { defineComponent } from "vue";

// Components
import SignUp from "../components/SignUp.vue";
import Navbar from "../components/Navbar.vue";
export default defineComponent({
  name: "HomeView",

  components: {
    SignUp,
    Navbar,
  },
});
</script>
