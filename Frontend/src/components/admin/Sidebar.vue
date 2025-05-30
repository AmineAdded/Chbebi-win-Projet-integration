<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
  />
  <nav class="sidebar" :class="{ expanded: isExpanded }">
    <div class="sidebar-header">
      <div class="logo-container">
        <span class="logo-icon">📌</span>
        <h2 class="logo-text">شبابي وين</h2>
      </div>
    </div>

    <div class="user-profile">
      <div class="avatar">
        <i class="fas fa-user-circle fa-2x"></i>
      </div>
      <div class="user-info">
        <p class="user-name">مرحبًا، {{ userName }}</p>
        <span class="user-role">مدير النظام</span>
      </div>
    </div>

    <div class="menu-container">
      <ul class="menu-items">
        <li class="menu-item" :class="{ active: activeMenu === 'classes' }">
          <a
            href="#"
            class="menu-link"
            @click.prevent="setActiveMenu('classes')"
          >
            <span class="menu-icon">📖</span>
            <span class="menu-text">الفصول</span>
          </a>
        </li>

        <li class="menu-item" :class="{ active: activeMenu === 'tests' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('tests')">
            <span class="menu-icon">🧪</span>
            <span class="menu-text">الاختبارات</span>
          </a>
        </li>

        <li class="menu-item" :class="{ active: activeMenu === 'quotes' }">
          <a
            href="#"
            class="menu-link"
            @click.prevent="setActiveMenu('quotes')"
          >
            <span class="menu-icon">💬</span>
            <span class="menu-text">الاقتباسات</span>
          </a>
        </li>

        <li class="menu-item" :class="{ active: activeMenu === 'workshops' }">
          <a
            href="#"
            class="menu-link"
            @click.prevent="setActiveMenu('workshops')"
          >
            <span class="menu-icon">📍</span>
            <span class="menu-text">الفعاليات وورش العمل القراب</span>
          </a>
        </li>

        <li class="menu-item" :class="{ active: activeMenu === 'analytics' }">
          <a
            href="#"
            class="menu-link"
            @click.prevent="setActiveMenu('analytics')"
          >
            <span class="menu-icon">📊</span>
            <span class="menu-text">الإحصائيات</span>
          </a>
        </li>
        <li
          v-if="userRole == 2"
          class="menu-item"
          :class="{ active: activeMenu === 'super-admin' }"
        >
          <a href="#" class="menu-link" @click.prevent="goToSuperAdmin">
            <span class="menu-icon">🛡️</span>
            <span class="menu-text">الإدارة العليا</span>
          </a>
        </li>
      </ul>

      <div class="menu-divider"></div>

      <ul class="menu-items bottom-menu">
        <li class="menu-item" :class="{ active: activeMenu === 'settings' }">
          <a href="#" class="menu-link" @click.prevent="updateInfos">
            <span class="menu-icon">⚙️</span>
            <span class="menu-text">
              {{ userRole == 2 ? "إعدادات المدير" : "إعدادات المشرف" }}
            </span>
          </a>
        </li>

        <li class="menu-item logout">
          <a href="#" class="menu-link" @click.prevent="showLogoutConfirm">
            <span class="menu-icon">🔒</span>
            <span class="menu-text">تسجيل الخروج</span>
          </a>
        </li>
      </ul>
    </div>
  </nav>

  <!-- Modal de confirmation de déconnexion -->
  <Transition name="modal-fade">
    <div
      v-if="showLogoutModal"
      class="modal-overlay"
      @click.self="cancelLogout"
    >
      <div class="modal" :class="{ 'rtl-content': true }">
        <div class="modal-header">
          <h3>تأكيد تسجيل الخروج</h3>
          <button class="close-button" @click="cancelLogout">
            <span>&times;</span>
          </button>
        </div>

        <div class="modal-body">
          <div class="logout-icon">
            <i class="fas fa-sign-out-alt"></i>
          </div>
          <p>هل أنت متأكد أنك تريد تسجيل الخروج من النظام؟</p>
        </div>

        <div class="modal-footer">
          <button class="btn cancel-button" @click="cancelLogout">إلغاء</button>
          <button class="btn confirm-button" @click="confirmLogout">
            تأكيد الخروج
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script>
import { useUserStore } from "@/store/User/userStore";
export default {
  data() {
    return {
      isExpanded: true,
      activeMenu: "SideBar",
      showLogoutModal: false,
      userRole: Number(localStorage.getItem('role'))
    };
  },
  methods: {
    updateInfos() {
      this.$emit("showUpdateAccount");
    },
    showLogoutConfirm() {
      this.showLogoutModal = true;
    },
    cancelLogout() {
      this.showLogoutModal = false;
    },
    confirmLogout() {
      const store = useUserStore();
      store.logout();
      this.showLogoutModal = false;
      this.$router.push("/");
    },
    goToSuperAdmin() {
      if (this.userRole == 2) {
        this.$router.push("/super-admin-verify");
      }
    },
    setActiveMenu(menu) {
      this.activeMenu = menu;
      this.$emit("menuSelected", menu); // scroll depuis le parent
    },
  },
  computed: {
    userName() {
      const store = useUserStore();
      return store.nom;
    },
  },
  mounted() {
    // Initialize sidebar state based on screen size
    if (window.innerWidth <= 768) {
      this.isExpanded = false;
    } else {
      this.isExpanded = true;
    }

    // Add resize listener
    window.addEventListener("resize", () => {
      if (window.innerWidth <= 768) {
        this.isExpanded = false;
      } else {
        this.isExpanded = true;
      }
    });
  },
};
</script>

<style scoped>
/* Styles existants de la barre latérale */

/* Modal styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background-color: #fff;
  border-radius: 12px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  text-align: right;
}

.rtl-content {
  direction: rtl;
}

.modal-header {
  background-color: #f8f9fa;
  padding: 16px 20px;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #3c4257;
  font-size: 18px;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  transition: color 0.2s;
}

.close-button:hover {
  color: #333;
}

.modal-body {
  padding: 24px 20px;
  text-align: center;
}

.logout-icon {
  font-size: 48px;
  color: #f56565;
  margin-bottom: 16px;
}

.modal-body p {
  font-size: 16px;
  color: #4a5568;
  margin-bottom: 0;
  line-height: 1.5;
}

.modal-footer {
  padding: 16px 20px;
  background-color: #f8f9fa;
  border-top: 1px solid #eaeaea;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-button {
  background-color: #f3f4f6;
  color: #4b5563;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.confirm-button {
  background-color: #f56565;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.cancel-button:hover {
  background-color: #e5e7eb;
}

.confirm-button:hover {
  background-color: #e53e3e;
}

/* Animation de transition */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: all 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

.modal-fade-enter-to,
.modal-fade-leave-from {
  opacity: 1;
  transform: scale(1);
}

/* Responsive design */
@media (max-width: 768px) {
  .modal {
    width: 95%;
  }

  .modal-footer {
    flex-direction: column;
  }

  .cancel-button,
  .confirm-button {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>

<style scoped>
.sidebar {
  width: 280px;
  background: linear-gradient(to bottom, #f8f9fa, #e9ecef);
  color: #495057;
  min-height: 100vh;
  right: 0;
  position: fixed;
  text-align: right;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  direction: rtl;
  z-index: 100;
  overflow: hidden;
  border-left: 1px solid #dee2e6;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 1.5rem 1rem;
  background: rgba(255, 255, 255, 0.7);
  border-bottom: 1px solid #e9ecef;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-icon {
  font-size: 1.5rem;
  margin-left: 0.5rem;
  color: #4263eb;
}

.logo-text {
  font-size: 1.3rem;
  font-weight: 600;
  color: #364fc7;
  margin: 0;
  letter-spacing: -0.5px;
}

.user-profile {
  display: flex;
  align-items: center;
  padding: 1rem 1.5rem;
  margin-bottom: 0.5rem;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 12px;
  margin: 1rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.03);
}

.user-profile:hover {
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.avatar {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #4263eb;
  overflow: hidden;
  margin-left: 0.75rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: 2px solid #fff;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-name {
  margin: 0 0 0.2rem;
  font-weight: 600;
  font-size: 0.95rem;
  color: #343a40;
}

.user-role {
  font-size: 0.75rem;
  color: #6c757d;
}

.menu-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  max-height: calc(100vh - 200px);
  padding: 0 0.75rem;
}

.menu-container::-webkit-scrollbar {
  width: 5px;
}

.menu-container::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.02);
}

.menu-container::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.menu-items {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu-item {
  margin: 0.4rem 0;
  border-radius: 8px;
  transition: all 0.3s ease;
  overflow: hidden;
}

.menu-link {
  display: flex;
  align-items: center;
  padding: 0.85rem 1rem;
  color: #495057;
  text-decoration: none;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.menu-icon {
  margin-left: 0.75rem;
  font-size: 1.1rem;
  width: 1.5rem;
  text-align: center;
  transition: transform 0.3s ease;
}

.menu-text {
  font-size: 0.95rem;
  font-weight: 500;
  transition: transform 0.3s ease;
}

.menu-item:hover .menu-link {
  background: rgba(255, 255, 255, 0.8);
  color: #364fc7;
  transform: translateX(-5px);
}

.menu-item:hover .menu-icon {
  transform: scale(1.1);
  color: #4263eb;
}

.menu-item.active .menu-link {
  background: linear-gradient(to right, #4263eb, #364fc7);
  color: white;
  box-shadow: 0 4px 15px rgba(66, 99, 235, 0.2);
}

.menu-divider {
  height: 1px;
  background: rgba(0, 0, 0, 0.06);
  margin: 1rem 0;
}

.bottom-menu {
  margin-top: auto;
  padding: 1rem 0;
  position: sticky;
  bottom: 0;
  background: linear-gradient(
    to bottom,
    rgba(248, 249, 250, 0.9) 0%,
    rgba(233, 236, 239, 0.9) 100%
  );
  backdrop-filter: blur(5px);
  z-index: 2;
  border-top: 1px solid #dee2e6;
}

.menu-item.logout .menu-link {
  color: #e03131;
}

.menu-item.logout:hover .menu-link {
  background: rgba(224, 49, 49, 0.05);
}

@media (max-width: 768px) {
  .sidebar {
    width: 70px;
  }

  .sidebar.expanded {
    width: 280px;
  }

  .logo-text,
  .user-info,
  .menu-text {
    display: none;
    opacity: 0;
    transition: opacity 0.2s ease;
  }

  .sidebar.expanded .logo-text,
  .sidebar.expanded .user-info,
  .sidebar.expanded .menu-text {
    display: block;
    opacity: 1;
  }

  .menu-icon {
    margin-left: 0;
  }

  .sidebar.expanded .menu-icon {
    margin-left: 0.75rem;
  }

  .user-profile {
    justify-content: center;
    padding: 1rem 0;
  }

  .sidebar.expanded .user-profile {
    justify-content: flex-start;
    padding: 1rem 1.5rem;
  }

  .toggle-icon {
    transform: rotate(180deg);
    transition: transform 0.3s ease;
  }

  .sidebar.expanded .toggle-icon {
    transform: rotate(0);
  }
  .menu-container {
    max-height: calc(100vh - 200px);
  }

  .bottom-menu {
    padding: 0.5rem 0;
  }
}
</style>
<!-- </style>

<script>
export default {
  data() {
    return {
      isExpanded: true,
      activeMenu: 'home',
      userRole: localStorage.getItem('role')
    };
  },
  methods: {
    toggleSidebar() {
      this.isExpanded = !this.isExpanded;
    },
    setActiveMenu(menu) {
      this.activeMenu = menu;
      // Vous pouvez émettre un événement pour informer le composant parent
      this.$emit('menu-changed', menu);
    },
    goToSuperAdmin() {
      if (this.userRole == 2) {
        this.$router.push('/super-admin-verify');
      }
    }
  },
  mounted() {
    // Initialize sidebar state based on screen size
    if (window.innerWidth <= 768) {
      this.isExpanded = false;
    } else {
      this.isExpanded = true;
    }

    // Add resize listener
    window.addEventListener('resize', () => {
      if (window.innerWidth <= 768) {
        this.isExpanded = false;
      } else {
        this.isExpanded = true;
      }
    });
  }
};
</script> -->
