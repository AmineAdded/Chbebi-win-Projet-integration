<template>
  <section class="user-management">
    <div class="header">
      <h2>👥 إدارة المستخدمين</h2>
      <button class="add-button">
        <span class="icon">+</span>
        <span>إضافة مستخدم</span>
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>الاسم</th>
            <th>البريد الإلكتروني</th>
            <th>الدور</th>
            <th>الحالة</th>
            <th>الإجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <div class="user-info">
                <div class="avatar">
                  {{ user.nom ? user.nom.charAt(0) : "?" }}
                </div>
                {{ user.nom }}
              </div>
            </td>
            <td>{{ user.email }}</td>
            <td>
              {{
                user.role === 0
                  ? "مستخدم"
                  : user.role === 1
                  ? "مشرف"
                  : user.role === 2
                  ? "مدير"
                  : "غير معروف"
              }}
            </td>

            <td>
              <span
                :class="[
                  'status',
                  user.id === currentUserId
                    ? 'status-active'
                    : 'status-inactive',
                ]"
              >
                {{ user.id === currentUserId ? "نشط" : "غير نشط" }}
              </span>
            </td>

            <td>
              <div class="actions">
                <button
                  class="icon-button edit"
                  @click="editUser(user.id)"
                  title="تعديل"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <path
                      d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"
                    ></path>
                    <path
                      d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"
                    ></path>
                  </svg>
                </button>
                <button
                  class="icon-button delete"
                  @click="showDeleteConfirm(user.id)"
                  title="حذف"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                  >
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path
                      d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"
                    ></path>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal de confirmation pour la suppression -->
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal">
        <h3>تأكيد الحذف</h3>
        <p>هل أنت متأكد من رغبتك في حذف هذا المستخدم؟</p>
        <div class="modal-actions">
          <button class="cancel-button" @click="cancelDelete">إلغاء</button>
          <button class="confirm-button" @click="confirmDelete">تأكيد</button>
        </div>
      </div>
    </div>

    <!-- Modal de mise à jour de l'utilisateur amélioré avec le champ de rôle au lieu du statut -->
    <Transition name="modal-fade">
      <div v-if="showEditModal" class="modal-overlay" @click.self="cancelEdit">
        <div class="modal" :class="{ 'rtl-content': true }">
          <div class="modal-header">
            <h3>تحديث معلومات المستخدم</h3>
            <button class="close-button" @click="cancelEdit">
              <span>&times;</span>
            </button>
          </div>

          <div class="modal-body">
            <div class="form-group">
              <label for="userName">الاسم</label>
              <input
                id="userName"
                v-model="editedUser.nom"
                type="text"
                placeholder="أدخل الاسم"
              />
            </div>

            <div class="form-group">
              <label for="userEmail">البريد الإلكتروني</label>
              <input
                id="userEmail"
                v-model="editedUser.email"
                type="email"
                placeholder="أدخل البريد الإلكتروني"
              />
            </div>

            <div class="form-group">
              <label for="userRole">الدور</label>
              <select
                id="userRole"
                v-model="editedUser.role"
                class="role-select"
              >
                <option value="" disabled>اختر الدور</option>
                <option :value="0">مستخدم</option>
                <option :value="1">مشرف</option>
                <option :value="2">مدير</option>
              </select>
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn cancel-button" @click="cancelEdit">إلغاء</button>
            <button class="btn confirm-button" @click="confirmEdit">
              تأكيد التحديث
            </button>
          </div>
        </div>
      </div>
    </Transition>
  </section>
</template>

<script>
import userService from "@/Services/userService";

export default {
  data() {
    return {
      currentUserId: null,
      users: [],
      showDeleteModal: false,
      showEditModal: false,
      userToDelete: null,
      editedUser: {
        id: null,
        nom: "",
        email: "",
        role: "", // Changement de status à role
      },
    };
  },
  methods: {
    fetchUsers() {
      userService
        .getAllUsers()
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          console.error(
            "Erreur lors de la récupération des utilisateurs :",
            error
          );
        });
    },
    editUser(id) {
      const user = this.users.find((u) => u.id === id);
      if (user) {
        this.editedUser = { ...user }; // clone de l'objet
        // Convertir le rôle en chaîne pour le select
        this.editedUser.role = this.editedUser.role.toString();
        this.showEditModal = true;
      }
    },
    cancelEdit() {
      this.showEditModal = false;
      this.editedUser = { id: null, nom: "", email: "", role: "" }; // Réinitialisation avec role au lieu de status
    },
    confirmEdit() {
      const userToUpdate = { ...this.editedUser };
      userToUpdate.role = parseInt(userToUpdate.role);
      console.log(userToUpdate)

      userService
        .updateUser(userToUpdate.id, userToUpdate)
        .then(() => {
          this.fetchUsers();
          this.cancelEdit();
        })
        .catch((error) => {
          console.error("Erreur lors de la mise à jour :", error);
        });
    },

    showDeleteConfirm(id) {
      this.userToDelete = id;
      this.showDeleteModal = true;
    },
    cancelDelete() {
      this.showDeleteModal = false;
      this.userToDelete = null;
    },
    confirmDelete() {
      if (this.userToDelete) {
        userService
          .deleteUser(this.userToDelete)
          .then(() => {
            this.fetchUsers(); // mise à jour après suppression
            this.cancelDelete();
          })
          .catch((error) => {
            console.error("Erreur lors de la suppression :", error);
          });
      }
    },
  },
  mounted() {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      const user = JSON.parse(storedUser);
      this.currentUserId = user.id;
    }
    this.fetchUsers();
  },
};
</script>

<style scoped>
/* Réinitialisation et styles de base */
* {
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

.user-management {
  direction: rtl;
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

/* En-tête avec titre et bouton d'ajout */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

h2 {
  font-size: 1.5rem;
  color: #333;
  margin: 0;
  font-weight: 600;
}

.add-button {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #6366f1;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.add-button:hover {
  background-color: #4f46e5;
}

.add-button .icon {
  font-size: 1.2rem;
  font-weight: bold;
}

/* Container de table avec ombre */
.table-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

/* Styles de la table */
table {
  width: 100%;
  border-collapse: collapse;
  text-align: right;
}

thead {
  background-color: #f9fafb;
}

th {
  padding: 14px 16px;
  font-weight: 600;
  color: #4b5563;
  border-bottom: 1px solid #e5e7eb;
  font-size: 0.875rem;
  text-transform: uppercase;
}

td {
  padding: 12px 16px;
  color: #374151;
  border-bottom: 1px solid #f1f1f3;
}

tbody tr:hover {
  background-color: #f9fafb;
}

/* Information utilisateur avec avatar */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  background-color: #6366f1;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

/* Indicateur de statut */
.status {
  display: inline-flex;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-active {
  background-color: #dcfce7;
  color: #166534;
}

.status-inactive {
  background-color: #fee2e2;
  color: #991b1b;
}

/* Boutons d'action */
.actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.icon-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
}

.edit {
  background-color: #e0f2fe;
  color: #0284c7;
}

.edit:hover {
  background-color: #bae6fd;
}

.delete {
  background-color: #fee2e2;
  color: #ef4444;
}

.delete:hover {
  background-color: #fecaca;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background-color: #fff;
  border-radius: 8px;
  padding: 24px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  text-align: right;
}

.rtl-content {
  direction: rtl;
}

.modal-header {
  background-color: #f8f9fa;
  padding: 16px 20px;
  border-bottom: 1px solid #eaeaea;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #3c4257;
  font-size: 18px;
  font-weight: 600;
}

.modal h3 {
  margin-top: 0;
  color: #333;
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  transition: color 0.2s;
}

.close-button:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #454545;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #409eff;
  outline: none;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* Style spécifique pour le select de rôle */
.role-select {
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23555' d='M6 8.5l-4-4 1-1 3 3 3-3 1 1z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: left 12px center;
  padding-left: 30px;
}

.rtl-content .role-select {
  background-position: right 12px center;
  padding-right: 30px;
  padding-left: 12px;
}

.modal-actions,
.modal-footer {
  padding: 16px 20px;
  background-color: #f8f9fa;
  border-top: 1px solid #eaeaea;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-button,
.btn.cancel {
  background-color: #f3f4f6;
  color: #4b5563;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
}

.confirm-button,
.btn.confirm {
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
}

.cancel-button:hover,
.btn.cancel:hover {
  background-color: #e5e7eb;
}

.confirm-button:hover,
.btn.confirm:hover {
  background-color: #66b1ff;
}

/* Transition d'animation pour le modal */
.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}

.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-to,
.modal-fade-leave-from {
  opacity: 1;
}

/* Responsive design */
@media (max-width: 768px) {
  .user-management {
    padding: 16px;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  table {
    display: block;
    overflow-x: auto;
  }

  th,
  td {
    padding: 10px 12px;
  }

  .modal {
    width: 95%;
  }

  .modal-actions,
  .modal-footer {
    flex-direction: column;
  }

  .cancel-button,
  .confirm-button,
  .btn {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>