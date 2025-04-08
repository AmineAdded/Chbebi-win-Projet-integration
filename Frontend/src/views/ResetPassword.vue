<template>
  <v-container class="d-flex justify-center align-center" style="min-height: 100vh;" dir="rtl">
    <v-card class="pa-6" width="100%" max-width="500" elevation="3">
      <v-card-title class="text-center text-h4 mb-6" style="font-family: 'Segoe UI';">
        إعادة تعيين كلمة المرور
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="resetPassword">
          <!-- Champ Token -->
          <!-- <v-text-field
            v-model="tokenInput"
            label="رمز التحقق (Token)"
            placeholder="أدخل الرمز المرسل عبر البريد الإلكتروني"
            prepend-inner-icon="mdi-key"
            variant="outlined"
            :rules="[v => !!v || 'الرمز مطلوب']"
            required
            class="mb-4"
          ></v-text-field> -->

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
          <div class="code-input-section mt-4">
            <div class="text-subtitle-1 mb-2">Code de vérification</div>
            <v-otp-input
              v-model="tokenInput"
              type="number"
              length="6"
              :rules="[v => !!v || 'الرمز مطلوب']"
              required
              class="otp-input"
            ></v-otp-input>
          </div>

          <!-- Bouton de soumission -->
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
        <div class="text-center mb-4">
                <v-btn class="close" to="/">
                  العودة إلى الصفحة الرئيسية
                </v-btn>
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
      tokenInput: "",
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
  mounted() {
    // Extraire automatiquement le token de l'URL si présent
    const tokenParam = this.$route.query.token;
    if (tokenParam) {
      this.tokenInput = tokenParam;
    }
  },
  methods: {
    async resetPassword() {
      if (!this.tokenInput || !this.passwordsMatch) {
        this.message = "يرجى التحقق من صحة المعلومات المدخلة";
        return;
      }

      this.loading = true;
      this.message = "";

      try {
        const res = await axios.post(
          "http://localhost:9090/api/auth/reset-password",
          {
            token: this.tokenInput,
            password: this.password,
            confirmPassword: this.confirmPassword
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
.close {
    background-color: #01F3F3;
    color: aliceblue;
    font-weight: bold;
    font-size: 1rem;
    border-radius: 30px;
    width: 55%;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    margin-top: 5%;
}
</style>