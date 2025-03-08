<template>
  <v-app-bar color="white" elevation="2" class="custom-navbar" height="80">
    <!-- Liens de navigation alignés à gauche -->
    <v-container class="nav-container">
      <v-btn text to="/agenda" class="nav-btn">أجندة</v-btn>
      <v-btn text to="/time-management" class="nav-btn">إدارة الوقت</v-btn>
      <v-btn text to="/culture" class="nav-btn">الثقافة التونسية</v-btn>
      <v-btn text to="/islam" class="nav-btn">دين الإسلام</v-btn>

      <v-spacer></v-spacer>

      <!-- Logo cliquable aligné à droite -->
      <v-btn text to="/" class="nav-btn">الصفحة الرئيسية</v-btn>
      <img :src="logo" alt="logo" class="logo-img" />
    </v-container>
  </v-app-bar>
</template>

<script>
import logo from "../assets/logo.png";

export default {
  name: "Navbar",
  data() {
    return {
      logo: logo,
    };
  },
};
</script>

<style scoped>
/* Style de la NavBar */
.custom-navbar {
  position: fixed; /* Fixe la barre en haut */
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  padding: 0 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); /* Ombre subtile */
}

/* Conteneur des liens */
.nav-container {
  display: flex;
  align-items: center;
}

/* Style des boutons de navigation */
.nav-btn {
  font-size: 20px;
  font-weight: bold;
  font-family: "Segoe UI", sans-serif;
  color: #152538;
  transition: all 0.3s ease-in-out;
  padding: 0 15px;
}

/* Effet au survol */
.nav-btn:hover {
  color: #2d80d5;
  text-decoration: none;
}

/* Style du logo */
.logo-img {
  width: 75px;
  height: auto;
  transition: transform 0.3s ease-in-out;
}

/* Effet au survol du logo */
.logo-img:hover {
  transform: scale(1.1);
}
</style>
