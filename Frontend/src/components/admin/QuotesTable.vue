<template>
  <div class="quotes-container">
    <div class="header">
      <h2>💬 الاقتباسات</h2>
      <button class="add-button" @click="showAddForm = true">
        <span class="icon">+</span> إضافة اقتباس جديد
      </button>
    </div>

    <div class="quotes-list" v-if="quotes.length > 0">
      <div v-for="(quote, index) in quotes" :key="quote.id" class="quote-card">
        <div class="quote-content">
          <div class="quote-number">{{ index + 1 }}</div>
          <div class="quote-text">"{{ quote.contenu }}"</div>
          <div class="quote-author">— {{ quote.nomAuteur }} —</div>
        </div>
        <div class="quote-actions">
          <button class="action-btn edit" @click="editQuote(quote)">
            <span class="icon">✏️</span>
          </button>
          <button class="action-btn delete" @click="confirmDelete(quote.id)">
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

    <!-- Modal for delete confirmation -->
    <div class="modal" v-if="showDeleteModal">
      <div class="modal-content">
        <h3>تأكيد الحذف</h3>
        <p>هل أنت متأكد من رغبتك في حذف هذا الاقتباس؟</p>
        <div class="modal-actions">
          <button class="cancel-btn" @click="showDeleteModal = false">
            إلغاء
          </button>
          <button class="confirm-btn" @click="deleteQuote(quoteToDelete)">
            تأكيد
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
            <button type="submit" class="confirm-btn">حفظ</button>
          </div>
        </form>
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
      showDeleteModal: false,
      quoteToDelete: null,
      showAddForm: false,
      editingQuote: null,
      currentQuote: {
        contenu: "",
        nomAuteur: "",
      },
    };
  },
  mounted() {
    this.fetchQuotes();
  },
  methods: {
    fetchQuotes() {
      quoteService
        .getAllQuotes()
        .then((response) => {
          this.quotes = response.data;
        })
        .catch((error) => {
          console.error(
            "Erreur lors de la récupération des citations :",
            error
          );
        });
    },

    confirmDelete(id) {
      this.quoteToDelete = id;
      this.showDeleteModal = true;
    },

    deleteQuote() {
      quoteService
        .deleteQuote(this.quoteToDelete)
        .then(() => {
          this.quotes = this.quotes.filter((q) => q.id !== this.quoteToDelete);
          this.showDeleteModal = false;
          this.quoteToDelete = null;
        })
        .catch((error) => {
          console.error("Erreur lors de la suppression :", error);
        });
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

    saveQuote() {
      if (this.editingQuote) {
        // 🔁 Mise à jour
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
          })
          .catch((error) => {
            console.error("Erreur lors de la mise à jour :", error);
          });
      } else {
        // ➕ Ajout
        quoteService
          .createQuote(this.currentQuote)
          .then((response) => {
            this.quotes.push(response.data);
            this.cancelEdit();
          })
          .catch((error) => {
            console.error("Erreur lors de la création :", error);
          });
      }
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

.quotes-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
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
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.empty-state p {
  margin-bottom: 1.5rem;
  color: #7f8c8d;
}

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

.confirm-btn:hover {
  background-color: #2980b9;
}
</style>