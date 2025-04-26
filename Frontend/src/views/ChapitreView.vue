<template>
  <div class="Chapitre-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />
    
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid> 
      <h2 class="section-title">{{ thematicTitle }}</h2>
      <div class="cards-wrapper">
        
        <div v-for="(chapitre, index) in chapitres" :key="index" class="card">
          <router-link :to="{ name: 'SousChapitre', params: { chapitreId: chapitre.id, chapitreTitle: chapitre.title } }" class="card-link">
            <img :src="require('@/assets/' + chapitre.image)" alt="Chapitre chapitre" class="card-img" />
            <div class="card-body">
              <h3 class="card-title">{{ chapitre.title }}</h3>
              <p class="card-desc">{{ chapitre.description }}</p>
              
              <!-- Ajout de la barre de progression -->
              <div class="progress-container">
                <div class="progress-stats">
                  <div class="stats-label">إحصائيات التقدم</div>
                  <v-progress-linear
                    :color="getProgressColor(chapitre.pourcentage || 0)"
                    :buffer-value="100"
                    buffer-color="light-blue-lighten-4"
                    :model-value="chapitre.pourcentage || 0"
                    height="14"
                    rounded
                    striped
                  >
                    <template v-slot:default="{ value }">
                      <strong class="progress-value">{{ Math.ceil(value) }}%</strong>
                    </template>
                  </v-progress-linear>
                  <div class="progress-text">
                    <div class="progress-detailed">
                      {{ chapitre.pourcentage || 0 }}% مكتمل
                    </div>
                    <v-chip
                      :color="getStatusColor(chapitre.pourcentage || 0)"
                      size="small"
                      class="status-chip"
                      variant="outlined"
                    >
                      {{ getStatusText(chapitre.pourcentage || 0) }}
                    </v-chip>
                  </div>
                </div>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </v-container>

    <Footer />
  </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
import SuperChapitre from "@/Services/chapitreService.js";

export default {
  name: "ChapitreView",
  components: {
    Navbar,
    Footer,
    UpdateAccount
  },
  data() {
    return {
      showUpdateAccount: false,
      chapitres: [],
    };
  },
  computed: {
    thematicId() {
      return this.$route.params.thematicId;
    },
    thematicTitle() {
      return this.$route.params.thematicTitle;
    },
  },
  methods: {
    // Fonctions pour déterminer les couleurs et le texte en fonction du pourcentage
    getProgressColor(progress) {
      if (progress < 30) return 'red-darken-1';
      if (progress < 60) return 'amber-darken-2';
      if (progress < 90) return 'light-blue-darken-1';
      return 'green-darken-1';
    },
    getStatusColor(progress) {
      if (progress === 0) return 'grey';
      if (progress < 30) return 'red';
      if (progress < 60) return 'amber';
      if (progress < 90) return 'blue';
      if (progress < 100) return 'teal';
      return 'green';
    },
    getStatusText(progress) {
      if (progress === 0) return 'لم يبدأ';
      if (progress < 30) return 'بداية';
      if (progress < 60) return 'في تقدم';
      if (progress < 90) return 'متقدم';
      if (progress < 100) return 'قريب من الإكمال';
      return 'مكتمل';
    },
    
    // Charger tous les chapitres
    async loadAllSuperChapitre(thematicId) {
      try {
        const response = await SuperChapitre.getAllSuperChapitre(thematicId);
        this.chapitres = response.data;
        
        // Après avoir chargé les chapitres, on charge leurs pourcentages de progression
        await this.loadProgressInfo();
      } catch (error) {
        console.error("Erreur lors de la récupération des chapitres :", error);
      }
    },
    
    // Nouvelle méthode pour charger la progression de chaque chapitre
    async loadProgressInfo() {
      for (const chapitre of this.chapitres) {
        try {
          // Supposons que nous avons une méthode pour obtenir le pourcentage de progression d'un chapitre
          const progressData = await SuperChapitre.getChapitreProgress(chapitre.id);
          console.log(`Progression pour le chapitre ${chapitre.id}:`, progressData);
          if (progressData) {
            chapitre.pourcentage = progressData;
          } else {
            chapitre.pourcentage = 0;
          }
        } catch (err) {
          console.error(`Erreur lors du chargement de la progression pour le chapitre ${chapitre.id}:`, err);
          chapitre.pourcentage = 0;
        }
      }
    }
  },
  watch: {
    thematicId(newId) {
      this.loadAllSuperChapitre(newId);
    }
  },
  mounted() {
    this.loadAllSuperChapitre(this.thematicId);
  }
};
</script>


<style scoped>
.Chapitre-container {
  background-color: #e3f2fd;
  min-height: 100vh;
  direction: rtl;
  padding-bottom: 30px;
}

.section-title {
  text-align: center;
  color: #0d47a1;
  margin-top: 30px;
  font-size: 2rem;
  font-weight: bold;
}

.cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 25px;
  padding: 40px 20px;
}

.card {
  width: 300px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: block;
  width: 100%;
  height: 100%;
}

.card-img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.card-body {
  padding: 20px;
}

.card-title {
  font-size: 1.3rem;
  color: #1565c0;
  margin-bottom: 10px;
}

.card-desc {
  font-size: 1rem;
  color: #424242;
  line-height: 1.5;
  margin-bottom: 15px;
}

/* Styles pour la barre de progression */
.progress-container {
  margin-top: 15px;
}

.progress-stats {
  width: 100%;
}

.stats-label {
  font-size: 0.9rem;
  color: #546e7a;
  margin-bottom: 5px;
}

.progress-text {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  font-size: 0.85rem;
}

.progress-detailed {
  color: #455a64;
}

.progress-value {
  font-size: 0.8rem;
  color: white;
}

.status-chip {
  font-size: 0.75rem;
}

/* Responsive */
@media (max-width: 768px) {
  .cards-wrapper {
    flex-direction: column;
    align-items: center;
  }
}
</style>