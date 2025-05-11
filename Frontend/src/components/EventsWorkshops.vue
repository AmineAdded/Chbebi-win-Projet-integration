<template>
  <v-card class="events-card pa-6">
    <h2 class="section-title mb-4">الفعاليات وورش العمل القراب</h2>

    <div class="event-list">
      <div v-for="(event, index) in events" :key="index" class="event-item">
        <div class="event-card">
          <div class="event-content-wrapper">
            <div class="event-image mt-2">
              <img
                :src="'http://localhost:9090' + event.image"
                alt="Event image"
                class="rounded-lg"
              />
            </div>
            <div class="event-info">
              <div class="d-flex justify-space-between align-center">
                <div class="event-title font-weight-bold">{{ event.nom }}</div>
                <div class="event-date primary--text">
                  {{ formatDate(event.date) }}
                </div>
              </div>
              <div class="event-description mt-2 text-right">
                {{ event.description }}
              </div>
              <div class="mt-3 text-left">
                <v-btn
                  color="success"
                  class="learn-more white--text px-2"
                  :href="event.lien"
                  target="_blank"
                >
                  تعرف أكثر <v-icon small class="mr-1">mdi-chevron-left</v-icon>
                </v-btn>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
import eventService from "@/Services/eventService";

export default {
  name: "EventsWorkshops",
  data() {
    return {
      events: [],
    };
  },
  mounted() {
    this.loadEvents();
  },
  methods: {
    async loadEvents() {
      try {
        // Supprimer les événements expirés d'abord
        await eventService.deleteExpiredEvents();

        // Puis charger les événements restants
        const res = await eventService.getAllEvents();

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const validEvents = res.data.filter(event => {
          const eventDate = new Date(event.date);
          eventDate.setHours(0, 0, 0, 0);
          return eventDate >= today;
        });

        const sortedEvents = validEvents.sort((a, b) => new Date(a.date) - new Date(b.date));
        this.events = sortedEvents.slice(0, 2);
      } catch (error) {
        console.error("Erreur lors de la récupération des événements :", error);
      }
    },
    formatDate(date) {
      const d = new Date(date);
      return d.toLocaleDateString("ar-TN", {
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    },
  },
};
</script>

<style scoped>
.events-card {
  background: white !important;
}

.section-title {
  color: #333;
  font-size: 2.5rem;
  text-align: right;
  font-weight: bold;
  position: relative;
  padding-bottom: 10px;
  border-bottom: 2px solid #e0e0e0;
  width: 100%;
  margin-bottom: 20px;
  text-align: center;
}

.event-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.event-item {
  overflow: hidden;
}

.event-card {
  background: white;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.event-content-wrapper {
  display: flex;
  flex-direction: row;
}

.event-image {
  width: 40%;
  padding-right: 100px;
}

.event-image img {
  margin-top: 20px;
  width: 70%;
  height: 70%;
  object-fit: cover;
}

.event-info {
  width: 60%;
  padding: 20px;
  padding-left: 100px;
  padding-top: 50px;
}

.event-date {
  color: #4f46e5;
  font-weight: bold;
  font-size: 0.9rem;
}

.event-title {
  font-size: 1.1rem;
  font-weight: bold;
  color: #333;
}

.event-description {
  color: #666;
  font-size: 1rem;
  margin-top: 1px;
  font-weight: 600;
}

.learn-more {
  font-weight: 500;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .event-content-wrapper {
    flex-direction: column;
  }

  .event-info {
    width: 100%;
    padding: 20px;
  }

  .event-image {
    display: none;
  }
}
</style>