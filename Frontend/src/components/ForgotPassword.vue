<template>
  <div >
    <v-card class="forgot-password-card" elevation="3" rounded="xl">
      <!-- Header plus compact -->
      <div class="card-header-wrapper">
        <div class="card-header">
          <h2 class="text-center primary--text mb-2">استعادة كلمة المرور</h2>
          <v-btn
            class="back-button"
            @click.prevent="$emit('closeForgotPassword')"
            variant="text"
            color="primary"
            prepend-icon="mdi-arrow-right"
            size="small"
            rounded="xl"
          >
            العودة
          </v-btn>
        </div>
      </div>

      <div class="content-wrapper">
        <!-- Image plus petite -->
        <div class="card-image text-center">
          <v-avatar size="70" color="primary" class="password-avatar mb-3">
            <v-icon size="36" color="white">mdi-lock-reset</v-icon>
          </v-avatar>
        </div>

        <v-divider class="mb-4"></v-divider>

        <!-- Formulaire avec espacements réduits -->
        <v-form class="forgot-form" @submit.prevent="sendResetLink">
          <p class="text-body-2 text-right mb-3 text-medium-emphasis">
            يرجى إدخال بريدك الإلكتروني لإرسال رابط إعادة تعيين كلمة المرور
          </p>

          <v-text-field
            v-model="email"
            placeholder="بريدك الإلكتروني"
            variant="outlined"
            color="primary"
            bg-color="surface"
            density="compact"
            dir="rtl"
            class="email-field mb-3"
            :rules="emailRules"
            type="email"
            required
            hide-details="auto"
          >
            <template v-slot:append-inner>
              <v-icon size="small" color="primary">mdi-email-outline</v-icon>
            </template>
          </v-text-field>

          <v-expand-transition>
            <v-alert
              v-if="message"
              :type="alertType"
              variant="tonal"
              dir="rtl"
              border="start"
              closable
              density="compact"
              class="my-3 alert-message"
            >
              {{ arabicMessage }}
            </v-alert>
          </v-expand-transition>

          <v-btn
            class="submit-button mt-4"
            color="primary"
            size="small"
            block
            rounded="lg"
            type="submit"
            :loading="loading"
            :disabled="!isEmailValid"
            elevation="1"
          >
            <v-icon start size="small">mdi-send</v-icon>
            إرسال رابط إعادة التعيين
          </v-btn>
        </v-form>
      </div>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ForgotPassword",
  data() {
    return {
      email: "",
      message: "",
      loading: false,
      emailRules: [
        (v) => !!v || "البريد الإلكتروني مطلوب",
        (v) => /.+@.+\..+/.test(v) || "يجب أن يكون البريد الإلكتروني صالحًا",
      ],
    };
  },
  computed: {
    alertType() {
      return this.message.includes("introuvable") ? "error" : "success";
    },
    arabicMessage() {
      if (this.message.includes("Lien de réinitialisation")) {
        return "تم إرسال رابط إعادة تعيين كلمة المرور إلى بريدك الإلكتروني بنجاح";
      } else if (this.message.includes("introuvable")) {
        return "البريد الإلكتروني غير مسجل في نظامنا";
      }
      return this.message;
    },
    isEmailValid() {
      return this.email && /.+@.+\..+/.test(this.email);
    }
  },
  methods: {
    async sendResetLink() {
      if (!this.email || !this.isEmailValid) return;

      this.loading = true;
      this.message = "";

      try {
        const res = await axios.post(
          "auth/forgot-password",
          { email: this.email }
        );
        this.message =
          res.data || "تم إرسال رابط إعادة تعيين كلمة المرور إلى بريدك الإلكتروني بنجاح";
      } catch (err) {
        this.message = err.response?.data?.message || "البريد الإلكتروني غير مسجل في نظامنا";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>

.forgot-password-card {
  width: 400px;
  max-width: 90%;
  background: white;
  overflow: hidden;
  position: relative;
  transition: transform 0.3s, box-shadow 0.3s;
  border-radius: 16px;
  border: 1px solid rgba(230, 235, 245, 0.8);
  margin: 20px auto;
}

.forgot-password-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.07), 0 6px 12px rgba(0, 0, 0, 0.04);
}

.card-header-wrapper {
  background: linear-gradient(120deg, #f8faff 0%, #f0f4fa 100%);
  border-bottom: 1px solid rgba(230, 240, 255, 0.5);
  padding: 16px 24px 12px;
  margin-bottom: 16px;
}

.card-header {
  position: relative;
}

.back-button {
  font-weight: 500;
  letter-spacing: 0.5px;
  text-transform: none;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  position: absolute;
  top: -2px;
  right: 0;
}

.back-button:hover {
  transform: translateX(-3px);
  opacity: 0.9;
}

.content-wrapper {
  padding: 0 24px 24px;
}

.password-avatar {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.password-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.forgot-form {
  padding: 0;
}

.email-field {
  border-radius: 12px;
  transition: all 0.3s ease;
}

.email-field:deep(.v-field) {
  border-radius: 12px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.25s ease;
}

.email-field:deep(.v-field--focused) {
  transform: scale(1.01);
  border-color: var(--v-primary-base);
  box-shadow: 0 0 0 2px rgba(var(--v-primary-base), 0.2);
}

.email-field:deep(.v-field__input) {
  font-size: 14px;
  padding: 10px 12px;
}

.submit-button {
  font-weight: 600;
  letter-spacing: 0.5px;
  text-transform: none;
  height: 40px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border-radius: 12px;
}

.submit-button::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.6s ease;
}

.submit-button:hover::after {
  left: 100%;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 14px rgba(45, 128, 213, 0.25);
}

.alert-message {
  border-radius: 8px;
  animation: fadeIn 0.3s ease-in-out;
  font-size: 13px;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-8px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 600px) {
  .forgot-password-card {
    width: 92%;
    max-width: 92%;
    margin: 16px auto;
    border-radius: 16px;
  }
  
  .forgot-password-wrapper {
    padding: 0;
  }
  
  .card-header-wrapper {
    padding: 16px 20px 12px;
  }
  
  .content-wrapper {
    padding: 0 16px 20px;
  }
  
  .submit-button {
    height: 38px;
  }
}
</style>