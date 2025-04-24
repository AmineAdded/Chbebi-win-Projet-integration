<template>
  <div class="super-admin-verify">
    <v-card class="verify-card" elevation="5">
      <div class="card-content">
        <div class="card-header">
          <div class="logo-container">
            <v-avatar size="72" color="primary" class="elevation-3">
              <v-icon size="40" color="white">mdi-shield-lock</v-icon>
            </v-avatar>
          </div>
          <h1 class="text-h4 font-weight-bold primary--text mt-6 mb-2">التحقق من هوية المدير العام</h1>
          <p class="text-subtitle-1 text-medium-emphasis mb-6">يرجى إدخال بيانات تسجيل الدخول للمتابعة</p>
        </div>

        <v-divider class="divider-style"></v-divider>

        <v-form @submit.prevent="verifyPassword" class="auth-form">
          <v-text-field
            v-model="password"
            :type="showPassword ? 'text' : 'password'"
            label="كلمة المرور"
            placeholder="أدخل كلمة مرور حساب المدير العام"
            :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append="showPassword = !showPassword"
            prepend-inner-icon="mdi-lock-outline"
            :error-messages="error ? [error] : []"
            dense
            outlined
            color="primary"
            class="mb-8 password-field"
            hide-details="auto"
            autocomplete="current-password"
          ></v-text-field>

          <v-btn
            type="submit"
            color="primary"
            x-large
            block
            :loading="loading"
            :disabled="!password"
            height="56"
            class="login-btn font-weight-bold"
            elevation="2"
          >
            <span class="mr-2">تحقق من الهوية</span>
            <v-icon>mdi-login-variant</v-icon>
          </v-btn>

          <v-expand-transition>
            <v-alert
              v-if="error"
              type="error"
              class="mt-6 error-alert"
              dense
              border="left"
              elevation="2"
              prominent
            >
              <template v-slot:prepend>
                <v-icon color="error">mdi-alert-circle</v-icon>
              </template>
              {{ error }}
            </v-alert>
          </v-expand-transition>
        </v-form>
      </div>
    </v-card>

    <div class="security-notice">
      <v-icon small color="primary" class="mr-1">mdi-shield-check</v-icon>
      <span>تم تأمين اتصالك بتشفير متقدم لحماية بياناتك</span>
    </div>
  </div>
</template>

<script>
import userService from '../Services/userService.js';

export default {
  data() {
    return {
      password: '',
      showPassword: false,
      loading: false,
      error: ''
    };
  },
  methods: {
    async verifyPassword() {
      if (!this.password) return;
      
      this.loading = true;
      this.error = '';

      try {
        const email = localStorage.getItem('email');
        console.log(email);

        await userService.logSuperAdmin(email, this.password);
        
        // Animation de succès avant redirection
        this.$store.dispatch('showSnackbar', {
          text: 'تم تسجيل الدخول بنجاح',
          color: 'success'
        });
        
        setTimeout(() => {
          this.$router.push('/super-admin');
        }, 500);

      } catch (err) {
        this.error = err.response?.data?.message || "فشل التحقق. يرجى التأكد من صحة كلمة المرور";
      } finally {
        this.loading = false;
      }
    }
  },
  mounted() {
    // Focus automatique sur le champ de mot de passe
    this.$nextTick(() => {
      const passwordField = this.$el.querySelector('input');
      if (passwordField) passwordField.focus();
    });
  }
};
</script>

<style scoped>
.super-admin-verify {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(145deg, #f8fafc 0%, #e2e8f0 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.super-admin-verify::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 200px;
  background: linear-gradient(to bottom right, rgba(59, 130, 246, 0.1), transparent);
  z-index: 0;
}

.verify-card {
  width: 100%;
  max-width: 480px;
  border-radius: 24px !important;
  overflow: hidden;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.08) !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.card-content {
  padding: 2.5rem;
}

.card-header {
  text-align: center;
  margin-bottom: 2rem;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.divider-style {
  margin: 1.5rem 0;
  opacity: 0.6;
}

.auth-form {
  margin-top: 2rem;
}

.password-field >>> .v-input__slot {
  border-radius: 12px !important;
  transition: all 0.2s ease;
}

.password-field >>> .v-input__slot:hover {
  border-color: var(--v-primary-base) !important;
}

.login-btn {
  border-radius: 12px !important;
  text-transform: none;
  letter-spacing: 0.5px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 15px rgba(var(--v-primary-base), 0.2) !important;
}

.error-alert {
  border-radius: 8px;
  font-size: 14px;
}

.security-notice {
  display: flex;
  align-items: center;
  margin-top: 24px;
  font-size: 13px;
  color: #64748b;
  background: rgba(255, 255, 255, 0.7);
  padding: 8px 16px;
  border-radius: 50px;
  backdrop-filter: blur(5px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* RTL support */
* {
  direction: rtl;
}

.v-text-field >>> .v-input__append-inner {
  margin-left: 0;
  margin-right: 12px;
}

.v-text-field >>> .v-input__prepend-inner {
  margin-right: 0;
  margin-left: 12px;
}

/* Animation subtile sur la carte */
@keyframes subtle-float {
  0% { transform: translateY(0); }
  50% { transform: translateY(-5px); }
  100% { transform: translateY(0); }
}

.verify-card:hover {
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.12) !important;
  animation: subtle-float 3s ease-in-out infinite;
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .card-content {
    padding: 1.5rem;
  }
  
  .verify-card {
    max-width: 95%;
  }
}
</style>