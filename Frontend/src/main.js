import './plugins/axios'
import { createApp } from "vue";
import App from "./App.vue";
import { createPinia } from 'pinia';
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import { loadFonts } from "./plugins/webfontloader";

loadFonts();

createApp(App).use(router).use(store).use(vuetify).use(createPinia()).mount("#app");
