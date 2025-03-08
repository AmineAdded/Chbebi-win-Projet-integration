<template>
  <v-container fluid class="about-container">
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
        <v-btn class="btn" elevation="3" @click.prevent="$emit('openSignUp')">
          أبدا توّا
        </v-btn>
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
/* Conteneur principal */
.about-container {
  padding: 40px 20px;
  background-color: #91b5c9;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Conteneur du titre et de l'image */
.title-container {
  display: flex;
  align-items: center;
  gap: 20px; /* Espacement entre le texte et l'image */
  justify-content: center;
  flex-wrap: wrap; /* Permet à l'image et au titre de s'adapter sur mobile */
}

/* Style du titre */
.display-2 {
  color: #152538;
  font-size: 2.8rem;
  text-align: center;
  max-width: 100%;
}

/* Image */
.logo-img {
  width: 200px;
  height: auto;
  max-width: 100%;
}

/* Style du paragraphe */
.subtitle-1 {
  line-height: 1.8;
  text-align: center;
  font-size: 1.4rem;
  font-weight: 600;
  font-family: "Segoe UI", sans-serif;
  color: #152538;
  max-width: 700px; /* Limite la largeur du texte */
  margin: 0 auto; /* Centre le texte */
}

/* Style du bouton */
.btn {
  font-size: 1.7rem;
  color: #152538;
  border: 3px solid #152538;
  border-radius: 12px;
  position: relative;
  right: 18%;
  padding-bottom: 3%;
  margin-top: 20px; 
  transition: all 0.3s ease-in-out;
  background-color: white;
  font-family: "Segoe UI", sans-serif;
}

/* Effet au survol */
.btn:hover {
  background-color: #152538;
  color: white;
  transform: scale(1.05);
  box-shadow: 0px 6px 12px rgba(0, 0, 0, 0.2);
}
</style>
