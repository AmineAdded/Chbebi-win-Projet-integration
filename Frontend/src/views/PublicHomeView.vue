<template>
  <div v-if="isLoading" class="loader-container">
      <div class="loader-content">
        <div class="custom-loader">
          <div class="pulse-ring"></div>
          <div class="pulse-dot"></div>
        </div>
        <div class="mt-4 text-center loading-text">جاري التحميل...</div>
      </div>
    </div>
  <div v-else class="main-container">
    <Navbar />
    
    <About @openSignUp="showSignUp = true" />

    <v-dialog v-model="showLogin" persistent width="auto">
      <Login @closeLogin="showLogin = false, showSignUp = true" @openSignUp="showLogin = false,showSignUp= true "/>
    </v-dialog>

    <v-dialog v-model="showSignUp" persistent width="auto">
      <SignUp @closeSignUp="showSignUp = false" @openLogin="showLogin = true, showSignUp = false" />
    </v-dialog>
    <EventsWorkshops/>
    <Slide />
    <Contact @openRate="showRate = true"/>
    <v-dialog v-model="showRate" persistent max-width="600px">
      <Rate @closeRate="showRate = false" />
    </v-dialog>
    <Footer />
  </div>

</template>

<script>
import { defineComponent } from "vue";
import Login from "../components/Login.vue";
import SignUp from "../components/SignUp.vue";
import EventsWorkshops from "@/components/EventsWorkshops.vue";
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import About from "../components/About.vue";
import Contact from "@/components/Contact.vue";
import Slide from "@/components/Slide.vue";
import Rate from "@/components/Rate.vue";

export default defineComponent({
  name: "HomeView",
  components: {
    EventsWorkshops,
    Login,
    SignUp,
    Navbar,
    Footer,
    About,
    Contact,
    Slide,
    Rate,
  },
  data() {
    return {
      showSignUp: false,
      showLogin: false,
      isLoading: true,
      showRate: false,
    };
  },
  created() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  },
});
</script>
<style scoped>
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
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background: linear-gradient(135deg, #e3f2fd, #bbdefb);
    z-index: 1001;
  }
  
  .loading-text {
    color: #1565c0;
    font-size: 1.25rem;
    font-weight: 500;
  }
  .custom-loader {
    position: relative;
    width: 100px;
    height: 100px;
  }
  @keyframes pulse {
    0% { transform: scale(0.8); opacity: 0.3; }
    50% { transform: scale(1.2); opacity: 0.6; }
    100% { transform: scale(0.8); opacity: 0.3; }
  }
  
  .pulse-ring {
    position: absolute;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: rgba(25, 118, 210, 0.2);
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation: pulse 2s ease-out infinite;
  }
  
  .pulse-dot {
    position: absolute;
    width: 30px;
    height: 30px;
    border-radius: 50%;
    background-color: #1976d2;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
</style>
