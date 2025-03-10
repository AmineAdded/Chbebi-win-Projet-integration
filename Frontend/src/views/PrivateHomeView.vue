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
            <div class="events-wrapper mt-4 mb-4">
                <EventsWorkshops />
            </div>
            
            <!-- Slide Section -->
            <div class="slide-wrapper mb-4">
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
/* .main-container {
    background: linear-gradient(135deg, #e3f2fd 0%, #91B5C9 100%);
    min-height: 100vh;
    padding: 20px;
    max-width: 1350px;
    margin: 0 auto;
} */

.content-wrapper {
    display: flex;
    background-color: #b2cfe0;
    justify-content: space-between;
    width: 100%;
    margin: 0 auto 20px auto;
    gap: 20px;
    direction: rtl;
    padding: 15px;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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