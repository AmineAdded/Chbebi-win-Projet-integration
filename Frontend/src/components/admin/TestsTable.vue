<template>
  <div class="tests-dashboard">
    <div class="dashboard-header">
      <h2><span class="icon">🧪</span> الاختبارات</h2>
      <div class="actions-group">
        <div class="search-container">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="بحث في الاختبارات..." 
            class="search-input"
          />
          <span class="search-icon">🔍</span>
        </div>
        <button class="add-button" @click="showAddTestModal = true">
          <span class="add-icon">+</span> إضافة اختبار
        </button>
      </div>
    </div>

    <div class="tests-list" v-if="filteredTests.length > 0">
      <div class="test-card" v-for="(test, index) in filteredTests" :key="test.id">
        <div class="test-info">
          <div class="test-number">{{ index + 1 }}</div>
          <div class="test-details">
            <h3 class="test-title">{{ test.title }}</h3>
            <div class="test-meta">آخر تحديث: {{ test.lastUpdated }}</div>
          </div>
        </div>
        
        <div class="test-score-container">
          <div class="test-score" :class="getScoreClass(test.score)">
            <span class="score-value">{{ test.score }}</span>
            <span class="score-total">/100</span>
          </div>
          <div class="score-label">{{ getScoreLabel(test.score) }}</div>
        </div>
        
        <div class="test-actions">
          <button class="action-button view" @click="viewTest(test)">
            <span class="action-icon">👁️</span>
            <span class="action-text">عرض</span>
          </button>
          <button class="action-button edit" @click="editTest(test)">
            <span class="action-icon">✏️</span>
            <span class="action-text">تعديل</span>
          </button>
          <button class="action-button delete" @click="confirmDeleteTest(test)">
            <span class="action-icon">🗑</span>
            <span class="action-text">حذف</span>
          </button>
        </div>
      </div>
    </div>
    
    <div class="empty-state" v-else>
      <div class="empty-icon">📝</div>
      <h3>لا توجد اختبارات</h3>
      <p>لم يتم العثور على أي اختبارات. يرجى إضافة اختبار جديد أو تغيير معايير البحث.</p>
      <button class="add-button" @click="showAddTestModal = true">إضافة اختبار جديد</button>
    </div>
    
    <!-- Modal for delete confirmation -->
    <div class="modal" v-if="showDeleteModal">
      <div class="modal-content">
        <h3>تأكيد الحذف</h3>
        <p>هل أنت متأكد من حذف "{{ testToDelete?.title }}"؟</p>
        <p class="modal-warning">هذا الإجراء لا يمكن التراجع عنه.</p>
        <div class="modal-actions">
          <button class="cancel-button" @click="cancelDelete">إلغاء</button>
          <button class="confirm-button delete" @click="deleteTest(testToDelete.id)">تأكيد الحذف</button>
        </div>
      </div>
    </div>
    
    <!-- Modal for adding a new test -->
    <div class="modal" v-if="showAddTestModal">
      <div class="modal-content">
        <h3>{{ editingTest ? 'تعديل الاختبار' : 'إضافة اختبار جديد' }}</h3>
        <form @submit.prevent="saveTest">
          <div class="form-group">
            <label for="testTitle">عنوان الاختبار</label>
            <input type="text" id="testTitle" v-model="currentTest.title" required>
          </div>
          <div class="form-group">
            <label for="testScore">النتيجة</label>
            <input type="number" id="testScore" v-model="currentTest.score" min="0" max="100" required>
          </div>
          <div class="modal-actions">
            <button type="button" class="cancel-button" @click="cancelAddEdit">إلغاء</button>
            <button type="submit" class="confirm-button">حفظ</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.tests-dashboard {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  direction: rtl;
  padding: 1.5rem;
  max-width: 1100px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.dashboard-header h2 {
  margin: 0;
  font-size: 1.75rem;
  color: #2c3e50;
  display: flex;
  align-items: center;
}

.icon {
  margin-left: 0.5rem;
}

.actions-group {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-container {
  position: relative;
}

.search-input {
  padding: 0.6rem 2.5rem 0.6rem 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 0.9rem;
  width: 220px;
  transition: all 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.search-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #94a3b8;
}

.add-button {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: background-color 0.3s;
}

.add-button:hover {
  background-color: #2980b9;
}

.add-icon {
  margin-left: 0.5rem;
  font-size: 1.1rem;
}

.tests-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.test-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.3s ease;
}

.test-card:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.test-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.test-number {
  background-color: #f1f5f9;
  color: #334155;
  height: 2rem;
  width: 2rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.9rem;
}

.test-details {
  display: flex;
  flex-direction: column;
}

.test-title {
  margin: 0 0 0.25rem 0;
  font-size: 1.1rem;
  color: #1e293b;
}

.test-meta {
  font-size: 0.8rem;
  color: #64748b;
}

.test-score-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 2rem;
}

.test-score {
  display: flex;
  align-items: baseline;
  font-weight: bold;
}

.score-value {
  font-size: 1.5rem;
}

.score-total {
  font-size: 0.9rem;
  opacity: 0.7;
  margin-right: 2px;
}

.score-label {
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

.score-excellent {
  color: #10b981;
}

.score-good {
  color: #3b82f6;
}

.score-average {
  color: #f59e0b;
}

.score-poor {
  color: #ef4444;
}

.test-actions {
  display: flex;
  gap: 0.5rem;
}

.action-button {
  background: none;
  border: none;
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.action-button.view {
  color: #3b82f6;
}

.action-button.edit {
  color: #f59e0b;
}

.action-button.delete {
  color: #ef4444;
}

.action-button:hover {
  background-color: #f8fafc;
}

.action-icon {
  margin-left: 0.3rem;
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  background-color: #f8fafc;
  border-radius: 12px;
  margin-top: 2rem;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: #94a3b8;
}

.empty-state h3 {
  margin: 0 0 0.5rem 0;
  color: #334155;
}

.empty-state p {
  color: #64748b;
  margin-bottom: 1.5rem;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
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
  z-index: 100;
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: #1e293b;
}

.modal-warning {
  color: #ef4444;
  font-size: 0.9rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.cancel-button {
  background-color: #f1f5f9;
  color: #334155;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cancel-button:hover {
  background-color: #e2e8f0;
}

.confirm-button {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-button:hover {
  background-color: #2980b9;
}

.confirm-button.delete {
  background-color: #ef4444;
}

.confirm-button.delete:hover {
  background-color: #dc2626;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #334155;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
}

.form-group input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

@media (max-width: 768px) {
  .dashboard-header {
    flex-direction: column;
    align-items: stretch;
  }
  
  .actions-group {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input {
    width: 100%;
  }
  
  .test-card {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
  }
  
  .test-score-container {
    margin: 0.5rem 0;
  }
  
  .test-actions {
    justify-content: space-around;
  }
}
</style>

<script>
export default {
  data() {
    return {
      tests: [
        { 
          id: 1, 
          title: "اختبار التحليل المنطقي", 
          score: 85,
          lastUpdated: "15 أبريل 2025" 
        },
        { 
          id: 2, 
          title: "اختبار الشخصية", 
          score: 92,
          lastUpdated: "10 أبريل 2025" 
        },
        { 
          id: 3, 
          title: "اختبار المهارات التقنية", 
          score: 76,
          lastUpdated: "5 أبريل 2025" 
        },
        { 
          id: 4, 
          title: "اختبار القدرات اللغوية", 
          score: 65,
          lastUpdated: "28 مارس 2025" 
        }
      ],
      searchQuery: "",
      showDeleteModal: false,
      testToDelete: null,
      showAddTestModal: false,
      editingTest: null,
      currentTest: {
        title: "",
        score: 0
      }
    };
  },
  computed: {
    filteredTests() {
      if (!this.searchQuery) return this.tests;
      
      const query = this.searchQuery.toLowerCase();
      return this.tests.filter(test => 
        test.title.toLowerCase().includes(query)
      );
    }
  },
  methods: {
    getScoreClass(score) {
      if (score >= 90) return "score-excellent";
      if (score >= 75) return "score-good";
      if (score >= 60) return "score-average";
      return "score-poor";
    },
    getScoreLabel(score) {
      if (score >= 90) return "ممتاز";
      if (score >= 75) return "جيد";
      if (score >= 60) return "متوسط";
      return "ضعيف";
    },
    viewTest(test) {
      console.log("عرض الاختبار:", test);
      // Implementation would go here
    },
    editTest(test) {
      this.editingTest = test;
      this.currentTest = { ...test };
      this.showAddTestModal = true;
    },
    confirmDeleteTest(test) {
      this.testToDelete = test;
      this.showDeleteModal = true;
    },
    cancelDelete() {
      this.showDeleteModal = false;
      this.testToDelete = null;
    },
    deleteTest(id) {
      this.tests = this.tests.filter(t => t.id !== id);
      this.showDeleteModal = false;
      this.testToDelete = null;
    },
    cancelAddEdit() {
      this.showAddTestModal = false;
      this.editingTest = null;
      this.currentTest = {
        title: "",
        score: 0
      };
    },
    saveTest() {
      const today = new Date();
      const formattedDate = `${today.getDate()} ${this.getArabicMonth(today.getMonth())} ${today.getFullYear()}`;
      
      if (this.editingTest) {
        // Update existing test
        const index = this.tests.findIndex(t => t.id === this.editingTest.id);
        if (index !== -1) {
          this.tests[index] = {
            ...this.currentTest,
            lastUpdated: formattedDate
          };
        }
      } else {
        // Add new test
        const newId = Math.max(0, ...this.tests.map(t => t.id)) + 1;
        this.tests.push({
          id: newId,
          title: this.currentTest.title,
          score: this.currentTest.score,
          lastUpdated: formattedDate
        });
      }
      
      this.cancelAddEdit();
    },
    getArabicMonth(monthIndex) {
      const arabicMonths = [
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
      ];
      return arabicMonths[monthIndex];
    }
  }
};
</script>