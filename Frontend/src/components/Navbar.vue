<template>
  <div class="navbar-container">
    <v-app-bar color="white" elevation="2" class="custom-navbar" height="80">
      <!-- Bouton de menu hamburger pour mobile -->
      <v-app-bar-nav-icon @click="drawer = !drawer" class="d-flex d-md-none"></v-app-bar-nav-icon>
      
      <!-- Logo pour mobile (centré) -->
      <v-toolbar-title class="d-flex d-md-none justify-center flex-grow-1">
        <img :src="logo" alt="logo" class="logo-img" @click="$router.push('/')" style="cursor: pointer;" />
      </v-toolbar-title>
      
      <!-- Liens de navigation pour grand écran -->
      <v-container class="nav-container d-none d-md-flex">
        <v-btn text to="/agenda" class="nav-btn">أجندة</v-btn>
        <v-btn text to="/time-management" class="nav-btn">إدارة الوقت</v-btn>
        <v-btn text to="/culture" class="nav-btn">الثقافة التونسية</v-btn>
        <v-btn text to="/islam" class="nav-btn">الدين الإسلامي</v-btn>

        <v-spacer></v-spacer>

        <!-- Logo cliquable aligné à droite (desktop) -->
        <v-btn text to="/" class="nav-btn">الصفحة الرئيسية</v-btn>
        <img :src="logo" alt="logo" class="logo-img" />
      </v-container>
    </v-app-bar>
    
    <!-- Drawer pour mobile -->
    <v-navigation-drawer v-model="drawer" temporary app right>
      <v-list>
        <v-list-item class="text-center py-4">
          <v-list-item-content>
            <img :src="logo" alt="logo" class="drawer-logo" />
          </v-list-item-content>
        </v-list-item>
        
        <v-divider></v-divider>
        
        <v-list-item to="/" link>
          <v-list-item-content class="text-right">
            <v-list-item-title class="nav-drawer-item">الصفحة الرئيسية</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/islam" link>
          <v-list-item-content class="text-right">
            <v-list-item-title class="nav-drawer-item">الدين الإسلامي</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/culture" link>
          <v-list-item-content class="text-right">
            <v-list-item-title class="nav-drawer-item">الثقافة التونسية</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/time-management" link>
          <v-list-item-content class="text-right">
            <v-list-item-title class="nav-drawer-item">إدارة الوقت</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        
        <v-list-item to="/agenda" link>
          <v-list-item-content class="text-right">
            <v-list-item-title class="nav-drawer-item">أجندة</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import logo from "../assets/logo.png";

export default {
  name: "Navbar",
  data() {
    return {
      logo: logo,
      drawer: false
    };
  },
};
</script>

<style scoped>
/* Style de la NavBar */
.custom-navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  padding: 0 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
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

/* Style pour le drawer mobile */
.drawer-logo {
  width: 60px;
  height: auto;
  margin: 0 auto;
}

.nav-drawer-item {
  font-size: 18px;
  font-weight: bold;
  color: #152538;
  font-family: "Segoe UI", sans-serif;
}

/* Media queries pour différentes tailles d'écran */
@media (max-width: 960px) {
  .custom-navbar {
    padding: 0 10px;
  }
  
  .logo-img {
    width: 60px;
  }
}

@media (max-width: 600px) {
  .custom-navbar {
    height: 60px !important;
  }
  
  .logo-img {
    width: 50px;
  }
}
</style>