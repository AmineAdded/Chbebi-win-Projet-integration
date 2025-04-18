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
                            density="comfortable" hide-details="auto" dir="rtl" :error-messages="errors.fullName"
                            @input="errors.fullName = ''" class="custom-input">
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-account</v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="email" placeholder="بريدك الإلكتروني" variant="outlined"
                            density="comfortable" hide-details="auto" dir="rtl" :error-messages="errors.email"
                            @input="errors.email = ''" 
                            :rules="emailRules"
                            class="custom-input">
                            <template v-slot:append-inner>
                                <v-icon color="primary">mdi-email</v-icon>
                            </template>
                        </v-text-field>
                    </v-sheet>

                    <v-sheet class="field-container" elevation="1">
                        <v-text-field v-model="password" placeholder="كلمة السر"
                            :type="showPassword ? 'text' : 'password'" variant="outlined" density="comfortable"
                            hide-details="auto" dir="rtl" :error-messages="errors.password" :rules=passwordRules
                            @input="errors.password = ''" class="custom-input">
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
                            hide-details="auto" dir="rtl" :error-messages="errors.confirmPassword"
                            @input="errors.confirmPassword = ''" 
                            :rules="confirmPasswordRules"
                            class="custom-input">
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
                    <v-btn class="me-2 buttonStyle" color="#2FE390" size="large" rounded="lg"
                        @click.prevent="$emit('openLogin')">
                        عندي حساب
                    </v-btn>
                    <v-btn class="me-2 buttonStyle" color="#2D80D5" size="large" rounded="lg" @click.prevent="register">
                        تسجيل الحساب
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
import userService from '@/Services/userService';
export default {
    name: "SignUp",
    data() {
        return {
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
                v => /[A-Z]/.test(v) || 'يجب أن تحتوي على حرف كبير واحد على الأقل',
                v => /[0-9]/.test(v) || 'يجب أن تحتوي على رقم واحد على الأقل',
                v => (v && v.length >= 8) || 'الحد الأدنى 8 أحرف'
            ],
            emailRules: [     
                (v) => /.+@.+\..+/.test(v) || "يجب أن يكون البريد الإلكتروني صالحًا",
            ],
            confirmPasswordRules: [
                v => (v && v === this.password) || 'كلمات المرور غير متطابقة'
            ]
        }
    },
    methods: {
        async register() {
            if (this.fullName && this.email && this.password && this.confirmPassword) {

                try {
                    await userService.signUp(
                        this.fullName,
                        this.email,
                        this.password,
                        this.confirmPassword
                    );
                    this.$router.push('/AvantTest');
                } catch (err) {
                    this.showSnackBar = true;
                    this.text = err.response.data.message;
                    console.log(this.text);
                }
            }
            else {
                if (!this.fullName) {
                    this.errors.fullName = 'الاسم مطلوب';
                }
                if (!this.email) {
                    this.errors.email = 'البريد الإلكتروني مطلوب';
                }
                if (!this.password) {
                    this.errors.password = 'كلمة المرور مطلوبة';
                }
                if (!this.confirmPassword) {
                    this.errors.confirmPassword = 'تأكيد كلمة المرور مطلوب';
                }
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
