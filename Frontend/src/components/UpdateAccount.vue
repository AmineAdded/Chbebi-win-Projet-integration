<template>
  <v-container
    class="d-flex justify-center align-center"
    style="min-height: 100vh; font-family: 'Segoe UI'"
  >
    <v-card class="mx-auto pa-6 card" elevation="3">
      <div class="text-center mb-4">
        <v-btn class="close" @click.prevent="$emit('closeUpdateAccount')">
          العودة
        </v-btn>
      </div>

      <v-form class="form-container">
        <v-container>
          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="fullName"
              placeholder="اسمك الكامل"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              dir="rtl"
              :error-messages="errors.fullName"
              @input="errors.fullName = ''"
              class="custom-input"
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-account</v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="email"
              placeholder="بريدك الإلكتروني"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              dir="rtl"
              :error-messages="errors.email"
              @input="errors.email = ''"
              :rules="emailRules"
              class="custom-input"
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-email</v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="currentPassword"
              placeholder="كلمة السر الحالية"
              :type="showCurrentPassword ? 'text' : 'password'"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              dir="rtl"
              :error-messages="errors.currentPassword"
              @input="errors.currentPassword = ''"
              class="custom-input"
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-lock</v-icon>
              </template>
              <template v-slot:prepend-inner>
                <v-icon @click="showCurrentPassword = !showCurrentPassword">
                  {{ showCurrentPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="newPassword"
              placeholder="كلمة السر الجديدة"
              :type="showNewPassword ? 'text' : 'password'"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              dir="rtl"
              :error-messages="errors.newPassword"
              :rules="passwordRules"
              @input="errors.newPassword = ''"
              class="custom-input"
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-lock-reset</v-icon>
              </template>
              <template v-slot:prepend-inner>
                <v-icon @click="showNewPassword = !showNewPassword">
                  {{ showNewPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>

          <v-sheet class="field-container" elevation="1">
            <v-text-field
              v-model="confirmNewPassword"
              placeholder="تأكيد كلمة السر الجديدة"
              :type="showConfirmNewPassword ? 'text' : 'password'"
              variant="outlined"
              density="comfortable"
              hide-details="auto"
              dir="rtl"
              :error-messages="errors.confirmNewPassword"
              @input="errors.confirmNewPassword = ''"
              :rules="confirmPasswordRules"
              class="custom-input"
            >
              <template v-slot:append-inner>
                <v-icon color="primary">mdi-lock-check</v-icon>
              </template>
              <template v-slot:prepend-inner>
                <v-icon
                  @click="showConfirmNewPassword = !showConfirmNewPassword"
                >
                  {{ showConfirmNewPassword ? "mdi-eye-off" : "mdi-eye" }}
                </v-icon>
              </template>
            </v-text-field>
          </v-sheet>
        </v-container>

        <div class="d-flex justify-center">
          <v-btn
            class="me-2 buttonStyle"
            color="#2FE390"
            size="large"
            rounded="lg"
            @click.prevent="$emit('cancelUpdate')"
          >
            إلغاء
          </v-btn>
          <v-btn
            class="me-2 buttonStyle"
            color="#2D80D5"
            size="large"
            rounded="lg"
            @click.prevent="update"
          >
            تحديث المعلومات
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
import userService from "@/Services/userService";
export default {
  name: "UpdateAccount",
  props: {
    userData: {
      type: Object,
      default: () => ({
        fullName: "",
        email: "",
      }),
    },
  },
  data() {
    return {
      fullName: "",
      email: "",
      currentPassword: "",
      newPassword: "",
      confirmNewPassword: "",
      showCurrentPassword: false,
      showNewPassword: false,
      showConfirmNewPassword: false,
      showSnackBar: false,
      snackbarColor: "error",
      snackbarIcon: "mdi-alert-circle",
      text: "",
      errors: {
        fullName: "",
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
  created() {
    // Initialize form with user data
    this.fullName = this.userData.fullName;
    this.email = this.userData.email;
  },
  methods: {
    async update() {
      // Reset error messages
      Object.keys(this.errors).forEach((key) => {
        this.errors[key] = "";
      });

      // Validate required fields
      if (!this.fullName) {
        this.errors.fullName = "الاسم مطلوب";
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
          fullName: this.fullName,
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

        // Emit event to parent component
        this.$emit("profileUpdated", updateData);
      } catch (err) {
        // Show error message
        this.snackbarColor = "error";
        this.snackbarIcon = "mdi-alert-circle";
        this.text =
          err.response?.data?.message || "حدث خطأ أثناء تحديث المعلومات";
        this.showSnackBar = true;
      }
    },
  },
};
</script>

<style scoped>
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