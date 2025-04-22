<template>
    <div class="super-admin-verify">
      <v-card class="verify-card">
        <v-card-title class="text-center">
          <h2>التحقق من هوية المدير العام</h2>
          <p>يجب إدخال كلمة مرور حساب المدير العام</p>
        </v-card-title>
  
        <v-card-text>
          <v-form @submit.prevent="verifyPassword">
            <v-text-field
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              label="كلمة المرور"
              outlined
              required
              :append-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append="showPassword = !showPassword"
            ></v-text-field>
  
            <v-btn 
              type="submit" 
              color="primary" 
              block
              :loading="loading"
            >
              تحقق
            </v-btn>
  
            <v-alert v-if="error" type="error" class="mt-3">
              {{ error }}
            </v-alert>
          </v-form>
        </v-card-text>
      </v-card>
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
        this.loading = true;
        this.error = '';
  
        try {
          const email = localStorage.getItem('email');
          console.log(email);
  
          await userService.logSuperAdmin(email, this.password);
  
          this.$router.push('/super-admin');
  
        } catch (err) {
          this.error = err.response?.data?.message || "كلمة المرور غير صحيحة";
        } finally {
          this.loading = false;
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .super-admin-verify {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background: #f5f5f5;
  }
  
  .verify-card {
    width: 100%;
    max-width: 500px;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  }
  </style>