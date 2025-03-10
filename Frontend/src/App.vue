<template>
  <v-app>
    <v-main>
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
      <div class="chatbot-icon">
      <v-btn icon color="black">
        <v-icon>mdi-robot</v-icon>
      </v-btn>
    </div>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",

  data: () => ({
    //
  }),
};
</script>
<style>
.chatbot-icon {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background: #ffffff;
  padding: 10px;
  border-radius: 50%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}
</style>
