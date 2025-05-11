<template>
  <div class="Events-container">
    <div class="header">
      <h2>📍 الفعاليات وورش العمل القراب</h2>
      <div class="header-actions">
        <div class="search-container">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="بحث في الفعاليات..."
            class="search-input"
          />
          <span class="search-icon">🔍</span>
        </div>
        <button class="add-button" @click="showAddForm = true">
          <span class="icon">+</span> إضافة فعاليات جدد
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>جاري تحميل البيانات...</p>
    </div>

    <div v-else>
      <div class="Events-list" v-if="paginatedEvents.length > 0">
        <div
          v-for="(event, index) in paginatedEvents"
          :key="event.id"
          class="event-card"
        >
          <div class="event-image">
            <img :src="'http://localhost:9090' + event.image" alt="صورة الحدث" width="100%" height="100%"/>
          </div>
          <div class="event-content">
            <div class="event-number">{{ startIndex + index + 1 }}</div>
            <div class="event-title">{{ event.nom }}</div>
            <div class="event-date">📅 {{ formatDate(event.date) }}</div>
            <div class="event-description">{{ event.description }}</div>
            <a :href="event.lien" target="_blank" class="event-link"
              >🔗 رابط الحدث</a
            >
          </div>
          <div class="event-actions">
            <button class="action-btn edit" @click="editEvent(event)">
              <span class="icon">✏️</span>
            </button>
            <button class="action-btn delete" @click="confirmDelete(event)">
              <span class="icon">🗑</span>
            </button>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-icon">📝</div>
        <p>لا توجد فعاليات حاليًا</p>
        <button class="add-button" @click="showAddForm = true">
          إضافة فعاليات جدد
        </button>
      </div>

      <!-- Pagination Controls -->
      <div
        class="pagination-controls"
        v-if="filteredEvents.length > itemsPerPage"
      >
        <button
          class="pagination-button"
          :disabled="currentPage === 1"
          @click="goToPage(currentPage - 1)"
        >
          <span class="pagination-icon">▶</span>
          السابق
        </button>

        <div class="page-numbers">
          <button
            v-for="page in displayedPageNumbers"
            :key="page"
            class="page-number"
            :class="{ active: currentPage === page }"
            @click="goToPage(page)"
          >
            {{ page }}
          </button>
        </div>

        <button
          class="pagination-button"
          :disabled="currentPage === totalPages"
          @click="goToPage(currentPage + 1)"
        >
          التالي
          <span class="pagination-icon">◀</span>
        </button>

        <div class="pagination-info">
          <select
            v-model="itemsPerPage"
            @change="handleItemsPerPageChange"
            class="items-per-page"
          >
            <option :value="5">5 لكل صفحة</option>
            <option :value="10">10 لكل صفحة</option>
            <option :value="15">15 لكل صفحة</option>
            <option :value="20">20 لكل صفحة</option>
          </select>
          <span class="page-indicator">
            صفحة {{ currentPage }} من {{ totalPages }}
          </span>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation de suppression -->
    <div class="modal-delete" v-if="showDeleteModal">
    <div class="modal-overlay" @click.self="showDeleteModal = false"></div>
    <div class="modal-box">
        <div class="modal-header">
        <h3>⚠️ تأكيد الحذف</h3>
        <button class="btn-close" @click="showDeleteModal = false">×</button>
        </div>
        <div class="modal-body">
        <p>هل أنت متأكد من رغبتك في حذف هذه الفعالية؟</p>
        <p class="event-name">{{ eventToDelete?.nom }}</p>
        </div>
        <div class="modal-footer">
        <button class="btn-cancel" @click="showDeleteModal = false">إلغاء</button>
        <button class="btn-confirm" @click="deleteEvent">حذف الفعالية</button>
        </div>
    </div>
    </div>

    <!-- Formulaire d'ajout / modification -->
    <div class="modal" v-if="showAddForm || editingEvent">
    <div class="modal-overlay" @click.self="closeModal"></div>
    <div class="modal-container">
        <div class="modal-header">
        <h3>{{ editingEvent ? "تعديل الفعالية" : "إضافة فعالية جديدة" }}</h3>
        <button class="modal-close" @click="cancelEdit">×</button>
        </div>
        <div class="modal-body">
        <form @submit.prevent="saveEvent">
            <div class="form-group">
            <label for="eventName">اسم الفعالية</label>
            <input type="text" id="eventName" v-model="currentEvent.nom" required />
            </div>
            <div class="form-group">
            <label for="eventDescription">الوصف</label>
            <textarea id="eventDescription" v-model="currentEvent.description" required></textarea>
            </div>
            <div class="form-group">
            <label for="eventDate">التاريخ</label>
            <input type="datetime-local" id="eventDate" v-model="currentEvent.date" required />
            </div>
            <div class="form-group">
            <label for="eventLink">الرابط</label>
            <input type="url" id="eventLink" v-model="currentEvent.lien" />
            </div>
            <div class="form-group">
            <label for="eventImage">الصورة</label>
            <input type="file" id="eventImage" @change="uploadImage" accept="image/*" />
            <p v-if="currentEvent.image">{{ currentEvent.image }}</p>
            </div>

            <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="cancelEdit">إلغاء</button>
            <button type="submit" class="confirm-btn">
                {{ editingEvent ? "حفظ التعديلات" : "إضافة الفعالية" }}
            </button>
            </div>
        </form>
        </div>
    </div>
    </div>

    <!-- Success/Error Messages -->
    <div class="error-alert" v-if="error">
      <div class="error-content">
        <span class="error-icon">⚠️</span>
        <span class="error-message">{{ error }}</span>
        <button class="error-close" @click="error = null">×</button>
      </div>
    </div>

    <div class="success-alert" v-if="successMessage">
      <div class="success-content">
        <span class="success-icon">✅</span>
        <span class="success-message">{{ successMessage }}</span>
        <button class="success-close" @click="successMessage = null">×</button>
      </div>
    </div>
  </div>
</template>

<script>
import eventService from "@/Services/eventService";
import axios from "axios";

export default {
  data() {
    return {
      events: [],
      searchQuery: "",
      loading: false,
      error: null,
      successMessage: null,
      showDeleteModal: false,
      eventToDelete: null,
      showAddForm: false,
      editingEvent: null,
      currentEvent: {
        nom: "",
        description: "",
        date: "",
        lien: "",
        image: "",
      },
      currentPage: 1,
      itemsPerPage: 10,
      maxPageButtons: 5,
    };
  },
  computed: {
    filteredEvents() {
      if (!this.searchQuery) return this.events;
      const query = this.searchQuery.toLowerCase();
      return this.events.filter(
        (event) =>
          event.nom.toLowerCase().includes(query) ||
          event.description.toLowerCase().includes(query)
      );
    },
    totalPages() {
      return Math.ceil(this.filteredEvents.length / this.itemsPerPage);
    },
    paginatedEvents() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredEvents.slice(start, end);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage;
    },
    displayedPageNumbers() {
      const halfButtons = Math.floor(this.maxPageButtons / 2);
      let startPage = Math.max(1, this.currentPage - halfButtons);
      let endPage = Math.min(
        this.totalPages,
        startPage + this.maxPageButtons - 1
      );

      // Adjust if we're near the end
      if (endPage - startPage + 1 < this.maxPageButtons) {
        startPage = Math.max(1, endPage - this.maxPageButtons + 1);
      }

      const pages = [];
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      return pages;
    },
  },
  watch: {
    searchQuery() {
      this.currentPage = 1;
    },
  },
  mounted() {
    this.fetchEvents();
  },
  methods: {
  fetchEvents() {
    this.loading = true;
    eventService.getAllEvents()
      .then((response) => {
        this.events = response.data;
      })
      .catch((error) => {
        console.error("Erreur lors de la récupération des événements :", error);
        this.error = "فشل في تحميل الأحداث";
      })
      .finally(() => {
        this.loading = false;
      });
  },
  confirmDelete(event) {
    this.eventToDelete = event;
    this.showDeleteModal = true;
  },
  deleteEvent() {
    this.loading = true;
    eventService.deleteEvent(this.eventToDelete.id)
      .then(() => {
        this.events = this.events.filter(e => e.id !== this.eventToDelete.id);
        this.showSuccessMessage("تم حذف الحدث بنجاح");
        this.showDeleteModal = false;
        if (this.paginatedEvents.length === 0 && this.currentPage > 1) {
          this.currentPage--;
        }
      })
      .catch((error) => {
        console.error("Erreur lors de la suppression :", error);
        this.error = "فشل في حذف الحدث";
      })
      .finally(() => {
        this.loading = false;
      });
  },
  saveEvent() {
    this.loading = true;
    if (this.editingEvent) {
      eventService.updateEvent(this.editingEvent.id, this.currentEvent)
        .then((response) => {
          const index = this.events.findIndex(e => e.id === this.editingEvent.id);
          if (index !== -1) {
            this.events[index] = response.data;
          }
          this.cancelEdit();
          this.showSuccessMessage("تم تحديث معلومات الحدث بنجاح");
        })
        .catch((error) => {
          console.error("Erreur lors de la mise à jour :", error);
          this.error = "فشل في تحديث معلومات الحدث";
        })
        .finally(() => {
          this.loading = false;
        });
    } else {
      eventService.createEvent(this.currentEvent)
        .then((response) => {
          this.events.unshift(response.data);
          this.cancelEdit();
          this.showSuccessMessage("تم إضافة الحدث بنجاح");
          this.currentPage = 1;
        })
        .catch((error) => {
          console.error("Erreur lors de la création :", error);
          this.error = "فشل في إضافة الحدث";
        })
        .finally(() => {
          this.loading = false;
        });
    }
  },
  showSuccessMessage(message) {
      this.successMessage = message;
      // Disparaît après 3 secondes (3000 millisecondes)
      setTimeout(() => {
        this.successMessage = null;
      }, 3000);
    },
  editEvent(event) {
    this.editingEvent = event;
    this.currentEvent = { ...event };
    this.showAddForm = true;
  },
  cancelEdit() {
    this.showAddForm = false;
    this.editingEvent = null;
    this.currentEvent = {
      nom: "",
      description: "",
      date: "",
      lien: "",
      image: ""
    };
  },
  formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('ar-TN', options);
  },
  async uploadImage(event) {
      const file = event.target.files[0];
      if (!file) return;
      const formData = new FormData();
      formData.append("file", file);

      try {
        const res = await axios.post("http://localhost:9090/api/upload", formData);
        this.currentEvent.image = res.data; // exemple: "/files/images/uuid_nom.jpg"
      } catch (err) {
        console.error("Erreur upload image", err);
      }
    },
}
};
</script>

<style scoped>
.modal-delete {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1050;
}

.modal-box {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  max-width: 500px;
  width: 90%;
  overflow: hidden;
  animation: fadeIn 0.3s ease-in-out;
  z-index: 1060;
}

.modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eaecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff3f3;
}

.btn-close {
  font-size: 1.5rem;
  background: none;
  border: none;
  color: #d32f2f;
  cursor: pointer;
}

.modal-body {
  padding: 1.5rem;
  text-align: center;
}

.event-name {
  font-weight: bold;
  font-size: 1.1rem;
  margin: 0.5rem 0;
  color: #d32f2f;
}

.event-desc {
  font-size: 0.95rem;
  color: #555;
  margin-top: 0.5rem;
}

.modal-footer {
  padding: 1rem;
  border-top: 1px solid #eaecef;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.btn-cancel,
.btn-confirm {
  padding: 0.5rem 1.2rem;
  font-size: 0.95rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-cancel {
  background-color: #e0e0e0;
  color: #333;
}

.btn-cancel:hover {
  background-color: #ccc;
}

.btn-confirm {
  background-color: #ef5350;
  color: white;
}

.btn-confirm:hover {
  background-color: #e53935;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.event-image {
  overflow: hidden;
  border-radius: 12px;
}
.event-link {
  color: #3498db;
  text-decoration: none;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}
.Events-container {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
  direction: rtl;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.header-actions {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-container {
  position: relative;
}

.search-input {
  padding: 0.5rem 2rem 0.5rem 1rem;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  width: 250px;
  font-size: 0.9rem;
}

.search-icon {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #777;
}

h2 {
  font-size: 1.8rem;
  color: #2c3e50;
  margin: 0;
}

.add-button {
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: background-color 0.3s;
}

.add-button:hover {
  background-color: #388e3c;
}

.add-button .icon {
  font-size: 1.2rem;
  margin-left: 0.3rem;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
}

.loading-spinner {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.Events-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.event-card {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s;
  width: 20rem;
  height: 20rem;
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.event-content {
  padding: 1.5rem;
  position: relative;
  flex-grow: 1;
  text-align: center;
}

.event-number {
  position: absolute;
  top: 0rem;
  right: 1rem;
  background-color: #f0f7ff;
  color: #3498db;
  height: 24px;
  width: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.75rem;
  font-weight: bold;
}

.event-text {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 1rem;
  color: #2c3e50;
}

.event-author {
  font-size: 0.9rem;
  color: #7f8c8d;
  text-align: left;
}

.event-actions {
  display: flex;
  justify-content: flex-end;
  padding: 0.75rem;
  background-color: #f8f9fa;
  border-top: 1px solid #eaecef;
}

.action-btn {
  border: none;
  background: none;
  height: 36px;
  width: 36px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.2s;
  margin-right: 0.5rem;
}

.action-btn.edit {
  color: #3498db;
}

.action-btn.delete {
  color: #e74c3c;
}

.action-btn:hover {
  background-color: rgba(0, 0, 0, 0.06);
}

.empty-state {
  padding: 3rem;
  text-align: center;
  background-color: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 2rem;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.empty-state p {
  margin-bottom: 1.5rem;
  color: #7f8c8d;
}

/* Pagination styles */
.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  margin-top: 2rem;
  padding: 1rem 0;
  border-top: 1px solid #eee;
  flex-wrap: wrap;
}

.pagination-button {
  padding: 8px 15px;
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-numbers {
  display: flex;
  gap: 5px;
}

.page-number {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  background-color: white;
}

.page-number.active {
  background-color: #3498db;
  color: white;
  border-color: #3498db;
}

.pagination-info {
  margin-right: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.items-per-page {
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.page-indicator {
  color: #777;
  font-size: 14px;
}

/* Modal styles */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  overflow-y: auto; /* Permet le scroll vertical */
}

.modal-container {
  background-color: white;
  border-radius: 12px;
  max-width: 600px;
  width: 90%;
  max-height: 90vh; /* Limite la hauteur */
  display: flex;
  flex-direction: column;
  overflow-y: hidden; /* On gère le scroll au niveau de .modal-body */
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
}

.modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eaecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-close {
  font-size: 1.5rem;
  background: none;
  border: none;
  cursor: pointer;
  color: #777;
}

.modal-body {
  padding: 1.5rem;
  overflow-y: auto; /* Scroll seulement ici si besoin */
  max-height: 60vh; /* Hauteur maximale avant le scroll */
}

.modal-actions {
  margin-top: 1.5rem;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #2c3e50;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-group textarea {
  min-height: 120px;
  resize: vertical;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
}

.cancel-btn {
  background-color: #f3f4f6;
  color: #4b5563;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cancel-btn:hover {
  background-color: #e5e7eb;
}

.confirm-btn {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-btn.delete {
  background-color: #e74c3c;
}

.confirm-btn:hover {
  opacity: 0.9;
}

.modal-warning {
  color: #e74c3c;
  font-size: 0.9rem;
  margin: 1rem 0;
  padding: 0.5rem;
  background-color: #ffebee;
  border-radius: 4px;
}

/* Alert styles */
.error-alert,
.success-alert {
  position: fixed;
  top: 20px;
  right: 20px;
  max-width: 400px;
  z-index: 1100;
  animation: slideIn 0.3s ease-out;
}

.error-alert {
  background-color: #ffebee;
  border-left: 4px solid #f44336;
}

.success-alert {
  background-color: #e8f5e9;
  border-left: 4px solid #4caf50;
}

.error-content,
.success-content {
  padding: 12px 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.error-icon,
.success-icon {
  font-size: 20px;
}

.error-message,
.success-message {
  flex: 1;
}

.error-close,
.success-close {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #777;
  padding: 0;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* Responsive styles */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
    flex-direction: column;
    gap: 1rem;
  }

  .search-container {
    width: 100%;
  }

  .search-input {
    width: 100%;
  }

  .add-button {
    width: 100%;
    justify-content: center;
  }

  .pagination-controls {
    flex-direction: column;
    gap: 1rem;
  }

  .pagination-info {
    width: 100%;
    justify-content: center;
    margin-right: 0;
  }
}
</style>