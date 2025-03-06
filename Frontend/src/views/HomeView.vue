<template>
  <SignUp />
</template>

<script>
import { defineComponent } from "vue";

// Components
import SignUp from "../components/SignUp.vue";

export default defineComponent({
  name: "HomeView",

  components: {
    SignUp,
  },
});
</script>
