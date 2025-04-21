<template>
  <v-container
    class="d-flex justify-center align-center"
    style="min-height: 100vh; font-family: 'Tajawal', 'Segoe UI', sans-serif"
  >
    <v-card class="account-card mx-auto" elevation="4">
      <div class="text-center mb-4">
        <h2 class="text-h5 mb-3 primary--text font-weight-medium">تحديث الحساب</h2>
        <v-btn
          class="return-btn"
          elevation="2"
          @click.prevent="$emit('closeUpdateAccount')"
          rounded
          color="info"
          prepend-icon="mdi-arrow-right"
          size="small"
        >
          العودة
        </v-btn>
      </div>

      <v-form class="form-container">
        <v-row dense>
          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg">
                <v-text-field
                  v-model="nom"
                  placeholder="اسمك الكامل"
                  variant="outlined"
                  bg-color="surface"
                  color="primary"
                  density="compact"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.nom"
                  @input="errors.nom = ''"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary" size="small">mdi-account</v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg">
                <v-text-field
                  v-model="email"
                  placeholder="بريدك الإلكتروني"
                  variant="outlined"
                  bg-color="surface"
                  color="primary"
                  density="compact"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.email"
                  @input="errors.email = ''"
                  :rules="emailRules"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary" size="small">mdi-email</v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg">
                <v-text-field
                  v-model="currentPassword"
                  placeholder="كلمة السر الحالية"
                  :type="showCurrentPassword ? 'text' : 'password'"
                  variant="outlined"
                  bg-color="surface"
                  color="primary"
                  density="compact"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.currentPassword"
                  @input="errors.currentPassword = ''"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary" size="small">mdi-lock</v-icon>
                  </template>
                  <template v-slot:prepend-inner>
                    <v-icon @click="showCurrentPassword = !showCurrentPassword" class="eye-icon" size="small">
                      {{ showCurrentPassword ? "mdi-eye-off" : "mdi-eye" }}
                    </v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg">
                <v-text-field
                  v-model="newPassword"
                  placeholder="كلمة السر الجديدة"
                  :type="showNewPassword ? 'text' : 'password'"
                  variant="outlined"
                  bg-color="surface"
                  color="primary"
                  density="compact"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.newPassword"
                  :rules="passwordRules"
                  @input="errors.newPassword = ''"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary" size="small">mdi-lock-reset</v-icon>
                  </template>
                  <template v-slot:prepend-inner>
                    <v-icon @click="showNewPassword = !showNewPassword" class="eye-icon" size="small">
                      {{ showNewPassword ? "mdi-eye-off" : "mdi-eye" }}
                    </v-icon>
                  </template>
                </v-text-field>
              </v-sheet>
            </v-slide-y-transition>
          </v-col>

          <v-col cols="12">
            <v-slide-y-transition>
              <v-sheet class="field-wrapper" rounded="lg">
                <v-text-field
                  v-model="confirmNewPassword"
                  placeholder="تأكيد كلمة السر الجديدة"
                  :type="showConfirmNewPassword ? 'text' : 'password'"
                  variant="outlined"
                  bg-color="surface"
                  color="primary"
                  density="compact"
                  hide-details="auto"
                  dir="rtl"
                  :error-messages="errors.confirmNewPassword"
                  @input="errors.confirmNewPassword = ''"
                  :rules="confirmPasswordRules"
                  class="custom-input rounded-lg"
                >
                  <template v-slot:append-inner>
                    <v-icon color="primary" size="small">mdi-lock-check</v-icon>
                  </template>
                  <template v-slot:prepend-inner>
                    <v-icon
                      @click="showConfirmNewPassword = !showConfirmNewPassword"
                      class="eye-icon"
                      size="small"
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
                <h3 class="text-caption font-weight-medium mb-1">متطلبات كلمة المرور:</h3>
                <div class="requirements-list">
                  <div class="requirement-item">
                    <v-icon size="x-small" :color="hasUpperCase ? 'success' : 'grey'">
                      {{ hasUpperCase ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                    </v-icon>
                    <span :class="hasUpperCase ? 'text-success text-caption' : 'text-grey text-caption'">حرف كبير (A-Z)</span>
                  </div>
                 
                  <div class="requirement-item">
                    <v-icon size="x-small" :color="hasNumber ? 'success' : 'grey'">
                      {{ hasNumber ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                    </v-icon>
                    <span :class="hasNumber ? 'text-success text-caption' : 'text-grey text-caption'">رقم (0-9)</span>
                  </div>
                 
                  <div class="requirement-item">
                    <v-icon size="x-small" :color="hasMinLength ? 'success' : 'grey'">
                      {{ hasMinLength ? 'mdi-check-circle' : 'mdi-circle-outline' }}
                    </v-icon>
                    <span :class="hasMinLength ? 'text-success text-caption' : 'text-grey text-caption'">الحد الأدنى 8 أحرف</span>
                  </div>
                </div>
              </div>
            </v-expand-transition>
          </v-col>

          <v-col cols="12" class="mt-2">
            <v-scale-transition>
              <div class="d-flex justify-center gap-2">
                <v-btn
                  class="action-btn"
                  color="error"
                  size="small"
                  rounded="pill"
                  elevation="1"
                  @click.prevent="$emit('cancelUpdate')"
                >
                  <v-icon start size="small">mdi-close</v-icon>
                  إلغاء
                </v-btn>
                <v-btn
                
                  class="action-btn"
                  color="primary"
                  size="small"
                  rounded="pill"
                  elevation="1"
                  @click.prevent="update"
                >
                  <v-icon start size="small">mdi-check</v-icon>
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
    elevation="4"
    rounded="pill"
    timeout="4000"
  >
    <div class="d-flex align-center">
      <v-icon class="mr-2" color="white" size="small">
        {{ snackbarIcon }}
      </v-icon>
      {{ text }}
    </div>
    <template v-slot:actions>
      <v-btn icon size="x-small" @click="showSnackBar = false">
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
  max-width: 420px;
  width: 100%;
  border-radius: 16px;
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.07);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  overflow: visible;
  padding: 1.25rem !important;
  margin-top: -20px; /* Aide à centrer verticalement tout en laissant de l'espace en bas */
  max-height: 90vh;
  overflow-y: auto;
}

.account-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.1);
}

.return-btn {
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  text-transform: none;
  padding: 4px 16px;
  font-size: 0.8rem;
}

.return-btn:hover {
  transform: translateX(-3px);
}

.form-container {
  max-width: 380px;
  margin: auto;
}

.field-wrapper {
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  margin-bottom: 6px;
  background-color: rgba(241, 245, 249, 0.6);
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 10px;
  overflow: hidden;
}

.field-wrapper:hover {
  background-color: rgba(248, 250, 252, 0.9);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.04);
  transform: translateY(-1px);
}

.field-wrapper:focus-within {
  border-color: var(--v-primary-base, #2196f3);
  box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.1);
}

.eye-icon {
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.eye-icon:hover {
  opacity: 1;
}

.custom-input :deep(input) {
  text-align: right;
  font-size: 0.85rem;
  font-family: 'Tajawal', sans-serif;
  color: #333;
  transition: color 0.3s ease;
}

.custom-input :deep(.v-field__outline) {
  border-radius: 10px !important;
}

.action-btn {
  min-width: 100px;
  margin-left: 20px;
  font-weight: 500;
  font-size: 0.8rem;
  letter-spacing: 0.25px;
  text-transform: none;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.12);
}

.password-requirements {
  background-color: rgba(241, 245, 249, 0.7);
  border-radius: 10px;
  border: 1px dashed rgba(203, 213, 225, 0.8);
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.03);
  padding: 8px 12px;
  font-size: 0.75rem;
}

.requirements-list {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: space-between;
}

.requirement-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.7rem;
}

/* Pour éviter le débordement vertical */
v-container {
  min-height: calc(100vh - 20px);
  padding-bottom: 20px;
  overflow-y: auto;
}

/* Assurer que le scrolling est possible si nécessaire */
@media (max-height: 650px) {
  .account-card {
    max-height: 85vh;
    overflow-y: auto;
    padding: 1rem !important;
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