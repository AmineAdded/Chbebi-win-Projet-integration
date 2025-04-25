<template>
  <div class="chapters-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />

    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid>
      <h2 class="section-title">الفصول الخاصة بموضوع: {{chapitreTitle}}</h2>
      <div class="cards-wrapper">
        <div
          v-for="(sousChapitre, index) in sousChapitres"
          :key="index"
          class="card"
          @click="openModal(sousChapitre)"
        >
          <img :src="require('@/assets/' + sousChapitre.image)" alt="chapitre" class="card-img" />
          <div class="card-body">
            <h3 class="card-title">{{ sousChapitre.title }}</h3>
          </div>
        </div>
      </div>
    </v-container>

    <v-dialog v-model="showModal" max-width="800">
      <div class="popup-content">
        <button class="close-btn" @click="showModal = false">X</button>
        <iframe
          class="video-frame"
          :src="selectedChapter.lienVideo"
          frameborder="0"
          allowfullscreen
        ></iframe>
        <p>{{ selectedChapter.description }}</p>
        <a :href="`/PDFs/${selectedChapter.pdf}`" target="_blank" download>تحميل الملف PDF</a>
      </div>
    </v-dialog>

    <Footer />
  </div>
</template>
  
  <script>
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
import SousChapitre from "@/Services/sousChapitreService.js";

export default {
  name: "SousChapitreView",
  components: { Navbar, Footer, UpdateAccount },
  data() {
    return {
      // chapitreId: this.$route.params.chapitreId,
      // chapitreTitle: this.$route.params.chapitreTitle,
      showModal: false,
      showUpdateAccount: false,
      selectedChapter: {},
      sousChapitres: [],
    };
  },
  computed: {
    chapitreTitle() {
      return this.$route.params.chapitreTitle;
    },
    chapitreId() {
      return this.$route.params.chapitreId;
    },
  },
  mounted() {
    this.loadAllSousChapitre(this.chapitreId);
  },
  methods: {
    openModal(chapter) {
      this.selectedChapter = chapter;
      this.showModal = true;
    },
    loadAllSousChapitre(chapitreId) {
      SousChapitre.getAllSousChapitre(chapitreId)
        .then((response) => {
          this.sousChapitres = response.data;
        })
        .catch((error) => {
          console.error(
            "Erreur lors de la récupération des sous chapitres :",
            error
          );
        });
    },
  },
};
</script>
  
  <style scoped>
.chapters-container {
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
  width: 280px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
  cursor: pointer;
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

/* Modal Styling */
.popup-content {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  position: relative;
  max-height: 90vh;
  overflow-y: auto;
}

.close-btn {
  position: absolute;
  top: 10px;
  left: 10px;
  background-color: #c62828;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 5px 12px;
  cursor: pointer;
}

.video-frame {
  width: 100%;
  height: 400px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.description {
  font-size: 1rem;
  color: #333;
  line-height: 1.6;
  margin-bottom: 20px;
}

.download-btn {
  display: inline-block;
  padding: 10px 20px;
  background-color: #0d47a1;
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.download-btn:hover {
  background-color: #1565c0;
}

/* Responsive */
@media (max-width: 768px) {
  .video-frame {
    height: 250px;
  }
}
</style>
  