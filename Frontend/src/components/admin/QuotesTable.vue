<template>
  <div class="quotes-container">
    <div class="header">
      <h2>💬 الاقتباسات</h2>
      <div class="header-actions">
        <div class="search-container">
          <input type="text" v-model="searchQuery" placeholder="بحث في الاقتباسات..." class="search-input" />
          <span class="search-icon">🔍</span>
        </div>
        <button class="add-button" @click="showAddForm = true">
          <span class="icon">+</span> إضافة اقتباس جديد
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>جاري تحميل البيانات...</p>
    </div>

    <div v-else>
      <div class="quotes-list" v-if="paginatedQuotes.length > 0">
        <div v-for="(quote, index) in paginatedQuotes" :key="quote.id" class="quote-card">
          <div class="quote-content">
            <div class="quote-number">{{ startIndex + index + 1 }}</div>
            <div class="quote-text">"{{ quote.contenu }}"</div>
            <div class="quote-author">— {{ quote.nomAuteur }} —</div>
          </div>
          <div class="quote-actions">
            <button class="action-btn edit" @click="editQuote(quote)">
              <span class="icon">✏️</span>
            </button>
            <button class="action-btn delete" @click="confirmDelete(quote)">
              <span class="icon">🗑</span>
            </button>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <div class="empty-icon">📝</div>
        <p>لا توجد اقتباسات حاليًا</p>
        <button class="add-button" @click="showAddForm = true">
          إضافة اقتباس جديد
        </button>
      </div>

      <!-- Pagination Controls -->
      <div class="pagination-controls" v-if="filteredQuotes.length > itemsPerPage">
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
          <select v-model="itemsPerPage" @change="handleItemsPerPageChange" class="items-per-page">
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

    <!-- Modal for delete confirmation -->
    <div class="modal1" v-if="showDeleteModal">
      <div class="modal-content">
        <h3>تأكيد الحذف</h3>
        <p>هل أنت متأكد من رغبتك في حذف هذا الاقتباس؟</p>
        <p class="modal-warning">"{{ quoteToDelete?.contenu }}"</p>
        <div class="modal-actions">
          <button class="cancel-btn" @click="showDeleteModal = false">
            إلغاء
          </button>
          <button class="confirm-btn delete" @click="deleteQuote(quoteToDelete)">
            تأكيد الحذف
          </button>
        </div>
      </div>
    </div>

    <!-- Form for adding/editing quote -->
    <div class="modal" v-if="showAddForm || editingQuote">
      <div class="modal-content">
        <h3>{{ editingQuote ? "تعديل الاقتباس" : "إضافة اقتباس جديد" }}</h3>
        <form @submit.prevent="saveQuote">
          <div class="form-group">
            <label for="quoteText">الاقتباس</label>
            <textarea
              id="quoteText"
              v-model="currentQuote.contenu"
              required
            ></textarea>
          </div>
          <div class="form-group">
            <label for="authorName">المؤلف</label>
            <input
              type="text"
              id="authorName"
              v-model="currentQuote.nomAuteur"
              required
            />
          </div>
          <div class="modal-actions">
            <button type="button" class="cancel-btn" @click="cancelEdit">
              إلغاء
            </button>
            <button type="submit" class="confirm-btn">{{ editingQuote ? 'حفظ التعديلات' : 'إضافة' }}</button>
          </div>
        </form>
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
import quoteService from "@/Services/QuoteService";

export default {
  data() {
    return {
      quotes: [],
      searchQuery: "",
      loading: false,
      error: null,
      successMessage: null,
      showDeleteModal: false,
      quoteToDelete: null,
      showAddForm: false,
      editingQuote: null,
      currentQuote: {
        contenu: "",
        nomAuteur: "",
      },
      // Pagination variables
      currentPage: 1,
      itemsPerPage: 10,
      maxPageButtons: 5
    };
  },
  computed: {
    filteredQuotes() {
      if (!this.searchQuery) return this.quotes;
      
      const query = this.searchQuery.toLowerCase();
      return this.quotes.filter(quote => 
        quote.contenu.toLowerCase().includes(query) || 
        quote.nomAuteur.toLowerCase().includes(query)
      );
    },
    totalPages() {
      return Math.ceil(this.filteredQuotes.length / this.itemsPerPage);
    },
    paginatedQuotes() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredQuotes.slice(start, end);
    },
    startIndex() {
      return (this.currentPage - 1) * this.itemsPerPage;
    },
    displayedPageNumbers() {
      const halfButtons = Math.floor(this.maxPageButtons / 2);
      let startPage = Math.max(1, this.currentPage - halfButtons);
      let endPage = Math.min(this.totalPages, startPage + this.maxPageButtons - 1);
      
      // Adjust if we're near the end
      if (endPage - startPage + 1 < this.maxPageButtons) {
        startPage = Math.max(1, endPage - this.maxPageButtons + 1);
      }
      
      const pages = [];
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      return pages;
    }
  },
  watch: {
    searchQuery() {
      this.currentPage = 1;
    }
  },
  mounted() {
    this.fetchQuotes();
  },
  methods: {
    // Pagination methods
    goToPage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },
    handleItemsPerPageChange() {
      this.currentPage = 1;
    },
    
    // Quote CRUD methods
    fetchQuotes() {
      this.loading = true;
      quoteService
        .getAllQuotes()
        .then((response) => {
          this.quotes = response.data;
        })
        .catch((error) => {
          console.error("Erreur lors de la récupération des citations :", error);
          this.error = "فشل في تحميل الاقتباسات";
        })
        .finally(() => {
          this.loading = false;
        });
    },
    confirmDelete(quote) {
      this.quoteToDelete = quote;
      this.showDeleteModal = true;
    },
    showSuccessMessage(message) {
      this.successMessage = message;
      // Disparaît après 3 secondes (3000 millisecondes)
      setTimeout(() => {
        this.successMessage = null;
      }, 3000);
    },
    
    deleteQuote() {
      this.loading = true;
      quoteService
        .deleteQuote(this.quoteToDelete.id)
        .then(() => {
          this.quotes = this.quotes.filter((q) => q.id !== this.quoteToDelete.id);
          this.showDeleteModal = false;
          this.showSuccessMessage("تم حذف الاقتباس بنجاح");
          
          if (this.paginatedQuotes.length === 0 && this.currentPage > 1) {
            this.currentPage = this.currentPage - 1;
          }
        })
        .catch((error) => {
          console.error("Erreur lors de la suppression :", error);
          this.error = "فشل في حذف الاقتباس";
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    saveQuote() {
      this.loading = true;
      if (this.editingQuote) {
        quoteService
          .updateQuote(this.editingQuote.id, this.currentQuote)
          .then((response) => {
            const index = this.quotes.findIndex(
              (q) => q.id === this.editingQuote.id
            );
            if (index !== -1) {
              this.quotes[index] = response.data;
            }
            this.cancelEdit();
            this.showSuccessMessage("تم تحديث الاقتباس بنجاح");
          })
          .catch((error) => {
            console.error("Erreur lors de la mise à jour :", error);
            this.error = "فشل في تحديث الاقتباس";
          })
          .finally(() => {
            this.loading = false;
          });
      } else {
        quoteService
          .createQuote(this.currentQuote)
          .then((response) => {
            this.quotes.unshift(response.data);
            this.cancelEdit();
            this.showSuccessMessage("تم إضافة الاقتباس بنجاح");
            this.currentPage = 1;
          })
          .catch((error) => {
            console.error("Erreur lors de la création :", error);
            this.error = "فشل في إضافة الاقتباس";
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    
    editQuote(quote) {
      this.editingQuote = quote;
      this.currentQuote = {
        contenu: quote.contenu,
        nomAuteur: quote.nomAuteur,
      };
      this.showAddForm = true;
    },
    cancelEdit() {
      this.showAddForm = false;
      this.editingQuote = null;
      this.currentQuote = {
        contenu: "",
        nomAuteur: "",
      };
    },
  },
};
</script>

<style scoped>
.quotes-container {
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
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.quotes-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.quote-card {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s;
}

.quote-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.quote-content {
  padding: 1.5rem;
  position: relative;
  flex-grow: 1;
}

.quote-number {
  position: absolute;
  top: 1rem;
  left: 1rem;
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

.quote-text {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 1rem;
  color: #2c3e50;
}

.quote-author {
  font-size: 0.9rem;
  color: #7f8c8d;
  text-align: left;
}

.quote-actions {
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
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
}

.modal h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: #2c3e50;
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
.error-alert, .success-alert {
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
  border-left: 4px solid #4CAF50;
}

.error-content, .success-content {
  padding: 12px 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.error-icon, .success-icon {
  font-size: 20px;
}

.error-message, .success-message {
  flex: 1;
}

.error-close, .success-close {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #777;
  padding: 0;
}

@keyframes slideIn {
  from { transform: translateX(100%); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
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