<template>
  <v-container class="d-flex justify-center align-center" style="min-height: 100vh;" dir="rtl">
    <v-card class="pa-6" width="100%" max-width="500" elevation="3">
      <!-- Titre en arabe -->
      <v-card-title class="text-center text-h4 mb-6" style="font-family: 'Segoe UI';">
        إعادة تعيين كلمة المرور
      </v-card-title>

      <!-- Formulaire -->
      <v-card-text>
        <v-form @submit.prevent="resetPassword">
          <!-- Champ Nouveau mot de passe -->
          <v-text-field
            v-model="password"
            label="كلمة المرور الجديدة"
            placeholder="أدخل كلمة المرور الجديدة"
            prepend-inner-icon="mdi-lock"
            :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            :type="showPassword ? 'text' : 'password'"
            variant="outlined"
            :rules="passwordRules"
            required
            @click:append-inner="showPassword = !showPassword"
            class="mb-4"
          ></v-text-field>

          <!-- Champ Confirmation mot de passe -->
          <v-text-field
            v-model="confirmPassword"
            label="تأكيد كلمة المرور"
            placeholder="أعد إدخال كلمة المرور الجديدة"
            prepend-inner-icon="mdi-lock-check"
            :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
            :type="showConfirmPassword ? 'text' : 'password'"
            variant="outlined"
            :rules="confirmPasswordRules"
            required
            @click:append-inner="showConfirmPassword = !showConfirmPassword"
          ></v-text-field>

          <!-- Bouton de réinitialisation -->
          <v-btn
            type="submit"
            color="primary"
            size="large"
            block
            class="mt-4"
            :loading="loading"
            :disabled="!passwordsMatch"
          >
            إعادة تعيين كلمة المرور
          </v-btn>
        </v-form>

        <!-- Message de retour -->
        <v-alert
          v-if="message"
          class="mt-6"
          :type="alertType"
          variant="tonal"
        >
          {{ message }}
        </v-alert>

        <!-- Lien de retour -->
        <div class="text-center mt-4">
          <router-link to="/login" class="text-primary">
            العودة إلى الصفحة الرئيسية
          </router-link>
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      password: "",
      confirmPassword: "",
      showPassword: false,
      showConfirmPassword: false,
      message: "",
      loading: false,
      passwordRules: [
        v => !!v || 'كلمة المرور مطلوبة',
        v => (v && v.length >= 8) || 'الحد الأدنى 8 أحرف',
        v => /[A-Z]/.test(v) || 'يجب أن تحتوي على حرف كبير واحد على الأقل',
        v => /[0-9]/.test(v) || 'يجب أن تحتوي على رقم واحد على الأقل'
      ]
    };
  },
  computed: {
    alertType() {
      return this.message.includes("خطأ") ? "error" : "success";
    },
    token() {
      return this.$route.params.token;
    },
    confirmPasswordRules() {
      return [
        v => !!v || 'يجب تأكيد كلمة المرور',
        v => v === this.password || 'كلمات المرور غير متطابقة'
      ];
    },
    passwordsMatch() {
      return this.password && this.confirmPassword && this.password === this.confirmPassword;
    }
  },
  methods: {
    async resetPassword() {
      if (!this.password || !this.passwordsMatch) {
        this.message = "يجب أن تتطابق كلمتا المرور";
        return;
      }
      
      this.loading = true;
      this.message = "";

      try {
        const res = await axios.post(
          "http://localhost:9090/api/auth/reset-password",
          { 
            token: this.token,
            password: this.password
          }
        );
        this.message = res.data?.message || "تم إعادة تعيين كلمة المرور بنجاح";
        this.password = "";
        this.confirmPassword = "";
      } catch (err) {
        this.message = err.response?.data?.message || "خطأ أثناء إعادة تعيين كلمة المرور";
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.v-card {
  transition: transform 0.3s ease;
}

.v-card:hover {
  transform: translateY(-5px);
}

a {
  text-decoration: none;
  transition: color 0.3s ease;
}

a:hover {
  color: #1976D2 !important;
}

/* Pour le support RTL */
[dir="rtl"] .v-text-field .v-input__prepend-inner {
  margin-right: 0;
  margin-left: 8px;
}

[dir="rtl"] .v-text-field .v-input__append-inner {
  margin-left: 0;
  margin-right: 8px;
}

/* Espacement entre les champs */
.v-text-field {
  margin-bottom: 16px;
}
</style>