<template>
  <div class="chapitre-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />
    
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>

    <v-container fluid class="main-content"> 
      <h2 class="section-title">{{ thematicTitle }}</h2>
      
      <!-- Mode slider pour plus de 4 chapitres -->
      <div v-if="chapitres.length > 4" class="slider-container">
        <v-btn 
          icon="mdi-chevron-right" 
          variant="text" 
          color="primary" 
          class="nav-btn prev-btn" 
          @click="slideLeft"
          :disabled="currentSlide === 0"
        />
        
        <div class="slider-wrapper" ref="sliderWrapper">
          <div 
            class="slider-track" 
            :style="{ transform: `translateX(${-currentSlide * slideOffset}px)` }"
          >
            <div v-for="(chapitre, index) in chapitres" :key="index" class="card-slide">
              <div class="card card-elevated">
                <router-link :to="{ name: 'SousChapitre', params: { chapitreId: chapitre.id, chapitreTitle: chapitre.title } }" class="card-link">
                  <div class="card-image-container">
                    <img :src="require('@/assets/' + chapitre.image)" alt="Chapitre image" class="card-img" />
                    <div class="card-overlay">
                      <v-chip
                        :color="getStatusColor(chapitre.pourcentage || 0)"
                        size="small"
                        class="status-chip"
                        variant="elevated"
                      >
                        {{ getStatusText(chapitre.pourcentage || 0) }}
                      </v-chip>
                    </div>
                  </div>
                  <div class="card-body">
                    <h3 class="card-title">{{ chapitre.title }}</h3>
                    <p class="card-desc">{{ chapitre.description }}</p>
                    
                    <!-- Barre de progression avec animation -->
                    <div class="progress-container">
                      <div class="progress-header">
                        <div class="stats-label">إحصائيات التقدم</div>
                        <div class="progress-value">{{ Math.ceil(chapitre.pourcentage || 0) }}%</div>
                      </div>
                      <v-progress-linear
                        :color="getProgressColor(chapitre.pourcentage || 0)"
                        :buffer-value="100"
                        buffer-color="light-blue-lighten-5"
                        :model-value="chapitre.pourcentage || 0"
                        height="10"
                        rounded
                        striped
                      />
                      <div class="progress-text">
                        <div class="progress-detailed">
                          {{ chapitre.pourcentage || 0 }}% مكتمل
                        </div>
                      </div>
                    </div>
                    
                    <!-- Bouton pour télécharger le certificat avec hover effect -->
                    <v-btn
                      v-if="chapitre.pourcentage === 100"
                      color="success"
                      class="download-btn mt-3"
                      prepend-icon="mdi-certificate"
                      @click.stop="generateCertificate(chapitre)"
                      block
                      variant="elevated"
                    >
                      تحميل الشهادة
                    </v-btn>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </div>
        
        <v-btn 
          icon="mdi-chevron-left" 
          variant="text" 
          color="primary" 
          class="nav-btn next-btn"
          @click="slideRight"
          :disabled="currentSlide >= maxSlide"
        />
        
        <!-- Indicateurs de pagination -->
        <div class="slider-pagination">
          <div 
            v-for="n in totalSlides" 
            :key="n" 
            class="pagination-dot" 
            :class="{ active: currentSlide === n-1 }"
            @click="currentSlide = n-1" 
          />
        </div>
      </div>
      
      <!-- Mode normal pour moins de 5 chapitres -->
      <div v-else class="cards-wrapper">
        <div v-for="(chapitre, index) in chapitres" :key="index" class="card card-elevated">
          <router-link :to="{ name: 'SousChapitre', params: { chapitreId: chapitre.id, chapitreTitle: chapitre.title } }" class="card-link">
            <div class="card-image-container">
              <img :src="require('@/assets/' + chapitre.image)" alt="Chapitre image" class="card-img" />
              <div class="card-overlay">
                <v-chip
                  :color="getStatusColor(chapitre.pourcentage || 0)"
                  size="small"
                  class="status-chip"
                  variant="elevated"
                >
                  {{ getStatusText(chapitre.pourcentage || 0) }}
                </v-chip>
              </div>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ chapitre.title }}</h3>
              <p class="card-desc">{{ chapitre.description }}</p>
              
              <!-- Barre de progression avec animation -->
              <div class="progress-container">
                <div class="progress-header">
                  <div class="stats-label">إحصائيات التقدم</div>
                  <div class="progress-value">{{ Math.ceil(chapitre.pourcentage || 0) }}%</div>
                </div>
                <v-progress-linear
                  :color="getProgressColor(chapitre.pourcentage || 0)"
                  :buffer-value="100"
                  buffer-color="light-blue-lighten-5"
                  :model-value="chapitre.pourcentage || 0"
                  height="10"
                  rounded
                  striped
                />
                <div class="progress-text">
                  <div class="progress-detailed">
                    {{ chapitre.pourcentage || 0 }}% مكتمل
                  </div>
                </div>
              </div>
              
              <!-- Bouton pour télécharger le certificat -->
              <v-btn
                v-if="chapitre.pourcentage === 100"
                color="success"
                class="download-btn mt-3"
                prepend-icon="mdi-certificate"
                @click.stop="generateCertificate(chapitre)"
                block
                variant="elevated"
              >
                تحميل الشهادة
              </v-btn>
            </div>
          </router-link>
        </div>
      </div>
    </v-container>

  </div>
  <Footer />
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
import SuperChapitre from "@/Services/chapitreService.js";
import { PDFDocument, rgb } from 'pdf-lib';
import { saveAs } from 'file-saver';
import fontkit from '@pdf-lib/fontkit';
import { useUserStore } from "@/store/User/userStore";

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
      user: {
        name: '', 
        email: ''
      },
      currentSlide: 0,
      slideOffset: 350, // Augmenté pour inclure la marge de 30px
      maxSlide: 0,
      totalSlides: 1,
      autoSlideInterval: null,
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
    // Gestion du slider
    calculateSliderParams() {
      if (!this.$refs.sliderWrapper) return;
      
      const containerWidth = this.$refs.sliderWrapper.clientWidth;
      const cardsPerView = Math.floor(containerWidth / this.slideOffset);
      this.maxSlide = Math.max(0, Math.ceil(this.chapitres.length / cardsPerView) - 1);
      this.totalSlides = this.maxSlide + 1;
      
      // Limiter currentSlide si nécessaire
      if (this.currentSlide > this.maxSlide) {
        this.currentSlide = this.maxSlide;
      }
    },
    
    slideLeft() {
      if (this.currentSlide > 0) {
        this.currentSlide--;
      }
    },
    
    slideRight() {
      if (this.currentSlide < this.maxSlide) {
        this.currentSlide++;
      }
    },
    
    // startAutoSlide() {
    //   // Slide automatique toutes les 5 secondes si plus de 4 chapitres
    //   if (this.chapitres.length > 4) {
    //     this.autoSlideInterval = setInterval(() => {
    //       if (this.currentSlide < this.maxSlide) {
    //         this.currentSlide++;
    //       } else {
    //         this.currentSlide = 0;
    //       }
    //     }, 5000);
    //   }
    // },
    
    // stopAutoSlide() {
    //   if (this.autoSlideInterval) {
    //     clearInterval(this.autoSlideInterval);
    //   }
    // },
    
    handleResize() {
      this.calculateSliderParams();
    },
    
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
        const response = await SuperChapitre.getChaptersByThematic(thematicId);
        // تأكد أن البيانات مصفوفة
        this.chapitres = Array.isArray(response) ? response : [];
        await this.loadProgressInfo();
        
        // Calculer les paramètres du slider après chargement
        this.$nextTick(() => {
          this.calculateSliderParams();
          // this.startAutoSlide();
        });
      } catch (error) {
        console.error("Erreur lors de la récupération des chapitres :", error);
        this.chapitres = []; // تعيين مصفوفة فارغة كإحتياطي
      }
    },
    
    // Méthode pour charger la progression de chaque chapitre
    async loadProgressInfo() {
      for (const chapitre of this.chapitres) {
        try {
          const store = useUserStore();
          const id = store.user.id;
          
          // Obtenir le pourcentage de progression d'un chapitre
          const progressData = [id, chapitre.id];  // [userId, chapitreId]

          const result = await SuperChapitre.getProgress(progressData);
          console.log(`Progression pour le chapitre ${chapitre.id}:`, result);
          if (result) {
            chapitre.pourcentage = result;
          } else {
            chapitre.pourcentage = 0;
          }
        } catch (err) {
          console.error(`Erreur lors du chargement de la progression pour le chapitre ${chapitre.id}:`, err);
          chapitre.pourcentage = 0;
        }
      }
    },
    
    async generateCertificate(chapitre) {
      try {
        console.log("🚀 بدء إنشاء الشهادة");

        // 1. تحميل ملف الشهادة الأساسي
        const existingPdfBytes = await fetch('/PDFs/Certificate.pdf').then(res => {
          if (!res.ok) throw new Error("فشل في تحميل شهادة القالب");
          return res.arrayBuffer();
        });

        // 2. تحميل مستند PDF
        const pdfDoc = await PDFDocument.load(existingPdfBytes);
        pdfDoc.registerFontkit(fontkit);
        const page = pdfDoc.getPages()[0];

        // 3. تحميل الخط العربي
        const arabicFontBytes = await fetch('/fonts/Amiri-Bold.ttf').then(res => {
          if (!res.ok) throw new Error("فشل في تحميل الخط العربي");
          return res.arrayBuffer();
        });
        const arabicFont = await pdfDoc.embedFont(arabicFontBytes);

        // 4. إعداد النصوص
        const texts = {
          name: this.user.name || 'اسم المستخدم',
          chapter: chapitre.title || 'عنوان الفصل',
          date: new Date().toLocaleDateString('ar-SA', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
          })
        };

        // 6. وظيفة مساعدة للرسم المركزي
        const drawCenteredText = (text, xPage, yPage, size, color = rgb(0, 0, 0)) => {
          page.drawText(text, {
            x: xPage,
            y: yPage,
            size,
            font: arabicFont,
            color,
          });
        };

        // 7. رسم العناصر بمسافات متناسقة
        drawCenteredText(texts.name, 308, 300, 16); // الاسم (الحجم الأكبر)
        drawCenteredText(texts.chapter, 305, 227, 14, rgb(0.1, 0.4, 0.6)); // الفصل
        drawCenteredText(texts.date, 170, 208, 14, rgb(0.3, 0.3, 0.3)); // التاريخ

        // 7. حفظ الملف النهائي
        const pdfBytes = await pdfDoc.save();
        const blob = new Blob([pdfBytes], { type: 'application/pdf' });
        saveAs(blob, `شهادة_${chapitre.title.replace(/\s+/g, '_')}.pdf`);

        console.log("✅ تم إنشاء الشهادة بنجاح");
      } catch (error) {
        console.error('❌ حصل خطأ أثناء إنشاء الشهادة:', error);
        alert(`فشل إنشاء الشهادة: ${error.message}`);
      }
    },

    async loadUserInfo() {
      try {
        // Récupération des infos utilisateur
        const userInfo = JSON.parse(localStorage.getItem('user')) || {};
        this.user.name = userInfo.nom || 'Utilisateur';
        this.user.email = userInfo.email || 'example@email.com';
      } catch (error) {
        console.error("Erreur lors du chargement des infos utilisateur:", error);
      }
    }
  },
  watch: {
    thematicId(newId) {
      this.loadAllSuperChapitre(newId);
    },
    chapitres: {
      handler() {
        this.$nextTick(() => {
          this.calculateSliderParams();
        });
      },
      deep: true
    }
  },
  mounted() {
    this.loadAllSuperChapitre(this.thematicId);
    this.loadUserInfo();
    
    // Ajouter l'écouteur de redimensionnement
    window.addEventListener('resize', this.handleResize);
  },
  beforeUnmount() {
    // Supprimer l'écouteur de redimensionnement et arrêter le défilement automatique
    window.removeEventListener('resize', this.handleResize);
    // this.stopAutoSlide();
  }
};
</script>

<style scoped>
.chapitre-container {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 70%, #90caf9 100%);
  min-height: 100vh;
  direction: rtl;
  padding-bottom: 70px;
  position: relative;
}

.chapitre-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 200px;
  background: linear-gradient(180deg, rgba(25, 118, 210, 0.05) 0%, rgba(255, 255, 255, 0) 100%);
  z-index: 1;
}

.main-content {
  max-width: 1280px;
  margin: 0 auto;
}

.section-title {
  text-align: center;
  color: #0d47a1;
  margin: 60px 0 50px;
  font-size: 2.8rem;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 2;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -15px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
  height: 4px;
  background: linear-gradient(90deg, #1976d2, #64b5f6);
  border-radius: 4px;
}

/* Mode normal pour moins de 5 cartes */
.cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 50px; /* Espacement entre les cartes */
  padding: 40px 0;
}

/* Styles communs de carte */
.card {
  width: 320px;
  background-color: white;
  border-radius: 20px;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
  border: 1px solid rgba(230, 240, 255, 0.8);
}

.card-elevated {
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.08);
}

.card:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 40px rgba(25, 118, 210, 0.25);
  border-color: rgba(200, 230, 255, 1);
}

.card-link {
  text-decoration: none;
  color: inherit;
  display: block;
  width: 100%;
  height: 100%;
}

.card-image-container {
  position: relative;
  overflow: hidden;
}

.card-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  transition: transform 0.8s ease;
  filter: brightness(0.95);
}

.card:hover .card-img {
  transform: scale(1.08);
  filter: brightness(1.05);
}

.card-overlay {
  position: absolute;
  top: 15px;
  left: 15px;
  z-index: 2;
}

.card-body {
  padding: 25px 22px;
  border-top: 1px solid rgba(232, 245, 253, 0.8);
}

.card-title {
  font-size: 1.5rem;
  color: #1565c0;
  margin-bottom: 15px;
  font-weight: 700;
  position: relative;
  padding-bottom: 12px;
}

.card-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  right: 0;
  width: 50px;
  height: 3px;
  background: linear-gradient(90deg, #64b5f6, #1976d2);
  border-radius: 2px;
}

.card-desc {
  font-size: 1.05rem;
  color: #455a64;
  line-height: 1.7;
  margin-bottom: 18px;
  height: 85px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

/* Styles pour la barre de progression */
.progress-container {
  margin-top: 20px;
  padding: 15px;
  background-color: rgba(232, 245, 253, 0.7);
  border-radius: 12px;
  box-shadow: inset 0 2px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(207, 232, 252, 0.8);
}

.progress-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 0.9rem;
  color: #455a64;
  font-weight: 500;
}

.progress-value {
  font-weight: 600;
  color: #0277bd;
}

.progress-text {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-top: 8px;
  font-size: 0.85rem;
}

.progress-detailed {
  color: #455a64;
}

.status-chip {
  font-size: 0.75rem;
  font-weight: 600;
}

/* Style du bouton de téléchargement */
.download-btn {
  margin-top: 20px !important;
  transition: all 0.4s ease;
  border-radius: 10px;
  font-weight: 700;
  letter-spacing: 0.8px;
  padding: 12px 24px !important;
  position: relative;
  overflow: hidden;
}

.download-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(255,255,255,0.2), rgba(255,255,255,0));
  transition: all 0.5s ease;
}

.download-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(76, 175, 80, 0.35);
}

.download-btn:hover::before {
  left: 100%;
}

/* Styles pour le slider */
.slider-container {
  position: relative;
  padding: 20px 60px;
  margin: 30px 0;
}

.slider-wrapper {
  overflow: hidden;
  margin: 0 auto;
  width: 100%;
}

.slider-track {
  display: flex;
  transition: transform 0.5s ease-out;
}

.card-slide {
  min-width: 320px;
  padding: 0 15px; /* Espacement horizontal entre les cartes dans le slider */
  box-sizing: border-box;
  margin: 0 15px; /* Ajout de marges latérales pour l'espacement */
}

.nav-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.nav-btn:hover {
  background-color: #1976d2 !important;
  color: white !important;
  box-shadow: 0 6px 14px rgba(25, 118, 210, 0.3);
}

.prev-btn {
  right: 10px;
}

.next-btn {
  left: 10px;
}

/* Pagination dots */
.slider-pagination {
  display: flex;
  justify-content: center;
  margin-top: 25px;
}

.pagination-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #bbdefb;
  margin: 0 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-dot.active {
  width: 25px;
  border-radius: 5px;
  background-color: #1976d2;
}

.pagination-dot:hover {
  background-color: #64b5f6;
}

/* Responsive */
@media (max-width: 1024px) {
  .cards-wrapper {
    gap: 30px; /* Réduire légèrement l'espacement pour les écrans moyens */
  }
  
  .card {
    width: 280px;
  }
  
  .slider-container {
    padding: 20px 50px;
  }
  
  .card-slide {
    min-width: 280px;
    margin: 0 10px; /* Réduire les marges pour les écrans moyens */
  }
}

@media (max-width: 768px) {
  .section-title {
    font-size: 2rem;
    margin: 30px 0;
  }
  
  .slider-container {
    padding: 10px 40px;
  }
  
  .nav-btn {
    width: 40px;
    height: 40px;
  }
  
  .cards-wrapper {
    gap: 20px; /* Réduire davantage l'espacement pour les petits écrans */
  }
  
  .card-slide {
    margin: 0 8px; /* Réduire encore les marges pour les petits écrans */
  }
  
  .card-desc {
    height: 60px;
    -webkit-line-clamp: 2;
  }
}

@media (max-width: 576px) {
  .card {
    width: 260px;
  }
  
  .card-slide {
    min-width: 260px;
    margin: 0 5px; /* Marges minimales pour très petits écrans */
  }
  
  .cards-wrapper {
    flex-direction: column;
    align-items: center;
    gap: 30px; /* Conserver un bon espacement vertical */
  }
}
</style>