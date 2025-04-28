<template>
  <div class="super-admin-page">
    <!-- صلاحية الدخول -->
    <div v-if="unauthorized" class="unauthorized-message">
      <v-alert type="error">
        ⚠️ ليس لديك صلاحية الوصول إلى هذه الصفحة
      </v-alert>
    </div>

    <!-- محتوى الصفحة -->
    <div v-else class="admin-content">
      <div class="nav"><Sidebar @showUpdateAccount="show = true" /></div>
      <div class="main-content">
        <h1 class="page-title">👑 لوحة الإدارة العليا</h1>
        <user-table 
          :is-super-admin="true"
          @role-updated="handleRoleUpdate"
          @user-deleted="handleUserDelete"
        />
      </div>
    </div>
    <v-dialog v-model="show" persistent width="auto">
        <UpdateAccount @closeUpdateAccount="show = false" />
    </v-dialog>
  </div>
</template>

<script>
import Sidebar from '@/components/admin/Sidebar.vue'
import UserTable from '@/components/admin/UsersTable.vue'
import axios from 'axios'
import UpdateAccount from "@/components/UpdateAccount.vue";


export default {
  components: { 
    UserTable,
    Sidebar,
    UpdateAccount
  },
  data() {
    return {
      unauthorized: false,
      show: false,
    }
  },
  mounted() {
    this.checkPermissions()
  },
  methods: {
    checkPermissions() {
      const userRole = parseInt(localStorage.getItem('role'))
      if (userRole !== 2) {
        this.unauthorized = true
        setTimeout(() => {
          this.$router.push('/dashboard')
        }, 3000)
      }
    },
    async handleRoleUpdate(user) {
      try {
        await axios.put(`/api/users/${user.id}/role`, {
          newRole: user.role
        }, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        })
        this.$toast.success('تم تحديث الدور بنجاح')
      } catch (error) {
        this.$toast.error('فشل في تحديث الدور')
      }
    },
    async handleUserDelete(userId) {
      try {
        await axios.delete(`/api/users/${userId}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('token')}`
          }
        })
        this.$toast.success('تم حذف المستخدم بنجاح')
      } catch (error) {
        this.$toast.error('فشل في حذف المستخدم')
      }
    }
  }
}
</script>

<style scoped>
.super-admin-page {
  direction: rtl;
  background-color: #f9f9f9;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Layout container with sidebar and main content */
.admin-content {
  display: flex;
}

/* Sidebar expected to be fixed on the right */
.sidebar {
  width: 250px;
  min-height: 100vh;
  background-color: #fff;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.05);
  position: fixed;
  right: 0;
  top: 0;
  z-index: 1000;
}

/* Main content area with margin to the left of sidebar */
.main-content {
  flex: 1;
  padding: 2rem;
  margin-right: 250px; /* match sidebar width */
  box-sizing: border-box;
}

/* Titles */
.page-title {
  color: #2D80D5;
  border-bottom: 3px solid #2D80D5;
  padding-bottom: 1rem;
  margin-bottom: 2rem;
  font-size: 28px;
}

.page-subtitle {
  font-size: 20px;
  margin-bottom: 1.5rem;
  color: #444;
}

/* Unauthorized message */
.unauthorized-message {
  max-width: 600px;
  margin: 4rem auto;
  text-align: center;
}
</style>
