<template>
  <div class="chapters-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />

    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid>
      <h2 class="section-title">الفصول الخاصة بموضوع: {{ chapitreTitle }}</h2>
      <div class="cards-wrapper">
        <div v-for="(sousChapitre, index) in sousChapitres" :key="index" class="card" @click="openModal(sousChapitre)">
          <img :src="require('@/assets/' + sousChapitre.image)" alt="chapitre" class="card-img" />
          <div class="card-body">
            <h3 class="card-title">{{ sousChapitre.title }}</h3>
            <p class="card-desc">{{ sousChapitre.description }}</p>
            <div class="progress-container">
              <!-- Barre de progression améliorée -->
              <div class="progress-stats">
                <div class="stats-label">إحصائيات التقدم</div>
                <v-progress-linear v-if="sousChapitre.lastPageRead && sousChapitre.totalPages"
                  :color="getProgressColor(sousChapitre.pourcentage || 0)" :buffer-value="100"
                  buffer-color="light-blue-lighten-4" :model-value="sousChapitre.pourcentage || 0" height="14" rounded
                  striped>
                  <template v-slot:default="{ value }">
                    <strong class="progress-value">{{ value }}%</strong>
                  </template>
                </v-progress-linear>
                <div class="progress-text">
                  <div class="progress-detailed">
                    {{ sousChapitre.pourcentage || 0 }}% مكتمل
                    <v-tooltip location="bottom">
                      <template v-slot:activator="{ props }">
                        <v-icon small color="primary" v-bind="props" class="info-icon">
                          mdi-information
                        </v-icon>
                      </template>
                      <span>صفحة {{ sousChapitre.lastPageRead || 0 }} من {{ sousChapitre.totalPages || '?' }}</span>
                    </v-tooltip>
                  </div>
                  <v-chip :color="getStatusColor(sousChapitre.pourcentage || 0)" size="small" class="status-chip"
                    variant="outlined">
                    {{ getStatusText(sousChapitre.pourcentage || 0) }}
                  </v-chip>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </v-container>

    <!-- Préchargement invisible des PDFs -->
    <div style="display: none; width: 0; height: 0; overflow: hidden;">
      <div v-for="(sousChapitre, index) in sousChapitres" :key="'loader-' + index">
        <PdfEmbed v-if="sousChapitre.pdf" :source="`/PDFs/${sousChapitre.pdf}`"
          @loaded="pdf => onPdfPreloaded(pdf, sousChapitre.id)" />
      </div>
    </div>

    <!-- Modal de visualisation -->
    <v-dialog v-model="showModal" max-width="800">
      <div class="popup-content">
        <button class="close-btn" @click="showModal = false">X</button>
        <iframe class="video-frame" :src="getEmbedUrl(selectedChapter.lienVideo)" frameborder="0"
          allowfullscreen></iframe>
        <p>{{ selectedChapter.description }}</p>
        <a :href="`/PDFs/${selectedChapter.pdf}`" target="_blank" download class="styled-download-link">تحميل الملف
          PDF</a>

        <button class="download-btn" @click="showPdfViewer = true">📄 عرض الملف PDF</button>

        <v-dialog v-model="showPdfViewer" max-width="900">
          <div class="popup-content">
            <div class="pdf-controls">
              <button @click="prevPage" :disabled="currentPage <= 1">⬅ الصفحة السابقة</button>
              <span>صفحة {{ currentPage }} من {{ totalPages }}</span>
              <button @click="nextPage" :disabled="currentPage >= totalPages">➡ الصفحة التالية</button>
            </div>

            <PdfEmbed ref="pdfViewer" :source="`/PDFs/${selectedChapter.pdf}`" @loaded="onPdfLoaded" :page="currentPage"
              style="width: 100%; height: 80vh" />
          </div>
        </v-dialog>
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
import PdfEmbed from "vue-pdf-embed";

export default {
  name: "SousChapitreView",
  components: { Navbar, Footer, UpdateAccount, PdfEmbed },
  data() {
    return {
      currentPage: 1,
      totalPages: 1,
      showModal: false,
      showUpdateAccount: false,
      showPdfViewer: false,
      selectedChapter: {},
      sousChapitres: [],
      pdfCache: {} // Pour stocker les informations de pages des PDFs déjà chargés
    };
  },
  computed: {
    chapitreTitle() {
      return this.$route.params.chapitreTitle;
    },
    chapitreId() {
      return this.$route.params.chapitreId;
    }
  },
  mounted() {
    this.loadAllSousChapitre(this.chapitreId);
    // Tenter de charger les données de cache au démarrage
    this.loadPdfCacheFromStorage();
  },
  methods: {
    calculateProgress(currentPage, totalPages) {
      if (!currentPage || !totalPages || totalPages === 0) {
        return 0;
      }
      return Math.min(Math.round((currentPage / totalPages) * 100), 100);
    },
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
    getEmbedUrl(url) {
      if (!url) return '';
      const videoId = url.includes('youtu.be/')
        ? url.split('youtu.be/')[1].split('?')[0]
        : url.includes('watch?v=')
          ? url.split('watch?v=')[1].split('&')[0]
          : '';
      return `https://www.youtube.com/embed/${videoId}`;
    },

    // Nouvelle méthode pour précharger les PDFs
    onPdfPreloaded(pdf, sousChapitreId) {
      if (pdf && pdf.numPages) {
        // Mettre en cache le nombre de pages
        this.pdfCache[sousChapitreId] = pdf.numPages;
        this.savePdfCacheToStorage();

        // Mise à jour du nombre total de pages pour ce sous-chapitre
        const index = this.sousChapitres.findIndex(sc => sc.id === sousChapitreId);
        if (index !== -1) {
          this.sousChapitres[index].totalPages = pdf.numPages;
        }
      }
    },

    // Méthodes pour stocker le cache des pages PDF
    savePdfCacheToStorage() {
      try {
        localStorage.setItem('pdf_pages_cache', JSON.stringify(this.pdfCache));
      } catch (err) {
        console.error("Erreur lors de la sauvegarde du cache PDF:", err);
      }
    },

    loadPdfCacheFromStorage() {
      try {
        const cachedData = localStorage.getItem('pdf_pages_cache');
        if (cachedData) {
          this.pdfCache = JSON.parse(cachedData);
        }
      } catch (err) {
        console.error("Erreur lors du chargement du cache PDF:", err);
        this.pdfCache = {};
      }
    },

    async saveCurrentPage() {
      try {
        if (!this.currentPage || !this.totalPages || this.totalPages === 0) {
          return 0;
        }
        const pourcentage = Math.min(Math.round((this.currentPage / this.totalPages) * 100), 100);
        const res = await SousChapitre.setLastReadPage(this.selectedChapter.id, this.currentPage, pourcentage);

        // Mettre à jour également la progression dans la liste des sous-chapitres
        const index = this.sousChapitres.findIndex(sc => sc.id === this.selectedChapter.id);
        if (index !== -1) {
          this.sousChapitres[index].lastPageRead = this.currentPage;
          this.sousChapitres[index].pourcentage = pourcentage;
          if (!this.sousChapitres[index].totalPages && this.totalPages) {
            this.sousChapitres[index].totalPages = this.totalPages;
          }
        }
        return res;
      } catch (err) {
        console.error("Erreur lors de la sauvegarde du numéro de page:", err);
      }
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.saveCurrentPage();
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.saveCurrentPage();
      }
    },

    async openModal(chapter) {
      this.selectedChapter = chapter;
      this.showModal = true;

      // Utiliser le nombre de pages en cache si disponible
      if (this.pdfCache[chapter.id]) {
        this.totalPages = this.pdfCache[chapter.id];
      }

      try {
        const lastPageData = await SousChapitre.getLastReadPage(chapter.id);

        if (lastPageData) {
          this.currentPage = lastPageData.lastPageRead || 1;
          // S'assurer que le chapitre sélectionné a aussi la bonne valeur de pourcentage
          const index = this.sousChapitres.findIndex(sc => sc.id === chapter.id);
          if (index !== -1) {
            this.sousChapitres[index].pourcentage = lastPageData.pourcentage || 0;
          }
        } else {
          this.currentPage = 1;
        }
      } catch (err) {
        console.error("Erreur lors de la récupération de la dernière page:", err);
        this.currentPage = 1;
      }
    },

    onPdfLoaded(pdf) {
      if (pdf && pdf.numPages) {
        this.totalPages = pdf.numPages;

        // Mettre à jour le cache
        this.pdfCache[this.selectedChapter.id] = pdf.numPages;
        this.savePdfCacheToStorage();

        // Mettre à jour totalPages dans la liste des sous-chapitres
        const index = this.sousChapitres.findIndex(sc => sc.id === this.selectedChapter.id);
        if (index !== -1) {
          this.sousChapitres[index].totalPages = this.totalPages;
        }
      }
    },

    async loadAllSousChapitre(chapitreId) {
      try {
        const response = await SousChapitre.getSousChaptersByChapterId(chapitreId);
        // تحقق من أن البيانات مصفوفة
        this.sousChapitres = Array.isArray(response) ? response : [];
        console.log("Sous chapitres:", this.sousChapitres);

        // تطبيق بيانات الذاكرة المؤقتة
        this.sousChapitres.forEach(sc => {
          if (this.pdfCache[sc.id]) {
            sc.totalPages = this.pdfCache[sc.id];
          }
        });

        await this.loadProgressInfo();
      } catch (error) {
        console.error("Erreur lors de la récupération des sous chapitres :", error);
        this.sousChapitres = []; // إعادة تعيين مصفوفة فارغة عند الخطأ
      }
    },

    async loadProgressInfo() {
      // Charger les informations de progression pour chaque sous-chapitre
      for (const sousChapitre of this.sousChapitres) {
        try {
          const lastPageData = await SousChapitre.getLastReadPage(sousChapitre.id);
          if (lastPageData) {
            sousChapitre.lastPageRead = lastPageData.lastPageRead || 0;
            sousChapitre.pourcentage = lastPageData.pourcentage || 0;
          }
        } catch (err) {
          console.error(`Erreur lors du chargement de la progression pour ${sousChapitre.id}:`, err);
        }
      }
    }
  },
  watch: {
    showPdfViewer(newValue) {
      if (!newValue && this.selectedChapter.id) {
        this.saveCurrentPage();
      }
    }
  }
};
</script>

<style scoped>
.card-desc {
  font-size: 1rem;
  color: #424242;
  line-height: 1.5;
  margin-bottom: 15px;
}

.progress-container {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 10px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.progress-stats {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.stats-label {
  font-size: 0.85rem;
  font-weight: bold;
  color: #0d47a1;
  margin-bottom: 6px;
}

.progress-text {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px;
  font-size: 0.85rem;
  color: #424242;
}

.progress-detailed {
  display: flex;
  align-items: center;
}

.progress-value {
  color: #fff;
  font-size: 0.75rem;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.5);
}

.info-icon {
  margin-right: 5px;
  cursor: help;
}

.status-chip {
  font-size: 0.75rem;
  font-weight: bold;
}

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

.download-btn {
  margin-top: 10px;
  background-color: #1976d2;
  border: none;
  color: white;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
}

.download-btn:hover {
  background-color: #1565c0;
}

.styled-download-link {
  display: inline-block;
  margin-top: 15px;
  margin-right: 15px;
  padding: 10px 20px;
  background-color: #0d47a1;
  color: white;
  font-weight: bold;
  text-decoration: none;
  border-radius: 8px;
  transition: background-color 0.3s ease, transform 0.2s ease;
  font-size: 1rem;
  text-align: center;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.styled-download-link:hover {
  background-color: #1565c0;
  transform: scale(1.03);
}

.pdf-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin: 10px 0;
}

.pdf-controls button {
  padding: 8px 14px;
  background-color: #1565c0;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.pdf-controls button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}
</style>