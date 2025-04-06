<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh; direction: rtl;">
      <v-card class="pa-6" width="100%" max-width="500" elevation="3">
        <!-- Titre -->
        <v-card-title class="text-center text-h4 mb-6" style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">
          نسيت كلمة المرور
        </v-card-title>
  
        <!-- Formulaire -->
        <v-card-text>
          <v-form @submit.prevent="sendResetLink">
            <!-- Champ Email -->
            <v-text-field
              v-model="email"
              label="البريد الإلكتروني"
              placeholder="أدخل بريدك الإلكتروني"
              prepend-inner-icon="mdi-email"
              type="email"
              variant="outlined"
              :rules="emailRules"
              required
              dir="rtl"
            ></v-text-field>
  
            <!-- Bouton d'envoi -->
            <v-btn
              text to="/reset-password/:token"
              type="submit"
              color="primary"
              size="large"
              block
              class="mt-4"
              :loading="loading"
            >
              إرسال رابط إعادة التعيين
            </v-btn>
          </v-form>
  
          <!-- Message de retour -->
          <v-alert
            v-if="message"
            class="mt-6"
            :type="alertType"
            variant="tonal"
            dir="rtl"
          >
            {{ arabicMessage }}
          </v-alert>
  
          <!-- Lien de retour -->
          <div class="text-center mt-4">
            <router-link to="/" class="text-primary" style="font-family: 'Segoe UI';">
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
        email: "",
        message: "",
        loading: false,
        emailRules: [
          v => !!v || 'البريد الإلكتروني مطلوب',
          v => /.+@.+\..+/.test(v) || 'يجب أن يكون البريد الإلكتروني صالحًا'
        ]
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
      }
    },
    methods: {
      async sendResetLink() {
        if (!this.email) return;
        
        this.loading = true;
        this.message = "";
  
        try {
          const res = await axios.post(
            "http://localhost:9090/api/auth/forgot-password",
            { email: this.email }
          );
          this.message = res.data || "Lien de réinitialisation envoyé avec succès";
        } catch (err) {
          this.message = err.response?.data?.message || "Email introuvable";
        } finally {
          this.loading = false;
        }
      }
    }
  };
  </script>
  
  <style scoped>
  /* Police adaptée pour l'arabe */
  .v-card-title, .v-text-field label, .v-btn, .v-alert, a {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }
  
  .v-card {
    transition: transform 0.3s ease;
    border-radius: 12px;
  }
  
  .v-card:hover {
    transform: translateY(-5px);
  }
  
  /* Style personnalisé pour les liens */
  a {
    text-decoration: none;
    transition: color 0.3s ease;
    font-weight: 500;
  }
  
  a:hover {
    color: #1976D2 !important;
  }
  
  /* Adaptation pour l'affichage RTL */
  .v-input__prepend-inner {
    margin-left: 8px;
    margin-right: 0 !important;
  }
  
  .v-text-field input {
    text-align: right;
  }
  </style>