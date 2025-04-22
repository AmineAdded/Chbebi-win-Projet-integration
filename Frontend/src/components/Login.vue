<template>
  <v-container>
    <div class="glassmorphism-background"></div>

    <v-card class="login-card mx-auto pa-8" elevation="10">
      <div class="logo-container text-center mb-6">
        <img :src="logo" alt="ChbebiWin Logo" class="logo-image" />
        <div class="tagline">استمتع بالتحدي. اربح المزيد.</div>
      </div>

      <v-form class="form-container">
        <h2 class="text-center mb-6 headline-text">تسجيل الدخول</h2>

        <v-slide-y-transition>
          <v-sheet class="field-container mb-5" elevation="0">
            <v-text-field v-model="email" placeholder="بريدك الإلكتروني" variant="outlined"
              :error-messages="errors.email" @input="errors.email = ''" density="comfortable" hide-details="auto"
              dir="rtl" class="custom-input" prepend-inner-icon="mdi-email-outline" color="primary"></v-text-field>
          </v-sheet>
        </v-slide-y-transition>

        <v-slide-y-transition>
          <v-sheet class="field-container" elevation="0">
            <v-text-field v-model="password" placeholder="كلمة السر" :type="showPassword ? 'text' : 'password'"
              variant="outlined" density="comfortable" :error-messages="errors.password" @input="errors.password = ''"
              hide-details="auto" dir="rtl" class="custom-input" prepend-inner-icon="mdi-lock-outline" color="primary">
              <template v-slot:append-inner>
                <v-icon @click="showPassword = !showPassword" class="password-toggle">
                  {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>
        </v-slide-y-transition>

        <div class="text-right mb-6">
          <v-btn variant="text" class="forgot-password" @click.prevent="$emit('openForgotPassword')">
            نسيت كلمة السر؟
          </v-btn>
        </div>

        <div class="d-flex flex-column flex-md-row justify-center gap-4">
          <v-btn class="login-btn" size="large" rounded="xl" elevation="4" @click.prevent="login">
            <v-icon start>mdi-login</v-icon>
            تسجيل الدخول
          </v-btn>

          <v-btn class="signup-btn" size="large" rounded="xl" elevation="4" @click.prevent="$emit('openSignUp')">
            <v-icon start>mdi-account-plus</v-icon>
            إنشاء حساب جديد
          </v-btn>
        </div>
      </v-form>

      <div class="text-center mt-8">
        <v-btn rounded="xl" class="back-btn" @click.prevent="$emit('closeLogin')">
          <v-icon start>mdi-arrow-left</v-icon>
          العودة
        </v-btn>
      </div>
    </v-card>
  </v-container>

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
import { useUserStore } from '../store/User/userStore.js';
import userService from '../Services/userService.js';
import logo from "../assets/logo.png";

export default {
  name: "Login",
  data() {
    return {
      logo: logo,
      email: '',
      password: '',
      showPassword: false,
      showSnackBar: false,
      snackbarColor: 'error',
      snackbarIcon: 'mdi-alert-circle',
      text: "",
      errors: {
        email: "",
        password: ""
      }
    }
  },
  methods: {
    async login() {
      if (this.email != "" && this.password != "") {
        try {
          const store = useUserStore();
          await userService.login(this.email, this.password);
          if (store.role == 0) {
            this.$router.push('/privatehome');
          } else if (store.role == 1 ) {
            this.$router.push('/admin');
          }else if (store.role == 2) {
            this.$router.push('/admin');
          }
        }
        catch (err) {
          this.snackbarColor = 'error';
          this.snackbarIcon = 'mdi-alert-circle';
          this.showSnackBar = true;
          this.text = err.response?.data?.message || "حدث خطأ أثناء تسجيل الدخول";
        }
      }
      else {
        if (this.email == "") {
          this.errors.email = "البريد الإلكتروني مطلوب";
        }
        if (this.password == "") {
          this.errors.password = "كلمة السر مطلوبة";
        }
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Tajawal:wght@400;500;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap');


.login-card {
  width: 100%;
  max-width: 520px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  position: relative;
  z-index: 10;
  transition: all 0.3s ease;
}
.logo-image {
  max-width: 200px; /* Ajustez selon la taille souhaitée */
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
  margin-bottom: 2rem;
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

.forgot-password {
  font-size: 0.9rem;
  color: #2D80D5;
  text-decoration: none;
  transition: all 0.2s ease;
}

.forgot-password:hover {
  color: #1b5ea1;
  text-decoration: underline;
}

.login-btn,
.signup-btn {
  min-width: 160px;
  padding: 12px 24px;
  margin-left: 10px;
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
  .login-card {
    margin: 16px;
    padding: 24px 16px !important;
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
    margin-bottom: 12px;
  }
}

/* Animation légère pour les champs */
.v-slide-y-transition-enter-active {
  transition-delay: calc(0.1s * v-bind('index'));
}
</style>