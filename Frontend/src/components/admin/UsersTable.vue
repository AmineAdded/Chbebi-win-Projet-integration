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
            <th>الحالة</th>
            <th>الإجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>
              <div class="user-info">
                <div class="avatar">{{ user.name.charAt(0) }}</div>
                <span>{{ user.name }}</span>
              </div>
            </td>
            <td>{{ user.email }}</td>
            <td>
              <span :class="['status', user.status === 'active' ? 'status-active' : 'status-inactive']">
                {{ user.status === 'active' ? 'نشط' : 'غير نشط' }}
              </span>
            </td>
            <td>
              <div class="actions">
                <button class="icon-button edit" @click="editUser(user.id)" title="تعديل">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                  </svg>
                </button>
                <button class="icon-button delete" @click="showDeleteConfirm(user.id)" title="حذف">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"></polyline>
                    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
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
  </section>
</template>

<script>
export default {
  data() {
    return {
      users: [
        { id: 1, name: "أحمد", email: "ahmed@example.com", status: "active" },
        { id: 2, name: "سارة", email: "sarah@example.com", status: "active" },
        { id: 3, name: "محمد", email: "mohamed@example.com", status: "inactive" },
      ],
      showDeleteModal: false,
      userToDelete: null
    };
  },
  methods: {
    editUser(id) {
      // Logique pour éditer l'utilisateur
      console.log(`Éditer l'utilisateur ${id}`);
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
        this.users = this.users.filter(user => user.id !== this.userToDelete);
        this.showDeleteModal = false;
        this.userToDelete = null;
      }
    }
  }
};
</script>

<style scoped>
/* Réinitialisation et styles de base */
* {
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
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

/* Modal de confirmation */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  width: 400px;
  text-align: right;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal h3 {
  margin-top: 0;
  color: #333;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.cancel-button {
  background-color: #f3f4f6;
  color: #4b5563;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
}

.confirm-button {
  background-color: #ef4444;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  font-weight: 500;
}

.cancel-button:hover {
  background-color: #e5e7eb;
}

.confirm-button:hover {
  background-color: #dc2626;
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
  
  th, td {
    padding: 10px 12px;
  }
}
</style>