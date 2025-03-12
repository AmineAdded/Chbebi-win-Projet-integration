<template>
    <div v-if="isLoading" class="loader-container">
        <div class="loader-content">
            <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
        </div>
    </div>
    <div v-else>
        <Navbar />
        <v-container fluid class="main-container">
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
            <div class="slide-wrapper ">
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
import EventsWorkshops from '@/components/EventsWorkshops.vue';
import Quote from '@/components/quote.vue';
import Agenda from "@/components/Agenda.vue";
import Personality from "@/components/PersonalitySection.vue";
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import Contact from "@/components/Contact.vue";
import Slide from "@/components/Slide.vue";
import Rate from "@/components/Rate.vue";

export default defineComponent({
    name: "HomeView",
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
.main-container{
    padding: 0;
}
.loader-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
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

.personality-section, .agenda-section {
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
    
    .personality-section, .agenda-section {
        max-height: none;
    }
}
</style>