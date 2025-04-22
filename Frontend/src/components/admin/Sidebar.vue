<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
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
        <p class="user-name">مرحبًا، {{userName}}</p>
        <span class="user-role">مدير النظام</span>
      </div>
    </div>
    
    <div class="menu-container">
      <ul class="menu-items">
        <!-- <li class="menu-item" :class="{ active: activeMenu === 'home' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('home')">
            <span class="menu-icon">🏠</span>
            <span class="menu-text">الرئيسية</span>
          </a>
        </li> -->
        
        <li class="menu-item" :class="{ active: activeMenu === 'users' }">
          <a href="/" class="menu-link" @click.prevent="setActiveMenu('users')">
            <span class="menu-icon">👥</span>
            <span class="menu-text">المستخدمين</span>
          </a>
        </li>
        
        <li class="menu-item" :class="{ active: activeMenu === 'classes' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('classes')">
            <span class="menu-icon">📖</span>
            <span class="menu-text">الفصول</span>
          </a>
        </li>
        
        <li class="menu-item" :class="{ active: activeMenu === 'surveys' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('surveys')">
            <span class="menu-icon">📝</span>
            <span class="menu-text">الاستبيانات</span>
          </a>
        </li>
        
        <li class="menu-item" :class="{ active: activeMenu === 'tests' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('tests')">
            <span class="menu-icon">🧪</span>
            <span class="menu-text">الاختبارات</span>
          </a>
        </li>
        
        <li class="menu-item" :class="{ active: activeMenu === 'quotes' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('quotes')">
            <span class="menu-icon">💬</span>
            <span class="menu-text">الاقتباسات</span>
          </a>
        </li>
        
        <li class="menu-item" :class="{ active: activeMenu === 'analytics' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('analytics')">
            <span class="menu-icon">📊</span>
            <span class="menu-text">التحليلات</span>
          </a>
        </li>
      </ul>
      
      <div class="menu-divider"></div>
      
      <ul class="menu-items bottom-menu">
        <li class="menu-item" :class="{ active: activeMenu === 'settings' }">
          <a href="#" class="menu-link" @click.prevent="setActiveMenu('settings')">
            <span class="menu-icon">⚙️</span>
            <span class="menu-text">الإعدادات</span>
          </a>
        </li>
        
        <li class="menu-item logout">
          <a href="#" class="menu-link" @click.prevent="logout">
            <span class="menu-icon">🔒</span>
            <span class="menu-text">تسجيل الخروج</span>
          </a>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { useUserStore } from '@/store/User/userStore';
export default {
  data() {
    return {
      isExpanded: true,
      activeMenu: 'SideBar'
    };
  },
  methods: {
    setActiveMenu(menu) {
      this.activeMenu = menu;
      // Vous pouvez émettre un événement pour informer le composant parent
      this.$emit('menu-changed', menu);
    },
    logout(){
      const store =useUserStore();
      store.logout();
      this.$router.push('/');
    }
  },
  computed:{
    userName(){
      const store = useUserStore();
      return store.nom;
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
</script>

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
  padding-bottom: 1.5rem;
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
}
</style>