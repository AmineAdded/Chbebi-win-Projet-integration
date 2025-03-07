<template>
    <v-container fluid class="about-container" style="position: relative; bottom: 780px;">
      <v-row justify="center" align="center">
        <!-- Colonne pour le texte -->
        <v-col cols="12" md="10" class="text-center">
          <div class="title-container">
            <h1 class="display-2 font-weight-bold">CHBEBIWIN<br />شبابي وين</h1>
            <img :src="logo" alt="logo" class="logo-img" />
          </div>
          <p class="subtitle-1">
            تحس روحك ضايع؟ عندك برشا أفكار وأسئلة وما لقيتش شكون يجاوبك؟<br />
            تحب تنظم وقتك، تتعلم حاجات جديدة، وتلقى طريقك في الحياة؟ 🤔<br />
            شبابي وين هو تطبيق معمول للشباب التونسي باش يعاونك تفهم روحك<br />
            تعرف تاريخك، تتعلم حاجات تنفعك، وتنظم وقتك بطريقة سهلة💡
          </p>
          <v-btn class="btn" @click.prevent="$emit('openSignUp')">أبدا توا</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </template>
    
  <script>
  import logo from "../assets/logo.png";
  
  export default {
    name: "About",
    data() {
      return {
        logo: logo,
      };
    },
  };
  </script>
    
  <style scoped>
  /* Conteneur principal avec fond prenant tout l'espace */
  .about-container {
    min-height: 100vh; /* Prend toute la hauteur de l'écran */
    background-color: #91b5c9;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 50px 10%;
  }
  
  /* Conteneur du titre et de l'image */
  .title-container {
    display: flex;
    align-items: center;
    gap: 20px; /* Espacement entre le texte et l'image */
    justify-content: center;
  }
  
  /* Style du titre */
  .display-2 {
    color: #152538;
    font-size: 2.8rem;
    text-align: center;
  }
  
  /* Image */
  .logo-img {
    width: 240px;
    height: 200px;
  }
  
  /* Style du paragraphe */
  .subtitle-1 {
    line-height: 1.8;
    text-align: center;
    font-size: 30px;
    font-weight: 600;
    font-family: "Segoe UI", sans-serif;
    color: #152538;
  }
  
  /* Style du bouton */
  .v-btn {
    font-size: 28px;
    color: #152538;
    border: #152538 solid 5px;
    border-radius: 12px;
    padding-bottom: 3%;
    margin-top: 2%;
    position: absolute;
    right: 60%;
  }
  </style>
  