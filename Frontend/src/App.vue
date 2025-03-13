<template>
  <v-app>
    <v-main style="font-family: 'Segoe UI', sans-serif;">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
      <div class="chatbot-icon">
      <v-btn icon color="black" @click="showChatbot = !showChatbot">
        <v-icon>mdi-robot</v-icon>
      </v-btn>
      <Chatbot v-if="showChatbot" />
    </div>
    </v-main>
  </v-app>
</template>

<script>
import { defineComponent } from 'vue';
import Chatbot from './components/Chatbot.vue';

export default defineComponent({
  name: "App",
  components:{
    Chatbot,
  },
  data: () => ({
    showChatbot: false,
  }),
});
</script>
<style>
.chatbot-icon {
  position: fixed;
  bottom: 20px;
  right: 20px;
  /* background: #ffffff; */
  padding: 10px;
  border-radius: 50%;
  /* box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); */
}
</style>
