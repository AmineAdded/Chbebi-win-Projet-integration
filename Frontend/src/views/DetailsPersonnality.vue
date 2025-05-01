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
    
    <div v-else class="page-wrapper main-container">
      <Navbar @openUpdateAccount="showUpdateAccount = true"/>
      <v-dialog v-model="showUpdateAccount" persistent width="auto">
        <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
      </v-dialog>
      
      <div class="page-header">
        <v-container>
          <h1 class="text-center page-title">تعرف على شخصيتك أكثر</h1>
        </v-container>
      </div>
      
      <v-container class="personality-container">
        <v-card class="personality-card mx-auto" elevation="4" rounded="lg">
          <v-row no-gutters>
            <v-col cols="12" md="4" class="personality-image-col">
              <div class="personality-image-wrapper">
                <v-img
                  :src="require('@/assets/'+this.Personnalite.image)"
                  alt="Thinker Personality"
                  class="personality-image"
                  cover
                ></v-img>
                <div class="personality-badge">
                  <span class="personality-type-badge">{{this.Personnalite.nomEnglish}}</span>
                </div>
              </div>
            </v-col>
            
            <v-col cols="12" md="8" class="personality-content-col">
              <v-card-item>
                <v-card-title class="personality-type text-primary text-right d-flex align-center">
                  <v-icon :icon=this.Personnalite.icon class="me-2" color="primary" size="large"></v-icon>
                  <span class="personality-name me-2">{{ this.Personnalite.nom}}: </span>
                  <span>النوع الشخصي</span>
                </v-card-title>
                
                <v-card-text class="personality-description text-right mt-4">
                  <p class="description-text">
                    {{ this.Personnalite.contenu }}
                  </p>
                </v-card-text>
              </v-card-item>
              
              <v-card-actions class="justify-end pb-4 px-4">
                <v-btn 
                  color="primary" 
                  variant="elevated" 
                  rounded="pill"
                  prepend-icon="mdi-share-variant"
                  class="share-button"
                >
                  مشاركة النتائج
                </v-btn>
                <v-btn 
                  color="secondary" 
                  variant="tonal" 
                  rounded="pill"
                  prepend-icon="mdi-download"
                  class="ms-2 download-button"
                >
                  تحميل التقرير الكامل
                </v-btn>
              </v-card-actions>
            </v-col>
          </v-row>
        </v-card>
    
        <div class="traits-section-title text-center my-8">
          <h2 class="section-title">الخصائص الرئيسية</h2>
          <div class="section-divider mx-auto"></div>
        </div>
    
        <v-row>
          <v-col cols="12" md="4" v-for="(critere, index) in this.criteres" :key="index">
            <v-card 
              class="trait-card" 
              elevation="3" 
              rounded="lg"
              :class="`trait-card-${index}`"
            >
              <div class="trait-icon-wrapper">
                <v-icon :icon="critere.icon" size="36" color="white"></v-icon>
              </div>
              
              <v-card-title class="trait-title text-center">{{ critere.nom }}</v-card-title>
              
              <v-card-text class="trait-description text-right">
                {{ critere.contenu }}
              </v-card-text>
              
              <v-card-actions class="justify-center pb-4">
                <v-btn 
                  rounded="pill"
                  variant="tonal" 
                  color="primary" 
                  :prepend-icon="critere.icon"
                  class="details-button"
                >
                  التفاصيل
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </div>
    <Footer/>

    <v-snackbar v-model="showSnackBar" :color="snackbarColor" location="top" timeout="3000" rounded="xl">
    <div class="d-flex align-center">
      <v-icon class="mr-2" color="white">
        {{ snackbarIcon }}
      </v-icon>
      {{ text }}
    </div>
    <template v-slot:actions>
      <v-btn icon @click="showSnackBar = false">
        <v-icon color="white">mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
  </template>
    
  <script>
  import { defineComponent } from "vue";
  import PersonnaliteService from "@/Services/PersonnaliteService";
  import { useUserStore } from '../store/User/userStore.js';
  import Navbar from "../components/Navbar.vue";
  import Footer from "../components/Footer.vue";
  import UpdateAccount from "@/components/UpdateAccount.vue";
    
  export default defineComponent({
    name: "DetailsPersonnality",
    components: {
      Navbar,
      Footer,
      UpdateAccount
    },
    data() {
      return {
        Personnalite :{},
        isLoading: true,
        showUpdateAccount: false,
        showSnackBar: false,
        snackbarColor: 'error',
        snackbarIcon: 'mdi-alert-circle',
        text: "",
        criters:[],
        
      };
    },
    created() {
      setTimeout(() => {
        this.isLoading = false;
      }, 1500);
    },
    mounted() {
      this.getAllCriteres();
    },
    methods:{
      async getAllCriteres(){
        try{
          const store = useUserStore();
          const personnalite_id = store.user.personnalite_id;
          this.Personnalite = await PersonnaliteService.getAllCriteres(personnalite_id);
          this.criteres = this.Personnalite.criteres;
          console.log(this.Personnalite.image);
        }
        catch(err){
          console.log(err);
          this.showSnackBar=true;
          this.text=err.reponse.data || "حدث خطأ أثناء تحميل البيانات";
        }
      }
    }
  });
  </script>
    
  <style scoped>
  .page-wrapper {
    background-color: #f8fafc;
    min-height: 100vh;
    width: 100%;
    position: relative;
  }
  
  .page-header {
    background-color: #b2cfe0;
    padding: 40px 0;
    color: white;
    margin-bottom: 40px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
  
  .page-title {
    font-size: 2.5rem;
    font-weight: 700;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  }
  
  /* Loader styles */
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
    0% { transform: scale(0.8); opacity: 0.3; }
    50% { transform: scale(1.2); opacity: 0.6; }
    100% { transform: scale(0.8); opacity: 0.3; }
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
  
  /* Personality card styles */
  .personality-container {
    padding: 24px;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
  }
  
  .personality-card {
    overflow: hidden;
    background-color: white;
    border-radius: 16px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }
  
  .personality-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
  }
  
  .personality-image-col {
    position: relative;
    overflow: hidden;
  }
  
  .personality-image-wrapper {
    position: relative;
    height: 100%;
    min-height: 300px;
  }
  
  .personality-image {
    height: 100%;
    border-top-right-radius: 16px;
    border-bottom-right-radius: 16px;
  }
  
  .personality-badge {
    position: absolute;
    top: 20px;
    left: 20px;
    background-color: rgba(25, 118, 210, 0.9); 
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: 600;
    z-index: 2;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .personality-content-col {
    display: flex;
    flex-direction: column;
    padding: 32px 20px;
  }
  
  .personality-type {
    font-size: 2rem;
    font-weight: 700;
    margin-bottom: 16px;
    color: #1a3a5f;
  }
  
  .personality-name {
    color: #2962ff;
    font-weight: 700;
    font-size: 2.2rem;
    margin-right: 8px;
  }
  
  .description-text {
    font-size: 1.2rem;
    line-height: 1.8;
    color: #333;
    margin-bottom: 24px;
  }
  
  /* Traits section styles */
  .traits-section-title {
    position: relative;
  }
  
  .section-title {
    font-size: 2rem;
    font-weight: 700;
    color: #1a3a5f;
    margin-bottom: 16px;
  }
  
  .section-divider {
    height: 4px;
    width: 80px;
     background: linear-gradient(to right, #2962ff, #1565c0); 
    border-radius: 2px;
    margin-bottom: 24px;
  }
  
  .trait-card {
    height: 100%;
    overflow: hidden;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    position: relative;
    border-radius: 16px;
    display: flex;
    flex-direction: column;
  }
  
  .trait-card:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.15);
  }
  
  .trait-icon-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 72px;
    height: 72px;
    margin-left: 150px;
    margin-top: 30px;
    border-radius: 50%;
     background: linear-gradient(135deg, #2962ff, #1565c0);
    box-shadow: 0 4px 10px rgba(41, 98, 255, 0.3);
    z-index: 2;
  }
  
  .trait-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #1a3a5f;
    margin-top: 16px;
    position: relative;
  }
  
  .trait-description {
    font-size: 1.1rem;
    line-height: 1.7;
    color: #333;
    flex-grow: 1;
  }
  
  .trait-card-0 {
    border-top: 4px solid #2962ff;
  }
  
  .trait-card-1 {
    border-top: 4px solid #00b0ff;
  }
  
  .trait-card-2 {
    border-top: 4px solid #00b8d4;
  }
  
  .details-button {
    margin-top: 16px;
    transition: transform 0.2s;
  }
  
  .details-button:hover {
    transform: scale(1.05);
  }
  
  @media (max-width: 960px) {
    .personality-image-wrapper {
      min-height: 240px;
    }
    
    .personality-badge {
      top: 10px;
      left: 10px;
    }
    
    .page-title {
      font-size: 2rem;
    }
  }
  
  @media (max-width: 768px) {
    .personality-content-col {
      padding: 16px;
    }
    
    .personality-type {
      font-size: 1.6rem;
    }
    
    .personality-name {
      font-size: 1.8rem;
    }
    
    .description-text {
      font-size: 1.1rem;
    }
    
    .personality-image-wrapper {
      border-radius: 16px 16px 0 0;
    }
  }
  </style>