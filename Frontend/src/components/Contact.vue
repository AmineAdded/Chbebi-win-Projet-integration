<template>
    <v-container fluid class="contact-container">
      <v-row justify="center">
        <v-col cols="12" md="12" class="text-center">
          <!-- Titre principal -->
          <h1 class="title">علاش تطبيقنا مهم؟</h1>
          
          <!-- Texte explicatif -->
          <p class="description">
            الشباب في تونس يعاني من الضغط وعدم وضوح الرؤية المستقبلية✅<br>
            نقص في التطبيقات التي تساعدهم فعلاً بطريقة مفيدة وعملية✅<br>
            أول تطبيق تونسي يدمج التنظيم الشخصي + التعليم + اكتشاف الذات✅<br>
          </p>
          <p class="mini-description">
            تطبيق يغيّر حياة الشباب بطريقة إيجابية، ويعاونهم يكونوا أكثر وعياً وثقة في أنفسهم🎯
          </p>
  
          <!-- Image illustrant l'idée -->
          <v-img src="@/assets/logo.png" alt="Idée" class="illustration"></v-img>
  
          <!-- Section des avis -->
          <h2 class="title">رايكم فينا</h2>
          <v-divider class="divider"></v-divider>
  
          <!-- Liste des avis -->
          <v-card v-for="(review, index) in reviews" :key="index" class="review-card">
            <v-list-item>
              <v-list-item-content class="review-content">
                <v-list-item-title class="review-author">{{ review.author }}</v-list-item-title>
                <v-list-item-subtitle class="review-text">{{ review.text }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-card>
  
          <!-- Bouton pour donner un avis -->
          <v-btn class="review-btn" outlined  @click.prevent="$emit('openRate')">أعطينا رايك</v-btn>
          <v-divider class="divider"></v-divider>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  export default {
    name: "Contact",
    data() {
      return {
        reviews: [
          { author: "مريم", text: "🔥 الصراحة؟ التطبيق تاااااايرا 🔥" },
          { author: "مجهول", text: "📢كبير potentiel مشروع عندو"},
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  /* Conteneur principal */
  .contact-container {
    background-color: #91B5C9;
    padding: 0px 0px 60px 0px;
    text-align: center;
  }
  
  /* Titres */
  .title {
    font-size: 2.7rem;
    font-weight: bold;
    color: #152538;
    text-align: center;
    font-family: 'Segoe UI';
    margin-bottom: 1%;
  }
  
  /* Description */
  .description {
    line-height: 1.8;
    text-align: center;
    font-size: 26px;
    font-weight: 600;
    font-family: "Segoe UI", sans-serif;
    color: #152538;
  }

  .mini-description {
    line-height: 1.8;
    text-align: center;
    font-size: 26px;
    font-weight: 600;
    font-family: "Segoe UI", sans-serif;
    color: #152538;
    margin-bottom: 5%;
    margin-top: 5%;
  }
  
  /* Illustration */
  .illustration {
    width: 280px;
    height: 220px;
    margin: 20px auto 20px auto;
  }
  
  /* Divider */
  .divider {
    width: 60%;
    margin: 10px auto;
    border: #000000 solid 3px;
  }
  
  /* Avis des utilisateurs */
  .review-card {
    background-color: white;
    margin: 10px auto;
    padding: 10px;
    border-radius: 12px;
    max-width: 500px;
    box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  /* Contenu des avis aligné à droite */
  .review-content {
    text-align: right;
  }
  
  /* Auteur de l'avis */
  .review-author {
    font-weight: bold;
    font-size: 1.2rem;
    color: #152538;
    font-family: 'Segoe UI';
  }
  
  /* Texte de l'avis */
  .review-text {
    font-size: 1rem;
    color: #152538;
    font-family: 'Segoe UI';
    padding: 1% 1% 5% 1%;
  }
  
  /* Bouton pour donner un avis */
  .review-btn {
    font-size: 1.7rem;
    font-family: 'Segoe UI';
    font-weight: bold;
    border: 2px solid #152538;
    color: #152538;
    border-radius: 20px;
    height: 5%;
  }
  </style>
  