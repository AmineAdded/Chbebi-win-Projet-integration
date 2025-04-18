<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh; font-family: 'Segoe UI';">
        <v-card class="mx-auto pa-6 card" elevation="3">
            <div class="text-center mb-4">
                <v-btn class="close" @click.prevent="$emit('closeLogin')">
                    العودة
                </v-btn>
            </div>

            <v-form class="form-container">
                <v-container>
                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="email" placeholder="بريدك الإلكتروني" variant="outlined"
                            :error-messages="errors.email" 
                            @input="errors.email = ''"
                            density="comfortable" hide-details="auto" dir="rtl" class="custom-input">
                            
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-email</v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="password" placeholder="كلمة السر"
                            :type="showPassword ? 'text' : 'password'" variant="outlined" density="comfortable"
                            :error-messages="errors.password"
                            @input="errors.password = ''"
                            hide-details="auto" dir="rtl" class="custom-input">
                            
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-lock</v-icon>
                            </template>
                            <template v-slot:prepend-inner>
                                <v-icon @click="showPassword = !showPassword">
                                    {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                                </v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>
                    <v-btn class="nav-btn" style="position: relative; left: 30%;" @click.prevent="$emit('openForgotPassword')">نسيت كلمة السر؟</v-btn>
                </v-container>

                <div class="d-flex justify-center">
                    <v-btn class="me-2 buttonStyle" color="#2FE390" size="large" rounded="lg" @click.prevent="$emit('openSignUp')">
                        إنشاء حساب جديد
                    </v-btn>
                    <v-btn class="me-2 buttonStyle" color="#2D80D5" size="large" rounded="lg" @click.prevent="login">
                        تسجيل الدخول
                    </v-btn>
                </div>
            </v-form>
        </v-card>
    </v-container>

    <v-snackbar v-model="showSnackBar" :color="snackbarColor" top timeout="3000">
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
import userService from '../Services/userService.js';
export default {
    name: "Login",
    data() {
        return {
            email: '',
            password: '',
            showPassword: false,
            showSnackBar: false,
            snackbarColor: 'error',
            snackbarIcon: 'mdi-alert-circle',
            text:"",
            errors:{
                email:"",
                password:""
            }
        }
    },
    methods: {
        async login(){
            if(this.email!="" && this.password!=""){
                try{
                    await userService.login(this.email,this.password);
                    this.$router.push('/privatehome');
                }
                catch(err){
                    this.showSnackBar = true;
                    this.text = err.response.data.message || "حدث خطأ أثناء تسجيل الدخول";
                }
            }
            else{
                this.errors.email =  "البريد الإلكتروني مطلوب";
                this.errors.password ="كلمة السر مطلوبة";
            }
        }
    }
}
</script>

<style scoped>
.card {
    max-width: 500px;
    border-radius: 30px;
}

.close {
    background-color: #01F3F3;
    color: aliceblue;
    font-weight: bold;
    font-size: 1.3rem;
    border-radius: 30px;
    width: 35%;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.form-container {
    max-width: 420px;
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
