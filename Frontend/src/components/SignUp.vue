<template>
  <div class="signup-wrapper">
    <v-container>
      <v-card class="login-card mx-auto pa-8" elevation="10">
        <div class="logo-container text-center mb-5">
          <img :src="logo" alt="ChbebiWin Logo" class="logo-image" />
          <div class="tagline">استمتع بالتحدي. اربح المزيد.</div>
        </div>

        <v-form class="form-container">
          <h2 class="text-center mb-6 headline-text">إنشاء حساب جديد</h2>

          <v-sheet class="field-container mb-4" elevation="0">
            <v-text-field v-model="fullName" placeholder="اسمك الكامل" variant="outlined" density="comfortable"
              hide-details="auto" dir="rtl" :error-messages="errors.fullName" @input="errors.fullName = ''"
              class="custom-input" prepend-inner-icon="mdi-account-outline" color="primary"></v-text-field>
          </v-sheet>

          <v-sheet class="field-container mb-4" elevation="0">
            <v-text-field v-model="email" placeholder="بريدك الإلكتروني" variant="outlined" density="comfortable"
              hide-details="auto" dir="rtl" :error-messages="errors.email" @input="errors.email = ''"
              :rules="emailRules" class="custom-input" prepend-inner-icon="mdi-email-outline" color="primary"></v-text-field>
          </v-sheet>

          <v-sheet class="field-container mb-4" elevation="0">
            <v-text-field v-model="password" placeholder="كلمة السر" :type="showPassword ? 'text' : 'password'"
              variant="outlined" density="comfortable" hide-details="auto" dir="rtl" :error-messages="errors.password"
              :rules="passwordRules" @input="errors.password = ''" class="custom-input"
              prepend-inner-icon="mdi-lock-outline" color="primary">
              <template v-slot:append-inner>
                <v-icon @click="showPassword = !showPassword" class="password-toggle">
                  {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-sheet class="field-container mb-5" elevation="0">
            <v-text-field v-model="confirmPassword" placeholder="إعادة كلمة السر"
              :type="showConfirmPassword ? 'text' : 'password'" variant="outlined" density="comfortable"
              hide-details="auto" dir="rtl" :error-messages="errors.confirmPassword"
              @input="errors.confirmPassword = ''" :rules="confirmPasswordRules" class="custom-input"
              prepend-inner-icon="mdi-lock-check-outline" color="primary">
              <template v-slot:append-inner>
                <v-icon @click="showConfirmPassword = !showConfirmPassword" class="password-toggle">
                  {{ showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <div class="password-strength-container mt-1 mb-5" v-if="password">
            <div class="strength-text">{{ getPasswordStrengthText() }}</div>
            <div class="strength-bar">
              <div class="strength-progress" :class="passwordStrengthClass" :style="{ width: passwordStrength + '%' }">
              </div>
            </div>
            <div class="password-tips">
              <div class="tip" :class="{ 'fulfilled': /[A-Z]/.test(password) }">
                <v-icon :color="/[A-Z]/.test(password) ? 'success' : 'grey'" size="small">
                  {{ /[A-Z]/.test(password) ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                </v-icon>
                <span>حرف كبير واحد على الأقل</span>
              </div>
              <div class="tip" :class="{ 'fulfilled': /[0-9]/.test(password) }">
                <v-icon :color="/[0-9]/.test(password) ? 'success' : 'grey'" size="small">
                  {{ /[0-9]/.test(password) ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                </v-icon>
                <span>رقم واحد على الأقل</span>
              </div>
              <div class="tip" :class="{ 'fulfilled': password.length >= 8 }">
                <v-icon :color="password.length >= 8 ? 'success' : 'grey'" size="small">
                  {{ password.length >= 8 ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                </v-icon>
                <span>الحد الأدنى 8 أحرف</span>
              </div>
            </div>
          </div>

          <div class="action-buttons d-flex flex-column flex-md-row justify-center gap-4 mt-6 mb-6">
            <v-btn class="signup-btn mb-3 mb-md-0" size="large" rounded="xl" elevation="4" @click.prevent="register">
              <v-icon start>mdi-account-plus</v-icon>
              تسجيل الحساب
            </v-btn>

            <v-btn class="login-btn" size="large" rounded="xl" elevation="4" @click.prevent="$emit('openLogin')">
              <v-icon start>mdi-login</v-icon>
              عندي حساب
            </v-btn>
          </div>
        </v-form>

        <div class="text-center mt-6">
          <v-btn rounded="xl" class="back-btn" @click.prevent="$emit('closeSignUp')">
            <v-icon start>mdi-arrow-left</v-icon>
            العودة
          </v-btn>
        </div>
      </v-card>
    </v-container>
  </div>

  <v-snackbar v-model="showSnackBar" :color="snackbarColor" location="top" timeout="3000" rounded="xl">
    <div class="d-flex align-center">
      <v-icon class="mr-2" color="white">
        {{ snackbarIcon }}
      </v-icon>
      {{ text }}
    </div>
    <template v-slot:actions>
      <v-btn icon @click="showSnackBar = false">
        <v-icon color="white">mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
import userService from '@/Services/userService';
import logo from "../assets/logo.png";

export default {
  name: "SignUp",
  data() {
    return {
      logo: logo,
      fullName: '',
      email: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
      showSnackBar: false,
      snackbarColor: 'error',
      snackbarIcon: 'mdi-alert-circle',
      text: "",
      errors: {
        fullName: '',
        email: '',
        password: '',
        confirmPassword: ''
      },
      passwordRules: [
        v => !!v || 'كلمة المرور مطلوبة',
        v => /[A-Z]/.test(v) || 'يجب أن تحتوي على حرف كبير واحد على الأقل',
        v => /[0-9]/.test(v) || 'يجب أن تحتوي على رقم واحد على الأقل',
        v => (v && v.length >= 8) || 'الحد الأدنى 8 أحرف'
      ],
      emailRules: [
        v => !!v || 'البريد الإلكتروني مطلوب',
        v => /.+@.+\..+/.test(v) || "يجب أن يكون البريد الإلكتروني صالحًا",
      ],
      confirmPasswordRules: [
        v => !!v || 'تأكيد كلمة المرور مطلوب',
        v => (v && v === this.password) || 'كلمات المرور غير متطابقة'
      ]
    }
  },
  computed: {
    passwordStrength() {
      let strength = 0;
      if (!this.password) return 0;

      // Increase strength for length
      if (this.password.length >= 8) strength += 25;
      if (this.password.length >= 12) strength += 15;

      // Check for uppercase letters
      if (/[A-Z]/.test(this.password)) strength += 20;

      // Check for numbers
      if (/[0-9]/.test(this.password)) strength += 20;

      // Check for special characters
      if (/[^A-Za-z0-9]/.test(this.password)) strength += 20;

      return Math.min(strength, 100);
    },
    passwordStrengthClass() {
      if (this.passwordStrength < 40) return 'strength-weak';
      if (this.passwordStrength < 70) return 'strength-medium';
      return 'strength-strong';
    }
  },
  methods: {
    getPasswordStrengthText() {
      if (this.passwordStrength < 40) return 'ضعيف';
      if (this.passwordStrength < 70) return 'متوسط';
      return 'قوي';
    },
    async register() {
      // Validate form first
      let isValid = true;
      
      if (!this.fullName) {
        this.errors.fullName = 'الاسم مطلوب';
        isValid = false;
      }
      if (!this.email) {
        this.errors.email = 'البريد الإلكتروني مطلوب';
        isValid = false;
      } else if (!/.+@.+\..+/.test(this.email)) {
        this.errors.email = 'يجب أن يكون البريد الإلكتروني صالحًا';
        isValid = false;
      }
      if (!this.password) {
        this.errors.password = 'كلمة المرور مطلوبة';
        isValid = false;
      } else if (!(/[A-Z]/.test(this.password) && /[0-9]/.test(this.password) && this.password.length >= 8)) {
        this.errors.password = 'كلمة المرور لا تستوفي المتطلبات';
        isValid = false;
      }
      if (!this.confirmPassword) {
        this.errors.confirmPassword = 'تأكيد كلمة المرور مطلوب';
        isValid = false;
      } else if (this.confirmPassword !== this.password) {
        this.errors.confirmPassword = 'كلمات المرور غير متطابقة';
        isValid = false;
      }

      if (isValid) {
        try {
          // Afficher un snackbar de chargement
          this.snackbarColor = 'info';
          this.snackbarIcon = 'mdi-loading mdi-spin';
          this.text = "جاري إنشاء الحساب...";
          this.showSnackBar = true;

          await userService.signUp(
            this.fullName,
            this.email,
            this.password,
            this.confirmPassword
          );

          // Afficher un snackbar de succès
          this.snackbarColor = 'success';
          this.snackbarIcon = 'mdi-check-circle';
          this.text = "تم إنشاء الحساب بنجاح";
          this.showSnackBar = true;

          // Rediriger après un court délai
          setTimeout(() => {
            this.$router.push('/AvantTest');
          }, 2000);
        } catch (err) {
          this.snackbarColor = 'error';
          this.snackbarIcon = 'mdi-alert-circle';
          this.showSnackBar = true;
          this.text = err.response?.data?.message || "حدث خطأ أثناء إنشاء الحساب";
          console.log(this.text);
        }
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Tajawal:wght@400;500;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap');

.signup-wrapper {
  max-height: 100vh;
  overflow-y: auto;
  padding: 20px 0;
  scroll-behavior: smooth;
  /* Cacher la barre de défilement pour Firefox */
  scrollbar-width: none;
}

/* Cacher la barre de défilement pour Chrome, Safari, etc. */
.signup-wrapper::-webkit-scrollbar {
  width: 0;
  height: 0;
  display: none;
}

.login-card {
  width: 100%;
  max-width: 520px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: visible;
  position: relative;
  z-index: 10;
  transition: all 0.3s ease;
  /* Ajout de marge pour éviter l'effet collé */
  margin: 10px auto 30px;
}

.logo-image {
  max-width: 200px;
  height: 100px;
  margin-bottom: 0.5rem;
  transition: transform 0.3s ease;
}

.logo-image:hover {
  transform: scale(1.05);
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.logo-container {
  margin-bottom: 1rem;
}

.logo-text {
  font-family: 'Poppins', sans-serif;
  font-weight: 700;
  font-size: 2.5rem;
  background: linear-gradient(45deg, #2FE390, #2D80D5);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);
}

.tagline {
  font-size: 1rem;
  color: #555;
  margin-top: -8px;
}

.headline-text {
  font-weight: 700;
  color: #333;
  font-size: 1.7rem;
}

.form-container {
  max-width: 420px;
  margin: auto;
}

.field-container {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease-in-out;
  border: 1px solid rgba(0, 0, 0, 0.1);
}

.field-container:hover {
  border-color: #2D80D5;
  box-shadow: 0px 4px 12px rgba(45, 128, 213, 0.15);
}

.custom-input :deep(input) {
  text-align: right;
  font-family: 'Tajawal', sans-serif;
  font-size: 1rem;
}

.custom-input :deep(.v-field__outline) {
  opacity: 0.7;
}

.custom-input :deep(.v-field--focused .v-field__outline) {
  opacity: 1;
}

.password-toggle {
  cursor: pointer;
  opacity: 0.7;
  transition: all 0.3s ease;
}

.password-toggle:hover {
  opacity: 1;
  transform: scale(1.1);
}

.password-strength-container {
  direction: rtl;
  padding: 0 12px;
}

.strength-text {
  text-align: right;
  margin-bottom: 5px;
  font-size: 0.9rem;
  font-weight: 500;
}

.strength-bar {
  height: 6px;
  background-color: #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 10px;
}

.strength-progress {
  height: 100%;
  border-radius: 10px;
  transition: width 0.3s ease;
}

.strength-weak {
  background-color: #f44336;
}

.strength-medium {
  background-color: #ff9800;
}

.strength-strong {
  background-color: #4caf50;
}

.password-tips {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 0.8rem;
  color: #666;
}

.tip {
  display: flex;
  align-items: center;
  gap: 5px;
}

.tip.fulfilled {
  color: #4caf50;
}

.action-buttons {
  margin-top: 30px;
}

.login-btn,
.signup-btn {
  min-width: 160px;
  padding: 12px 24px;
  font-weight: 700;
  font-size: 1rem;
  letter-spacing: 0.5px;
  text-transform: none;
  transition: all 0.3s ease;
}

.login-btn {
  background: linear-gradient(45deg, #2D80D5, #1a5ea3);
  color: white;
}

.login-btn:hover {
  background: linear-gradient(45deg, #1a5ea3, #0d4580);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(45, 128, 213, 0.4);
}

.signup-btn {
  background: linear-gradient(45deg, #2FE390, #1fae6e);
  color: white;
}

.signup-btn:hover {
  background: linear-gradient(45deg, #1fae6e, #17824e);
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(47, 227, 144, 0.4);
}

.back-btn {
  background-color: rgba(1, 243, 243, 0.9);
  color: #333;
  font-weight: 500;
  padding: 8px 24px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background-color: rgba(1, 243, 243, 1);
  transform: translateX(-5px);
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .signup-wrapper {
    padding: 10px 0;
  }
  
  .login-card {
    margin: 5px auto 20px;
    padding: 16px !important;
  }

  .logo-text {
    font-size: 2rem;
  }

  .headline-text {
    font-size: 1.5rem;
  }

  .login-btn,
  .signup-btn {
    width: 100%;
  }

  .password-tips {
    font-size: 0.75rem;
  }
}
</style>