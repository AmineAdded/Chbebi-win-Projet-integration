<template>
  <v-footer color="white" padless class="foot">
    <v-row justify="center" no-gutters>
      <v-col class="text-center py-4" cols="12">
        <v-btn
          v-for="(icon, index) in icons"
          :key="index"
          :href="icon.link"
          target="_blank"
          class="mx-2 transparent-btn"
          text
        >
          <img :src="icon.src" :alt="icon.alt" height="90" width="90" />
        </v-btn>
      </v-col>
    </v-row>
  </v-footer>
</template>
  
  <script>
import facebook from "../assets/facebook.png";
import insta from "../assets/insta.png";
import whats from "../assets/whatsapp.png";

export default {
  name: "Footer",
  data() {
    return {
      icons: [
        { src: facebook, alt: "Facebook", link: "#" },
        { src: insta, alt: "Instagram", link: "#" },
        { src: whats, alt: "WhatsApp", link: "#" },
      ],
    };
  },
};
</script>

<style scoped>
.v-footer {
    background-color: white;
}

.transparent-btn {
  background-color: transparent !important; /* Supprime le fond des boutons */
  box-shadow: none !important; /* Supprime l'ombre des boutons */
}

.transparent-btn:hover {
  opacity: 0.8; /* Effet de survol */
}
.foot {
    box-shadow: 1px 1px 5px 0px rgba(6, 0, 0, 0.878);
    padding-bottom: 3%;
}
</style>