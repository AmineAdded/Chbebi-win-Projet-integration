<template>
  <HelloWorld/>
  <SignUp />
</template>

<script>
import { defineComponent } from "vue";

// Components
import SignUp from "../components/SignUp.vue";
import HelloWorld from "../components/HelloWorld.vue";
export default defineComponent({
  name: "HomeView",

  components: {
    SignUp,
    HelloWorld,
  },
});
</script>
