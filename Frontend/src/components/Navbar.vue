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
        <v-btn text class="nav-btn" @click="handleTimeManagementClick">إدارة الوقت</v-btn>
        <v-btn text to="/islamic/:topicId" class="nav-btn">الثقافة التونسية</v-btn>
        <v-btn text to="/islam" class="nav-btn">الدين الإسلامي</v-btn>
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

          <v-card>
            <v-list>
              <v-list-item @click="$emit('openUpdateAccount')">
                <v-list-item-icon>
                  <v-icon>mdi-account-cog</v-icon>
                </v-list-item-icon>
                <v-list-item-title >إدارة الحساب</v-list-item-title>
              </v-list-item>
              

              <v-divider></v-divider>

              <v-list-item @click="logout">
                <v-list-item-icon>
                  <v-icon>mdi-logout</v-icon>
                </v-list-item-icon>
                <v-list-item-title>تسجيل الخروج</v-list-item-title>
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
          <v-list-item-title class="text-right nav-drawer-item"
            >الصفحة الرئيسية</v-list-item-title
          >
        </v-list-item>

        <v-list-item to="/islam" link>
          <v-list-item-title class="text-right nav-drawer-item"
            >الدين الإسلامي</v-list-item-title
          >
        </v-list-item>

        <v-list-item to="/islamic/:topicId" link>
          <v-list-item-title class="text-right nav-drawer-item"
            >الثقافة التونسية</v-list-item-title
          >
        </v-list-item>

        <v-list-item link @click.prevent="handleTimeManagementClick">
          <v-list-item-title class="text-right nav-drawer-item">
            إدارة الوقت
          </v-list-item-title>
        </v-list-item>

        <v-list-item link @click.prevent="handleAgendaClick">
          <v-list-item-title class="text-right nav-drawer-item"
            >أجندة</v-list-item-title
          >
        </v-list-item>

        <v-divider></v-divider>

       
        <v-list-item @click="$emit('openUpdateAccount')">
          <v-list-item-title class="text-right nav-drawer-item"
            > إدارة الحساب</v-list-item-title
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
        <v-card-title class="text-right">تسجيل الخروج</v-card-title>
        <v-card-text class="text-right"
          >هل أنت متأكد أنك تريد تسجيل الخروج؟</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey darken-1" text @click="logoutDialog = false"
            >إلغاء</v-btn
          >
          <v-btn color="red darken-1" text @click="confirmLogout">تأكيد</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import logo from "../assets/logo.png";
import { useUserStore } from "@/store/User/userStore";

export default {
  name: "Navbar",
  data() {
    return {
      logo: logo,
      drawer: false,
      logoutDialog: false,
    };
  },
  computed: {
    showMenu() {
      const store = useUserStore();
      if (store.accessToken) {
        return true;
      } else {
        return false;
      }
    },
    userName() {
      const store = useUserStore();
      return store.user.nom;
    },
    isLoggedIn() {
      const store = useUserStore();
      if (store.accessToken) {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
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
        console.log(this.isLoggedIn);
        this.$router.push('/time-management');
      } else {
        this.$emit('openLogin');
      }
    },
    handleAgendaClick() {
      if (this.isLoggedIn) {
        console.log(this.isLoggedIn);
        this.$router.push('/agenda');
      } else {
        this.$emit('openLogin');
      }
    }
  },
  handleClick(){
    if(this.isLoggedIn) {
      this.$router.push('/privatehome');
    } else {
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #152538;
  font-family: "Segoe UI", sans-serif;
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
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

.nav-container {
  display: flex;
  align-items: center;
}

.nav-btn {
  font-size: 20px;
  font-weight: bold;
  font-family: "Segoe UI", sans-serif;
  color: #152538;
  transition: all 0.3s ease-in-out;
  padding: 0 15px;
}

.nav-btn:hover {
  color: #2d80d5;
  text-decoration: none;
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
  font-family: "Segoe UI", sans-serif;
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
