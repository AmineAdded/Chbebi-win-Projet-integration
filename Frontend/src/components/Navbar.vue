<template>
  <div class="navbar-container">
    <v-app-bar color="white" elevation="2" class="custom-navbar" height="80">
      <!-- Bouton hamburger (mobile) -->
      <v-app-bar-nav-icon
        @click="drawer = !drawer"
        class="d-flex d-md-none"
      ></v-app-bar-nav-icon>

      <!-- Logo mobile -->
      <v-toolbar-title class="d-flex d-md-none justify-center flex-grow-1">
        <img
          :src="logo"
          alt="logo"
          class="logo-img"
          @click="$router.push('/')"
          style="cursor: pointer"
        />
      </v-toolbar-title>

      <!-- Liens desktop -->
      <v-container class="nav-container d-none d-md-flex">
        <v-btn text class="nav-btn" @click="handleAgendaClick">أجندة</v-btn>
        <v-btn text class="nav-btn" @click="handleTimeManagementClick"
          >إدارة الوقت</v-btn
        >

        <!-- Menu déroulant desktop amélioré -->
        <v-menu
          offset-y
          transition="slide-y-transition"
          :close-on-content-click="false"
          open-on-hover
          :open-delay="100"
          :close-delay="100"
          max-width="320"
        >
          <template v-slot:activator="{ props }">
            <v-btn
              text
              class="nav-btn theme-menu-btn"
              v-bind="props"
              @mouseenter="hoverTheme = true"
              @mouseleave="hoverTheme = false"
            >
              المواضيع
              <v-icon :color="hoverTheme ? 'primary' : ''" class="ms-2">
                {{ hoverTheme ? "mdi-chevron-up" : "mdi-chevron-down" }}
              </v-icon>
            </v-btn>
          </template>

          <v-card class="theme-menu-card" elevation="8">
            <v-card-title class="pb-0 text-primary font-weight-bold text-center">
              <v-icon color="primary" class="me-2">mdi-bookmark-multiple</v-icon>
              المواضيع المتاحة
            </v-card-title>
            <v-divider class="mt-2"></v-divider>
            
            <v-list class="theme-menu-list py-2" v-if="thematics && thematics.length > 0">
              <v-list-item
                v-for="(thematic, index) in thematics"
                :key="index"
                :to="'/theme/' + thematic.id"
                link
                active-class="theme-menu-item-active"
                class="theme-menu-item"
              >
                <template v-slot:prepend>
                  <v-icon color="grey" class="theme-icon">mdi-label</v-icon>
                </template>
                <v-list-item-title class="text-right">{{ thematic.nom }}</v-list-item-title>
              </v-list-item>
            </v-list>
            
            <!-- Message quand la liste est vide -->
            <v-card-text v-else class="text-center py-6">
              <v-icon size="64" color="grey" class="mb-3">mdi-alert-circle-outline</v-icon>
              <p class="text-subtitle-1 font-weight-medium text-center">لا توجد مواضيع متاحة حالياً</p>
              <p class="text-body-2 text-grey text-center">سيتم إضافة مواضيع جديدة قريباً</p>
            </v-card-text>

            <v-divider v-if="thematics && thematics.length > 0"></v-divider>
            
          </v-card>
        </v-menu>

        <v-spacer></v-spacer>
        <v-btn text class="nav-btn" @click="handleClick">الصفحة الرئيسية</v-btn>
        <img :src="logo" alt="logo" class="logo-img" />

        <!-- Menu avatar desktop -->
        <v-menu offset-y v-if="showMenu">
          <template v-slot:activator="{ props }">
            <span class="user-name">{{ userName }}</span>
            <v-btn icon class="avatar-btn ml-4" v-bind="props">
              <v-avatar color="#152538" size="40">
                <v-icon color="white">mdi-account</v-icon>
              </v-avatar>
            </v-btn>
          </template>

          <v-card class="user-menu-card">
            <v-list>
              <v-list-item prepend-icon="mdi-account-circle" class="user-menu-header" density="compact">
                <v-list-item-title class="text-right font-weight-bold">{{ userName }}</v-list-item-title>
              </v-list-item>
              <v-divider></v-divider>
              <v-list-item @click="$emit('openUpdateAccount')" class="user-menu-item">
                <template v-slot:prepend>
                  <v-icon color="primary">mdi-account-cog</v-icon>
                </template>
                <v-list-item-title class="text-right">إدارة الحساب</v-list-item-title>
              </v-list-item>

              <v-divider></v-divider>

              <v-list-item @click="logout" class="user-menu-item">
                <template v-slot:prepend>
                  <v-icon color="error">mdi-logout</v-icon>
                </template>
                <v-list-item-title class="text-right">تسجيل الخروج</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card>
        </v-menu>
      </v-container>
    </v-app-bar>

    <!-- Drawer mobile -->
    <v-navigation-drawer v-model="drawer" temporary app right>
      <v-list>
        <v-list-item class="text-center py-4">
          <v-list-item-content>
            <img :src="logo" alt="logo" class="drawer-logo" />
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>

        <v-list-item @click="handleClick" link>
          <v-list-item-title class="text-right nav-drawer-item">
            الصفحة الرئيسية
          </v-list-item-title>
        </v-list-item>

        <!-- Menu déroulant mobile pour المواضيع - AMÉLIORÉ -->
        <v-list-group prepend-icon="mdi-bookmark-multiple" color="primary">
          <template v-slot:activator="{ props }">
            <v-list-item v-bind="props" class="text-right">
              <v-list-item-title class="nav-drawer-item">المواضيع</v-list-item-title>
            </v-list-item>
          </template>

          <div v-if="thematics && thematics.length > 0">
            <v-list-item 
              v-for="(thematic, index) in thematics" 
              :key="index"
              :to="'/theme/' + thematic.id" 
              link
              class="mobile-theme-item"
            >
              <template v-slot:prepend>
                <v-icon size="small" color="grey" class="me-2">mdi-label</v-icon>
              </template>
              <v-list-item-title class="text-right nav-drawer-item pr-4">
                {{ thematic.nom }}
              </v-list-item-title>
            </v-list-item>
          </div>
          <v-list-item v-else>
            <v-list-item-title class="text-right text-grey">
              لا توجد مواضيع متاحة حالياً
            </v-list-item-title>
          </v-list-item>
        </v-list-group>

        <v-list-item link @click.prevent="handleTimeManagementClick">
          <v-list-item-title class="text-right nav-drawer-item">
            إدارة الوقت
          </v-list-item-title>
        </v-list-item>

        <v-list-item link @click.prevent="handleAgendaClick">
          <v-list-item-title class="text-right nav-drawer-item">
            أجندة
          </v-list-item-title>
        </v-list-item>

        <v-divider></v-divider>

        <v-list-item @click="$emit('openUpdateAccount')">
          <v-list-item-title class="text-right nav-drawer-item">
            إدارة الحساب</v-list-item-title
          >
        </v-list-item>

        <v-list-item @click="logout">
          <v-list-item-title class="text-right nav-drawer-item"
            >تسجيل الخروج</v-list-item-title
          >
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Dialog de confirmation -->
    <v-dialog v-model="logoutDialog" max-width="400">
      <v-card>
        <v-card-title class="text-right text-h5">تسجيل الخروج</v-card-title>
        <v-card-text class="text-right">هل أنت متأكد أنك تريد تسجيل الخروج؟</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="logoutDialog = false">إلغاء</v-btn>
          <v-btn color="error" text @click="confirmLogout">تأكيد</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import thematicService from "@/Services/thematicService";
import logo from "../assets/logo.png";
import { useUserStore } from "@/store/User/userStore";

export default {
  name: "Navbar",
  data() {
    return {
      logo: logo,
      drawer: false,
      logoutDialog: false,
      hoverTheme: false,
      thematics: [],
      isLoading: true,
      hasError: false
    };
  },
  mounted() {
    this.getAllThematics();
  },
  computed: {
    showMenu() {
      const store = useUserStore();
      return !!store.accessToken;
    },
    userName() {
      const store = useUserStore();
      return store.nom;
    },
    isLoggedIn() {
      const store = useUserStore();
      return !!store.accessToken;
    },
  },
  methods: {
    async getAllThematics() {
      this.isLoading = true;
      this.hasError = false;
      try {
        const response = await thematicService.getAllThematics();
        this.thematics = response.data;
      } catch (error) {
        console.error("Erreur lors de la récupération des thématiques:", error);
        this.hasError = true;
        this.thematics = [];
      } finally {
        this.isLoading = false;
      }
    },
    logout() {
      this.logoutDialog = true;
    },
    confirmLogout() {
      this.logoutDialog = false;
      const store = useUserStore();
      store.logout();
      this.$router.push("/");
    },
    handleTimeManagementClick() {
      if (this.isLoggedIn) {
        this.$router.push("/time-management");
      } else {
        this.$emit("openLogin");
      }
    },
    handleAgendaClick() {
      if (this.isLoggedIn) {
        this.$router.push("/agenda");
      } else {
        this.$emit("openLogin");
      }
    },
    handleClick() {
      if (this.isLoggedIn) {
        this.$router.push("/privatehome");
      } else {
        this.$router.push("/");
      }
    },
  },
};
</script>

<style scoped>
/* Styles améliorés pour le dropdown desktop */
.theme-menu-btn {
  position: relative;
  transition: all 0.3s ease;
  font-family: 'Segoe UI', 'Dubai', sans-serif;
}

.theme-menu-btn:hover {
  color: #2d80d5 !important;
}

.theme-menu-btn::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 2px;
  background: #2d80d5;
  transition: width 0.3s ease;
}

.theme-menu-btn:hover::after {
  width: 70%;
}

.theme-menu-card {
  border-radius: 12px !important;
  overflow: hidden;
  min-width: 280px;
  margin-top: 8px;
  border: 1px solid #eee;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1) !important;
}

.theme-menu-list {
  padding: 4px 0;
  max-height: 320px;
  overflow-y: auto;
}

.theme-menu-item {
  transition: all 0.25s ease;
  padding: 8px 16px;
  margin: 2px 6px;
  border-radius: 6px;
}

.theme-menu-item:hover {
  background-color: rgba(45, 128, 213, 0.05) !important;
}

.theme-menu-item-active {
  background-color: rgba(45, 128, 213, 0.1) !important;
  color: #2d80d5 !important;
}

.theme-icon {
  transition: all 0.25s ease;
}

.theme-menu-item:hover .theme-icon {
  transform: translateX(-3px);
  color: #2d80d5 !important;
}

.user-menu-card {
  border-radius: 12px !important;
  overflow: hidden;
  min-width: 200px;
  margin-top: 8px;
  border: 1px solid #eee;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1) !important;
}

.user-menu-header {
  background-color: #f5f9ff;
  padding: 12px 16px !important;
}

.user-menu-item {
  transition: all 0.2s ease;
}

.user-menu-item:hover {
  background-color: #f5f9ff !important;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #152538;
  font-family: "Segoe UI", "Dubai", sans-serif;
  position: relative;
  left: 60px;
}

.custom-navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  padding: 0 20px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.06);
}

.nav-container {
  display: flex;
  align-items: center;
}

.nav-btn {
  font-size: 20px;
  font-weight: bold;
  font-family: "Segoe UI", "Dubai", sans-serif;
  color: #152538;
  transition: all 0.3s ease-in-out;
  padding: 0 15px;
  height: 40px;
  border-radius: 6px;
}

.nav-btn:hover {
  color: #2d80d5;
  text-decoration: none;
  background-color: rgba(45, 128, 213, 0.05);
}

.logo-img {
  width: 75px;
  height: auto;
  transition: transform 0.3s ease-in-out;
}

.logo-img:hover {
  transform: scale(1.1);
}

.drawer-logo {
  width: 60px;
  height: auto;
  margin: 0 auto;
}

.nav-drawer-item {
  font-size: 18px;
  font-weight: bold;
  color: #152538;
  font-family: "Segoe UI", "Dubai", sans-serif;
}

.mobile-theme-item {
  padding-right: 32px !important;
  border-radius: 0;
  transition: all 0.2s ease;
}

.mobile-theme-item:hover {
  background-color: rgba(45, 128, 213, 0.05) !important;
  color: #2d80d5;
}

.avatar-btn {
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
  position: absolute;
  right: 20px;
}

.avatar-btn:hover {
  transform: scale(1.1);
}

@media (max-width: 960px) {
  .custom-navbar {
    padding: 0 10px;
  }

  .logo-img {
    width: 60px;
  }

  .v-toolbar-title {
    position: absolute;
    right: 10px;
  }
}

@media (max-width: 600px) {
  .custom-navbar {
    height: 60px !important;
  }

  .logo-img {
    width: 50px;
  }

  .v-toolbar-title {
    position: absolute;
    right: 10px;
  }
}
</style>