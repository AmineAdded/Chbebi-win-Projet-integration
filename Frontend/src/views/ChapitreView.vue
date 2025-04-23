<template>
  <div class="Chapitre-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />
    
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid>
      <h2 class="section-title">{{ thematicTitle }}</h2>
      <div class="cards-wrapper">
        
        <div v-for="(chapitre, index) in chapitres" :key="index" class="card ">
          <router-link :to="{ name: 'SousChapitre', params: { chapitreId: chapitre.id, chapitreTitle: chapitre.title } }" class="card">
          <img :src="require('@/assets/' + chapitre.image)" alt="Chapitre chapitre" class="card-img" />
          <div class="card-body">
            <h3 class="card-title">{{ chapitre.title }}</h3>
            <p class="card-desc">{{ chapitre.description }}</p>
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
    loadAllSuperChapitre(thematicId) {
      SuperChapitre.getAllSuperChapitre(thematicId)
        .then(response => {
          this.chapitres = response.data;
        })
        .catch(error => {
          console.error("Erreur lors de la récupération des chapitres :", error);
        });
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
  text-decoration: none;

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
}
a.no-style {
  text-decoration: none;
  color: inherit;
}

/* Responsive */
@media (max-width: 768px) {
  .cards-wrapper {
    flex-direction: column;
    align-items: center;
  }
}
</style>
