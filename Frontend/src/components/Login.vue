<template>
    <v-container class="d-flex justify-center align-center" style="min-height: 100vh;">
        <v-card class="mx-auto pa-4 card" elevation="3">
            <div class="text-center mb-4">
                <v-btn class="teal-lighten-2 white--text" rounded="pill" size="large"
                    style="background-color: #01F3F3;">
                    العودة
                </v-btn>
            </div>

            <v-form style="width: 420px;position: relative; left: 30px;">
                <v-card class="mb-4 widthField" variant="outlined" rounded="lg">
                    <v-text-field v-model="email" :placeholder="'بريدك الإلكتروني'" variant="plain"
                        density="comfortable" hide-details class="widthFild" dir="rtl">
                        <template v-slot:append-inner>
                            <v-avatar color="grey" size="32" class="icon">
                                <v-icon size="20" color="white">mdi-email</v-icon>
                            </v-avatar>
                        </template>
                    </v-text-field>
                </v-card>

                <v-card class="mb-4 widthField" variant="outlined" rounded="lg">
                    <v-text-field v-model="password" :placeholder="'كلمة السر'"
                        :type="showPassword ? 'text' : 'password'" variant="plain" density="comfortable" hide-details
                        class="widthFild" dir="rtl">
                        <template v-slot:append-inner>
                            <v-avatar color="grey" size="32" class="icon">
                                <v-icon size="20" color="white">mdi-lock</v-icon>
                            </v-avatar>
                        </template>
                        <template v-slot:prepend-inner>
                            <v-icon @click="showPassword = !showPassword"
                                style="padding-left: 20px;"
                                :color="black">
                                {{ showPassword ? 'mdi-eye-off' : 'mdi-eye' }}
                            </v-icon>
                        </template>
                    </v-text-field>
                </v-card>

                <div class="d-flex" style="justify-content: center;">
                    <v-btn class="me-2 buttonStyle" color="#4287f5" size="large" rounded="lg">
                        تسجيل الدخول
                    </v-btn>
                </div>
            </v-form>
        </v-card>
    </v-container>
</template>

<script>
export default {
    name: "Login",
    data() {
        return {
            email: '',
            password: '',
            confirmPassword: '',
            showPassword: false,
            showConfirmPassword: false
        }
    },
    methods: {
        login() {
            console.log('Connexion avec:', {
                email: this.email,
                password: this.password
            });
        }
    }
}
</script>

<style scoped>
.card {
    width: 500px;
    border-radius: 30px;
}

.icon {
    position: relative;
    bottom: 7px;
    right: 10px;
}

.buttonStyle {
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    transition: box-shadow 0.3s ease;
}

.buttonStyle:hover {
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}

.widthField {
    box-shadow: inset 3px 3px 6px rgba(0, 0, 0, 0.3), inset -3px -3px 6px rgba(255, 255, 255, 0.7);
    border-radius: 10px;
    transition: box-shadow 0.3s ease;
}
.v-text-field ::placeholder {
    padding-right: 20px;
}
.v-text-field :deep(input) {
    text-align: right;
    padding-right: 20px; 
}
</style>