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
    <Navbar @openUpdateAccount="showUpdateAccount = true" />

    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid>
      <Quote />

      <!-- Personality and Agenda Row -->
      <div class="content-wrapper">
        <div class="personality-section">
          <Personality />
        </div>
        <div class="agenda-section">
          <Agenda />
        </div>
      </div>

      <!-- Events and Workshops Section -->
      <div class="events-wrapper">
        <EventsWorkshops />
      </div>

      <!-- Slide Section -->
      <div class="slide-wrapper">
        <Slide />
      </div>

      <Contact @openRate="showRate = true" />
      <v-dialog v-model="showRate" persistent max-width="600px">
        <Rate @closeRate="showRate = false" />
      </v-dialog>
    </v-container>
    <Footer />
  </div>
</template>

<script>
import { defineComponent } from "vue";
import EventsWorkshops from "@/components/EventsWorkshops.vue";
import Quote from "@/components/quote.vue";
import Agenda from "@/components/Agenda.vue";
import Personality from "@/components/PersonalitySection.vue";
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import Contact from "@/components/Contact.vue";
import Slide from "@/components/Slide.vue";
import Rate from "@/components/Rate.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";

export default defineComponent({
  name: "PrivateHomeView",
  components: {
    EventsWorkshops,
    Quote,
    Agenda,
    Personality,
    Navbar,
    Footer,
    Contact,
    Slide,
    Rate,
    UpdateAccount
  },
  data() {
    return {
      showSignUp: false,
      showLogin: false,
      isLoading: true,
      showRate: false,
      showUpdateAccount: false,
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
.main-container {
  padding: 0;
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
  0% {
    transform: scale(0.8);
    opacity: 0.3;
  }

  50% {
    transform: scale(1.2);
    opacity: 0.6;
  }

  100% {
    transform: scale(0.8);
    opacity: 0.3;
  }
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

.content-wrapper {
  display: flex;
  background-color: #b2cfe0;
  justify-content: space-between;
  margin: 0;
  gap: 20px;
  direction: rtl;
  padding: 80px;
}

.personality-section,
.agenda-section {
  flex: 1;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  max-height: 300px;
  background-color: white;
}

.events-wrapper {
  width: 100%;
  direction: rtl;
}

.slide-wrapper {
  width: 100%;
}

/* Add styling for the Quote component to match the image */
:deep(.quote-container) {
  margin-bottom: 20px;
  background-color: white;
  border-radius: 10px;
  padding: 15px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Update for mobile responsiveness */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
    padding: 10px;
  }

  .personality-section,
  .agenda-section {
    max-height: none;
  }
}
</style>