<template>
  <v-container
    class="d-flex justify-center align-center cont"
  >
    <v-card class="mx-auto pa-6 card" elevation="3">
      <div class="text-center mb-4">
        <v-btn class="close" @click.prevent="$emit('closeForgotPassword')"> العودة </v-btn>
      </div>

      <v-form class="form-container" @submit.prevent="sendResetLink">
        <v-container>
          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="email"
              placeholder="بريدك الإلكتروني"
              variant="outlined"
              density="comfortable"
              hide-details
              dir="rtl"
              class="custom-input"
              :rules="emailRules"
              type="email"
              required
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-email</v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-alert
            v-if="message"
            class="mt-4"
            :type="alertType"
            variant="tonal"
            dir="rtl"
          >
            {{ arabicMessage }}
          </v-alert>
        </v-container>

        <div class="d-flex justify-center">
          <v-btn
            class="me-2 buttonStyle"
            color="#2D80D5"
            size="large"
            rounded="lg"
            type="submit"
            :loading="loading"
          >
            إرسال رابط إعادة التعيين
          </v-btn>
        </div>
      </v-form>
    </v-card>
  </v-container>
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
  },
  methods: {
    async sendResetLink() {
      if (!this.email) return;

      this.loading = true;
      this.message = "";

      try {
        const res = await axios.post(
          "auth/forgot-password",
          { email: this.email }
        );
        this.message =
          res.data || "Lien de réinitialisation envoyé avec succès";
      } catch (err) {
        this.message = err.response?.data?.message || "Email introuvable";
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.cont {
    min-height: 100vh; 
    font-family: 'Segoe UI';
}
.card {
  max-width: 500px;
  border-radius: 30px;
}

.close {
  background-color: #01f3f3;
  color: aliceblue;
  font-weight: bold;
  font-size: 1.3rem;
  border-radius: 30px;
  width: 35%;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.form-container {
  /* max-width: 420px; */
  width: 400px;
  margin: auto;
}

.field-container {
  border-radius: 10px;
  padding: 5px;
  margin-bottom: 15px;
  transition: all 0.3s ease-in-out;
}

.field-container:hover {
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
}

.custom-input :deep(input) {
  text-align: right;
}

.buttonStyle {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  transition: box-shadow 0.3s ease;
  color: aliceblue;
  font-weight: bold;
  font-size: 1.1rem;
}

.buttonStyle:hover {
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}
</style>