<template>
  <v-container
    class="d-flex justify-center align-center"
    style="min-height: 100vh; font-family: 'Tajawal', 'Segoe UI', sans-serif"
  >
    <v-card class="account-card mx-auto" elevation="8" rounded="xl">
      <div class="account-header pa-4">
        <v-avatar class="profile-avatar mb-2" color="primary" size="68">
          <v-icon size="36" color="white">mdi-account-edit</v-icon>
        </v-avatar>
        <h2 class="text-h5 font-weight-bold gradient-text mb-2">تحديث الحساب</h2>
        <div class="subtitle text-body-2 text-medium-emphasis mb-2">يمكنك تحديث معلومات حسابك بسهولة</div>
        <v-btn
          class="return-btn"
          variant="tonal"
          @click.prevent="$emit('closeUpdateAccount')"
          rounded="pill"
          color="info"
          prepend-icon="mdi-arrow-right"
          size="small"
        >
          العودة
        </v-btn>
      </div>

      <v-divider class="mx-4 my-1"></v-divider>

      <v-form class="form-container pa-4">
        <v-row dense>
          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg" elevation="0">
                <v-text-field
                  v-model="nom"
                  placeholder="اسمك الكامل"
                  variant="outlined"
                  bg-color="surface-variant"
                  color="primary"
                  density="comfortable"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.nom"
                  @input="errors.nom = ''"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary">mdi-account</v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg" elevation="0">
                <v-text-field
                  v-model="email"
                  placeholder="بريدك الإلكتروني"
                  variant="outlined"
                  bg-color="surface-variant"
                  color="primary"
                  density="comfortable"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.email"
                  @input="errors.email = ''"
                  :rules="emailRules"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary">mdi-email-outline</v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12" class="mt-2">
            <v-expansion-panels variant="accordion" class="password-panels" flat>
              <v-expansion-panel title="تغيير كلمة المرور">
                <template v-slot:title>
                  <div class="d-flex align-center">
                    <v-icon color="primary" class="mr-2">mdi-lock-reset</v-icon>
                    <span class="font-weight-medium">تغيير كلمة المرور</span>
                  </div>
                </template>
                <v-expansion-panel-text>
                  <v-row dense>
                    <v-col cols="12">
                      <v-slide-y-transition>
                        <v-sheet class="field-wrapper" rounded="lg" elevation="0">
                          <v-text-field
                            v-model="currentPassword"
                            placeholder="كلمة السر الحالية"
                            :type="showCurrentPassword ? 'text' : 'password'"
                            variant="outlined"
                            bg-color="surface-variant"
                            color="primary"
                            density="comfortable"
                            hide-details="auto"
                            dir="rtl"
                            :error-messages="errors.currentPassword"
                            @input="errors.currentPassword = ''"
                            class="custom-input rounded-lg"
                          >
                            <template v-slot:append-inner>
                              <v-icon color="primary">mdi-lock</v-icon>
                            </template>
                            <template v-slot:prepend-inner>
                              <v-icon @click="showCurrentPassword = !showCurrentPassword" class="eye-icon">
                                {{ showCurrentPassword ? "mdi-eye-off" : "mdi-eye" }}
                              </v-icon>
                            </template>
                          </v-text-field>
                        </v-sheet>
                      </v-slide-y-transition>
                    </v-col>

                    <v-col cols="12">
                      <v-slide-y-transition>
                        <v-sheet class="field-wrapper" rounded="lg" elevation="0">
                          <v-text-field
                            v-model="newPassword"
                            placeholder="كلمة السر الجديدة"
                            :type="showNewPassword ? 'text' : 'password'"
                            variant="outlined"
                            bg-color="surface-variant"
                            color="primary"
                            density="comfortable"
                            hide-details="auto"
                            dir="rtl"
                            :error-messages="errors.newPassword"
                            :rules="passwordRules"
                            @input="errors.newPassword = ''"
                            @focus="showPasswordRequirements = true"
                            class="custom-input rounded-lg"
                          >
                            <template v-slot:append-inner>
                              <v-icon color="primary">mdi-shield-key</v-icon>
                            </template>
                            <template v-slot:prepend-inner>
                              <v-icon @click="showNewPassword = !showNewPassword" class="eye-icon">
                                {{ showNewPassword ? "mdi-eye-off" : "mdi-eye" }}
                              </v-icon>
                            </template>
                          </v-text-field>
                        </v-sheet>
                      </v-slide-y-transition>
                    </v-col>

                    <v-col cols="12">
                      <v-slide-y-transition>
                        <v-sheet class="field-wrapper" rounded="lg" elevation="0">
                          <v-text-field
                            v-model="confirmNewPassword"
                            placeholder="تأكيد كلمة السر الجديدة"
                            :type="showConfirmNewPassword ? 'text' : 'password'"
                            variant="outlined"
                            bg-color="surface-variant"
                            color="primary"
                            density="comfortable"
                            hide-details="auto"
                            dir="rtl"
                            :error-messages="errors.confirmNewPassword"
                            @input="errors.confirmNewPassword = ''"
                            :rules="confirmPasswordRules"
                            class="custom-input rounded-lg"
                          >
                            <template v-slot:append-inner>
                              <v-icon color="primary">mdi-shield-check</v-icon>
                            </template>
                            <template v-slot:prepend-inner>
                              <v-icon
                                @click="showConfirmNewPassword = !showConfirmNewPassword"
                                class="eye-icon"
                              >
                                {{ showConfirmNewPassword ? "mdi-eye-off" : "mdi-eye" }}
                              </v-icon>
                            </template>
                          </v-text-field>
                        </v-sheet>
                      </v-slide-y-transition>
                    </v-col>

                    <v-col cols="12" class="mt-2">
                      <v-expand-transition>
                        <div v-if="showPasswordRequirements && newPassword" class="password-requirements mb-2">
                          <h3 class="text-body-2 font-weight-medium mb-2">متطلبات كلمة المرور:</h3>
                          <div class="requirements-list">
                            <div class="requirement-item">
                              <v-icon size="small" :color="hasUpperCase ? 'success' : 'grey-darken-1'">
                                {{ hasUpperCase ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                              </v-icon>
                              <span :class="hasUpperCase ? 'text-success text-caption' : 'text-medium-emphasis text-caption'">
                                حرف كبير (A-Z)
                              </span>
                            </div>
                           
                            <div class="requirement-item">
                              <v-icon size="small" :color="hasNumber ? 'success' : 'grey-darken-1'">
                                {{ hasNumber ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                              </v-icon>
                              <span :class="hasNumber ? 'text-success text-caption' : 'text-medium-emphasis text-caption'">
                                رقم (0-9)
                              </span>
                            </div>
                           
                            <div class="requirement-item">
                              <v-icon size="small" :color="hasMinLength ? 'success' : 'grey-darken-1'">
                                {{ hasMinLength ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                              </v-icon>
                              <span :class="hasMinLength ? 'text-success text-caption' : 'text-medium-emphasis text-caption'">
                                الحد الأدنى 8 أحرف
                              </span>
                            </div>
                          </div>
                        </div>
                      </v-expand-transition>
                    </v-col>
                  </v-row>
                </v-expansion-panel-text>
              </v-expansion-panel>
            </v-expansion-panels>
          </v-col>

          <v-col cols="12" class="mt-4">
            <v-scale-transition>
              <div class="d-flex justify-center">
                <v-btn
                  class="action-btn mr-2"
                  color="error"
                  variant="outlined"
                  size="large"
                  rounded="pill"
                  prepend-icon="mdi-close"
                  elevation="0"
                  @click.prevent="$emit('cancelUpdate')"
                >
                  إلغاء
                </v-btn>
                <v-btn
                  class="action-btn"
                  color="primary"
                  size="large"
                  rounded="pill"
                  elevation="3"
                  @click.prevent="update"
                >
                  <v-icon start>mdi-content-save</v-icon>
                  تحديث المعلومات
                </v-btn>
              </div>
            </v-scale-transition>
          </v-col>
        </v-row>
      </v-form>
    </v-card>
  </v-container>

  <v-snackbar
    v-model="showSnackBar"
    :color="snackbarColor"
    location="top"
    elevation="6"
    rounded="pill"
    timeout="4000"
  >
    <div class="d-flex align-center">
      <v-icon class="mr-2" color="white">
        {{ snackbarIcon }}
      </v-icon>
      {{ text }}
    </div>
    <template v-slot:actions>
      <v-btn icon variant="text" size="small" @click="showSnackBar = false">
        <v-icon color="white">mdi-close</v-icon>
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
import userService from "@/Services/userService";
export default {
  name: "UpdateAccount",
  props: {
    userData: {
      type: Object,
      default: () => ({
        nom: "",
        email: "",
      }),
    },
  },
  data() {
    return {
      nom: "",
      email: "",
      currentPassword: "",
      newPassword: "",
      confirmNewPassword: "",
      showCurrentPassword: false,
      showNewPassword: false,
      showConfirmNewPassword: false,
      showPasswordRequirements: true,
      showSnackBar: false,
      snackbarColor: "error",
      snackbarIcon: "mdi-alert-circle",
      text: "",
      errors: {
        nom: "",
        email: "",
        currentPassword: "",
        newPassword: "",
        confirmNewPassword: "",
      },
      passwordRules: [
        (v) =>
          !v || /[A-Z]/.test(v) || "يجب أن تحتوي على حرف كبير واحد على الأقل",
        (v) => !v || /[0-9]/.test(v) || "يجب أن تحتوي على رقم واحد على الأقل",
        (v) => !v || v.length >= 8 || "الحد الأدنى 8 أحرف",
      ],
      emailRules: [
        (v) =>
          !v || /.+@.+\..+/.test(v) || "يجب أن يكون البريد الإلكتروني صالحًا",
      ],
      confirmPasswordRules: [
        (v) =>
          !v || (v && v === this.newPassword) || "كلمات المرور غير متطابقة",
      ],
    };
  },
  computed: {
    hasUpperCase() {
      return /[A-Z]/.test(this.newPassword);
    },
    hasNumber() {
      return /[0-9]/.test(this.newPassword);
    },
    hasMinLength() {
      return this.newPassword.length >= 8;
    },
  },
  created() {
    if (this.userData && this.userData.nom && this.userData.email) {
      this.nom = this.userData.nom;
      this.email = this.userData.email;
    } else {
      const userLocal = localStorage.getItem("user");
      if (userLocal) {
        const parsedUser = JSON.parse(userLocal);
        this.nom = parsedUser.nom || "";
        this.email = parsedUser.email || "";
      }
    }
  },
  methods: {
    async update() {
      // Reset error messages
      Object.keys(this.errors).forEach((key) => {
        this.errors[key] = "";
      });

      // Validate required fields
      if (!this.nom) {
        this.errors.nom = "الاسم مطلوب";
      }
      if (!this.email) {
        this.errors.email = "البريد الإلكتروني مطلوب";
      }

      // If changing password, validate password fields
      if (this.newPassword || this.confirmNewPassword) {
        if (!this.currentPassword) {
          this.errors.currentPassword = "كلمة المرور الحالية مطلوبة";
          return;
        }
        if (!this.newPassword) {
          this.errors.newPassword = "كلمة المرور الجديدة مطلوبة";
          return;
        }
        if (!this.confirmNewPassword) {
          this.errors.confirmNewPassword = "تأكيد كلمة المرور الجديدة مطلوب";
          return;
        }
        if (this.newPassword !== this.confirmNewPassword) {
          this.errors.confirmNewPassword = "كلمات المرور غير متطابقة";
          return;
        }
      }

      // Check if there are any errors
      if (Object.values(this.errors).some((error) => error !== "")) {
        return;
      }

      try {
        // Prepare data for update
        const updateData = {
          nom: this.nom,
          email: this.email,
        };

        // Add password data if changing password
        if (this.newPassword && this.currentPassword) {
          updateData.currentPassword = this.currentPassword;
          updateData.newPassword = this.newPassword;
        }

        // Call API to update user profile
        await userService.updateUserProfile(updateData);

        // Show success message
        this.snackbarColor = "success";
        this.snackbarIcon = "mdi-check-circle";
        this.text = "تم تحديث المعلومات بنجاح";
        this.showSnackBar = true;

        // Clear password fields after successful update
        this.currentPassword = "";
        this.newPassword = "";
        this.confirmNewPassword = "";

        // Emit event to parent component
        this.$emit("profileUpdated", updateData);
        // Mettre à jour les infos dans le localStorage
        const userInStorage = JSON.parse(localStorage.getItem("user") || "{}");
        localStorage.setItem(
          "user",
          JSON.stringify({ ...userInStorage, ...updateData })
        );
      } catch (err) {
        console.error("Erreur API :", err);

        this.snackbarColor = "error";
        this.snackbarIcon = "mdi-alert-circle";

        // Vérifie si le backend retourne directement un string
        if (typeof err.response?.data === "string") {
          this.text = err.response.data;
        } else {
          this.text =
            err.response?.data?.message || "حدث خطأ أثناء تحديث المعلومات";
        }

        this.showSnackBar = true;
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Tajawal:wght@400;500;700&display=swap');

.account-card {
  max-width: 460px;
  width: 100%;
  border-radius: 24px !important;
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08) !important;
  transition: all 0.4s cubic-bezier(0.19, 1, 0.22, 1);
  overflow: visible;
  margin-top: -20px;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
  border: 1px solid rgba(228, 233, 240, 0.7);
}

.account-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12) !important;
}

.account-header {
  text-align: center;
  position: relative;
}

.profile-avatar {
  margin: 0 auto;
  border: 3px solid rgba(var(--v-theme-primary), 0.2);
  box-shadow: 0 5px 15px rgba(var(--v-theme-primary), 0.15);
  transition: all 0.3s ease;
}

.profile-avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(var(--v-theme-primary), 0.2);
}

.gradient-text {
  background: linear-gradient(90deg, #2196f3, #6610f2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-fill-color: transparent;
}

.return-btn {
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  text-transform: none;
  padding: 5px 20px;
  font-size: 0.8rem;
}

.return-btn:hover {
  transform: translateX(-3px);
  background-color: rgba(var(--v-theme-info), 0.1);
}

.form-container {
  max-width: 400px;
  margin: auto;
}

.field-wrapper {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  margin-bottom: 12px;
  background-color: white !important;
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  overflow: hidden;
}

.field-wrapper:hover {
  background-color: white !important;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
  transform: translateY(-1px);
}

.field-wrapper:focus-within {
  border-color: rgba(var(--v-theme-primary), 1);
  box-shadow: 0 0 0 2px rgba(var(--v-theme-primary), 0.1);
}

.eye-icon {
  opacity: 0.7;
  transition: opacity 0.2s ease;
  cursor: pointer;
}

.eye-icon:hover {
  opacity: 1;
  color: rgba(var(--v-theme-primary), 1);
}

.custom-input :deep(input) {
  text-align: right;
  font-size: 0.9rem;
  font-family: 'Tajawal', sans-serif;
  color: rgba(0, 0, 0, 0.87);
  transition: color 0.3s ease;
  background-color: white !important;
}

.custom-input :deep(.v-field__outline) {
  border-radius: 12px !important;
}

.custom-input :deep(.v-field__field) {
  background-color: white !important;
}

.custom-input :deep(.v-field) {
  background-color: white !important;
  border-color: rgba(226, 232, 240, 0.8);
}

.custom-input :deep(.v-field__input) {
  background-color: white !important;
}

.action-btn {
  min-width: 120px;
  font-weight: 500;
  font-size: 0.9rem;
  letter-spacing: 0.25px;
  text-transform: none;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  padding: 0 24px;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(var(--v-theme-primary), 0.15) !important;
}

.password-requirements {
  background-color: rgba(241, 245, 249, 0.7);
  border-radius: 12px;
  border: 1px dashed rgba(203, 213, 225, 0.8);
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.03);
  padding: 12px 16px;
  font-size: 0.75rem;
}

.requirements-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.requirement-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.8rem;
}

.password-panels {
  background-color: rgba(241, 245, 249, 0.5);
  border-radius: 12px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.password-panels:hover {
  background-color: rgba(241, 245, 249, 0.9);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
}

v-container {
  min-height: calc(100vh - 20px);
  padding-bottom: 20px;
  overflow-y: auto;
}

.subtitle {
  color: rgba(0, 0, 0, 0.6);
  font-weight: 400;
}

@media (max-height: 700px) {
  .account-card {
    max-height: 85vh;
    overflow-y: auto;
    padding-top: 0.75rem !important;
    padding-bottom: 1rem !important;
  }
 
  .text-center.mb-4 {
    margin-bottom: 0.5rem !important;
  }
 
  h2.text-h5 {
    font-size: 1.25rem !important;
    margin-bottom: 0.5rem !important;
  }
}
</style>