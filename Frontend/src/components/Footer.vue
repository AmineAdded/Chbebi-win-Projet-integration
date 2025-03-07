<template>
  <v-footer color="transparent" padless class="footer-container">
    <v-container>
      <v-row justify="center" align="center" class="py-6">
        <v-col cols="12" class="text-center">
          <div class="social-icons-wrapper">
            <v-btn
              v-for="(icon, index) in icons"
              :key="index"
              :href="icon.link"
              target="_blank"
              class="social-icon-btn mx-3"
              icon
              elevation="0"
            >
              <v-avatar size="56" class="social-avatar">
                <v-img 
                  :src="icon.src" 
                  :alt="icon.alt" 
                  height="32" 
                  width="32"
                  contain
                ></v-img>
              </v-avatar>
            </v-btn>
          </div>
          <div class="mt-6 subtitle">
            <span class="text-subtitle-1 font-weight-light">تابعونا على وسائل التواصل الاجتماعي</span>
          </div>
        </v-col>
      </v-row>
    </v-container>
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
.footer-container {
  background: linear-gradient(to bottom, #ffffff, #f5f5f5);
  border-top: 1px solid #eeeeee;
  box-shadow: 0px -2px 15px rgba(0, 0, 0, 0.05);
  padding: 20px 0;
}

.social-icons-wrapper {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
}

.social-icon-btn {
  transition: transform 0.3s ease, opacity 0.2s ease;
  margin: 0 12px;
}

.social-icon-btn:hover {
  transform: translateY(-5px);
  opacity: 0.9;
}

.social-avatar {
  background-color: white;
  padding: 12px;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.social-icon-btn:hover .social-avatar {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

.subtitle {
  color: #666666;
  letter-spacing: 0.5px;
}

@media (max-width: 600px) {
  .social-avatar {
    padding: 8px;
  }
  
  .social-icon-btn {
    margin: 0 8px;
  }
}
</style>