<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh; font-family: 'Segoe UI';">
        <v-card class="mx-auto pa-6 card" elevation="3">
            <div class="text-center mb-4">
                <v-btn class="close" @click.prevent="$emit('closeSignUp')">
                    العودة
                </v-btn>
            </div>

            <v-form class="form-container">
                <v-container>
                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="fullName" placeholder="اسمك الكامل" variant="outlined"
                            density="comfortable" hide-details dir="rtl" class="custom-input">
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-account</v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="email" placeholder="بريدك الإلكتروني" variant="outlined"
                            density="comfortable" hide-details dir="rtl" class="custom-input">
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-email</v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="password" placeholder="كلمة السر" :type="showPassword ? 'text' : 'password'"
                            variant="outlined" density="comfortable" hide-details dir="rtl" class="custom-input">
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

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="confirmPassword" placeholder="إعادة كلمة السر"
                            :type="showConfirmPassword ? 'text' : 'password'" variant="outlined" density="comfortable"
                            hide-details dir="rtl" class="custom-input">
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-lock</v-icon>
                            </template>
                            <template v-slot:prepend-inner>
                                <v-icon @click="showConfirmPassword = !showConfirmPassword">
                                    {{ showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                                </v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>
                </v-container>

                <div class="d-flex justify-center">
                    <v-btn class="me-2 buttonStyle" color="#2FE390" size="large" rounded="lg" @click.prevent="$emit('openLogin')">
                        عندي حساب
                    </v-btn>
                    <v-btn class="me-2 buttonStyle" color="#2D80D5" size="large" rounded="lg" @click.prevent="register">
                        تسجيل الحساب
                    </v-btn>
                </div>
            </v-form>
        </v-card>
    </v-container>
</template>

<script>
import axios from 'axios';
export default {
    name: "SignUp",
    data() {
        return {
            fullName: '',
            email: '',
            password: '',
            confirmPassword: '',
            showPassword: false,
            showConfirmPassword: false
        }
    },
    methods: {
        async register() {
            console.log('Inscription avec:', {
                fullName: this.fullName,
                email: this.email,
                password: this.password
            })
            try{
                await axios.post("Utilisateur/register",{
                    nom: this.fullName,
                    email: this.email,
                    password: this.password,
                    confirmPassword: this.confirmPassword
                });
                this.$router.push('/AvantTest');
            }catch(err){
                console.log(err.response?.data?.message || "Message d'erreur");
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
