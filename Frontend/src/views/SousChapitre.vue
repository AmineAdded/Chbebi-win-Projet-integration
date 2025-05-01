<template>
  <div class="chapters-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />

    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid>
      <h2 class="section-title">الفصول الخاصة بموضوع: {{ chapitreTitle }}</h2>
      
      <!-- Slider avec navigation -->
      <div class="slider-container">
        <button 
          v-if="sousChapitres.length > 4" 
          class="nav-btn prev-btn" 
          @click="slidePrev" 
          :disabled="currentSlide <= 0"
        >
          <v-icon>mdi-chevron-right</v-icon>
        </button>
        
        <div class="slider-wrapper" ref="sliderWrapper">
          <div 
            class="slider-track" 
            :style="{ transform: `translateX(${-currentSlide * slideWidth}px)` }"
          >
            <div v-for="(sousChapitre, index) in sousChapitres" :key="index" class="card"
              @click="openModal(sousChapitre)">
              <img :src="require('@/assets/' + sousChapitre.image)" alt="chapitre" class="card-img" />
              <div class="card-body">
                <h3 class="card-title">{{ sousChapitre.title }}</h3>
                <p class="card-desc">{{ sousChapitre.description }}</p>
                <div class="progress-container">
                  <div class="progress-stats">
                    <div class="stats-label">إحصائيات التقدم</div>
                    <v-progress-linear
                      v-if="sousChapitre.lastPageRead && sousChapitre.totalPages"
                      :color="getProgressColor(sousChapitre.pourcentage || 0)"
                      :model-value="sousChapitre.pourcentage || 0"
                      height="14"
                      rounded
                      striped
                    >
                      <template v-slot:default="{ value }">
                        <strong class="progress-value">{{ value }}%</strong>
                      </template>
                    </v-progress-linear>
                    <div class="progress-text">
                      <div class="progress-detailed">
                        {{ sousChapitre.pourcentage || 0 }}% مكتمل
                        <v-icon small color="primary" class="info-icon">mdi-information</v-icon>
                      </div>
                      <v-chip
                        :color="getStatusColor(sousChapitre.pourcentage || 0)"
                        size="small"
                        class="status-chip"
                        variant="outlined"
                      >
                        {{ getStatusText(sousChapitre.pourcentage || 0) }}
                      </v-chip>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <button 
          v-if="sousChapitres.length > 4" 
          class="nav-btn next-btn" 
          @click="slideNext" 
          :disabled="currentSlide >= maxSlide"
        >
          <v-icon>mdi-chevron-left</v-icon>
        </button>
      </div>
      
      <!-- Indicateurs de pagination -->
      <div v-if="sousChapitres.length > 4" class="slider-dots">
        <span 
          v-for="n in slidesCount" 
          :key="n" 
          class="dot" 
          :class="{ active: Math.floor(currentSlide / 4) === n - 1 }"
          @click="goToSlide((n-1) * 4)"
        ></span>
      </div>
    </v-container>

    <!-- Reste du code inchangé -->
    <div style="display: none; width: 0; height: 0; overflow: hidden;">
      <div v-for="(sousChapitre, index) in sousChapitres" :key="'loader-'+index">
        <PdfEmbed 
          v-if="sousChapitre.pdf" 
          :source="`/PDFs/${sousChapitre.pdf}`" 
          @loaded="pdf => onPdfPreloaded(pdf, sousChapitre.id)" 
        />
      </div>
    </div>

    <v-dialog v-model="showModal" max-width="800">
      <div class="popup-content">
        <button class="close-btn" @click="showModal = false">X</button>
        <iframe class="video-frame" :src="getEmbedUrl(selectedChapter.lienVideo)" frameborder="0"
          allowfullscreen></iframe>
        <p>{{ selectedChapter.description }}</p>
        <a :href="`/PDFs/${selectedChapter.pdf}`" target="_blank" download class="styled-download-link">تحميل الملف PDF</a>
        <button class="download-btn" @click="showPdfViewer = true">📄 عرض الملف PDF</button>
      </div>
    </v-dialog>

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

    <Footer />
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import Footer from "@/components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
import SousChapitre from "@/Services/sousChapitreService.js";
import PdfEmbed from "vue-pdf-embed";
import { useUserStore } from "@/store/User/userStore";

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
      pdfCache: {}, // Pour stocker les informations de pages des PDFs déjà chargés
      currentSlide: 0,
      slideWidth: 305, // Largeur d'une carte + marge
      maxSlide: 0,
      slidesCount: 1,
      resizeTimer: null
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
    this.loadPdfCacheFromStorage();
    
    // Ajouter un écouteur de redimensionnement pour le responsive
    window.addEventListener('resize', this.handleResize);
    
    // Configuration initiale après chargement des données
    this.$nextTick(() => {
      this.calculateSliderDimensions();
    });
  },
  unmounted() {
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    // Méthodes pour le slider
    handleResize() {
      clearTimeout(this.resizeTimer);
      this.resizeTimer = setTimeout(() => {
        this.calculateSliderDimensions();
      }, 200);
    },
    
    calculateSliderDimensions() {
      if (this.$refs.sliderWrapper) {
        const wrapperWidth = this.$refs.sliderWrapper.offsetWidth;
        const visibleCards = Math.floor(wrapperWidth / this.slideWidth);
        this.maxSlide = Math.max(0, this.sousChapitres.length - visibleCards);
        this.slidesCount = Math.ceil(this.sousChapitres.length / 4);
        
        // Ajuster la position actuelle si nécessaire
        if (this.currentSlide > this.maxSlide) {
          this.currentSlide = this.maxSlide;
        }
      }
    },
    
    slidePrev() {
      if (this.currentSlide > 0) {
        this.currentSlide -= 1;
      }
    },
    
    slideNext() {
      if (this.currentSlide < this.maxSlide) {
        this.currentSlide += 1;
      }
    },
    
    goToSlide(index) {
      this.currentSlide = Math.min(index, this.maxSlide);
    },
    
    // Méthodes existantes
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
    
    onPdfPreloaded(pdf, sousChapitreId) {
      if (pdf && pdf.numPages) {
        this.pdfCache[sousChapitreId] = pdf.numPages;
        this.savePdfCacheToStorage();
        
        const index = this.sousChapitres.findIndex(sc => sc.id === sousChapitreId);
        if (index !== -1) {
          this.sousChapitres[index].totalPages = pdf.numPages;
        }
      }
    },
    
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
        const index = this.sousChapitres.findIndex(sc => sc.id === this.selectedChapter.id);
        const store = useUserStore();
        const idUser = store.user.id;
        const pourcentage = Math.min(Math.round((this.currentPage / this.totalPages) * 100), 100);
        const res = await SousChapitre.setLastReadPage({userId:idUser,SousChapitreId:index,LastPageRead:this.currentPage, pourcentage:pourcentage});
        
        
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

      if (this.pdfCache[chapter.id]) {
        this.totalPages = this.pdfCache[chapter.id];
      }

      try {
        const index = this.sousChapitres.findIndex(sc => sc.id === this.selectedChapter.id);
        const store = useUserStore();
        const idUser = store.user.id;

        const lastPageData = await SousChapitre.getLastReadPage({userId:idUser,SousChapitreId:index});
        
        if (lastPageData) {
          this.currentPage = lastPageData.lastPageRead || 1;
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
        
        this.pdfCache[this.selectedChapter.id] = pdf.numPages;
        this.savePdfCacheToStorage();
        
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
    },
    sousChapitres: {
      handler() {
        this.$nextTick(() => {
          this.calculateSliderDimensions();
        });
      },
      deep: true
    }
  }
};
</script>

<style scoped>
.chapters-container {
  background-color: #f0f8ff;
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
  position: relative;
  padding-bottom: 10px;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #0d47a1, #42a5f5);
  border-radius: 2px;
}

/* Styles pour le slider */
.slider-container {
  position: relative;
  width: 100%;
  margin: 30px auto;
  padding: 0 40px;
}

.slider-wrapper {
  width: 100%;
  overflow: hidden;
  position: relative;
}

.slider-track {
  display: flex;
  transition: transform 0.5s ease;
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(145deg, #1976d2, #1565c0);
  border: none;
  color: white;
  cursor: pointer;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.nav-btn:hover {
  background: linear-gradient(145deg, #1565c0, #0d47a1);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.nav-btn:disabled {
  background: #bdbdbd;
  box-shadow: none;
  cursor: not-allowed;
}

.prev-btn {
  right: -5px;
}

.next-btn {
  left: -5px;
}

.slider-dots {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin: 20px 0;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #bbdefb;
  cursor: pointer;
  transition: transform 0.3s ease, background-color 0.3s ease;
}

.dot.active {
  background-color: #1976d2;
  transform: scale(1.3);
}

/* Styles pour les cartes */
.card {
  flex: 0 0 280px;
  margin: 0 12px;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.4s ease;
  cursor: pointer;
  position: relative;
  border: 1px solid rgba(230, 230, 230, 0.8);
}

.card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 12px 45px rgba(0, 0, 0, 0.12);
}

.card-img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.card:hover .card-img {
  transform: scale(1.08);
}

.card-body {
  padding: 20px;
}

.card-title {
  font-size: 1.3rem;
  color: #1565c0;
  margin-bottom: 10px;
  font-weight: 600;
}

.card-desc {
  font-size: 0.95rem;
  color: #424242;
  line-height: 1.5;
  margin-bottom: 15px;
}

.progress-container {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f9ff;
  border-radius: 10px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
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

/* Modal Styling */
.popup-content {
  background: #fff;
  padding: 25px;
  border-radius: 16px;
  position: relative;
  max-height: 90vh;
  overflow-y: auto;
}

.close-btn {
  position: absolute;
  top: 15px;
  left: 15px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.close-btn:hover {
  background-color: #d32f2f;
  transform: rotate(90deg);
}

.video-frame {
  width: 100%;
  height: 400px;
  border-radius: 12px;
  margin-bottom: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}

.download-btn {
  margin-top: 10px;
  background: linear-gradient(145deg, #1976d2, #1565c0);
  border: none;
  color: white;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(21, 101, 192, 0.3);
}

.download-btn:hover {
  background: linear-gradient(145deg, #1565c0, #0d47a1);
  box-shadow: 0 6px 15px rgba(13, 71, 161, 0.4);
  transform: translateY(-2px);
}

.styled-download-link {
  display: inline-block;
  margin-top: 15px;
  margin-right: 15px;
  padding: 10px 20px;
  background: linear-gradient(145deg, #0d47a1, #1565c0);
  color: white;
  font-weight: bold;
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-size: 1rem;
  text-align: center;
  box-shadow: 0 4px 10px rgba(13, 71, 161, 0.3);
}

.styled-download-link:hover {
  background: linear-gradient(145deg, #1565c0, #42a5f5);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(13, 71, 161, 0.4);
}

.pdf-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin: 15px 0;
  padding: 10px;
  background-color: #f5f9ff;
  border-radius: 10px;
}

.pdf-controls button {
  padding: 8px 16px;
  background: linear-gradient(145deg, #1976d2, #1565c0);
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(21, 101, 192, 0.2);
}

.pdf-controls button:hover:not(:disabled) {
  background: linear-gradient(145deg, #1565c0, #0d47a1);
  transform: translateY(-2px);
}

.pdf-controls button:disabled {
  background: #bdbdbd;
  box-shadow: none;
  cursor: not-allowed;
}

/* Media Queries pour le responsive */
@media (max-width: 768px) {
  .slider-container {
    padding: 0 30px;
  }
  
  .nav-btn {
    width: 35px;
    height: 35px;
  }
  
  .card {
    flex: 0 0 260px;
  }
}

@media (max-width: 480px) {
  .slider-container {
    padding: 0 25px;
  }
  
  .nav-btn {
    width: 30px;
    height: 30px;
  }
  
  .card {
    flex: 0 0 240px;
    margin: 0 8px;
  }
  
  .card-img {
    height: 160px;
  }
}
</style>