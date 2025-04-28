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
      <div class="test-container" v-for="(test, index) in filteredTests" :key="test.id">
        <div class="test-card">
          <div class="test-info">
            <div class="test-number">{{ index + 1 }}</div>
            <div class="test-details">
              <h3 class="test-title">{{ test.title }}</h3>
              <div class="test-meta">
                <span>آخر تحديث: {{ test.lastUpdated }}</span>
              </div>
            </div>
          </div>
          
          <div class="test-questions-container">
            <div class="test-questions-count">
              <span class="questions-value">{{ test.questions ? test.questions.length : 0 }}</span>
              <span class="questions-label">أسئلة</span>
            </div>
          </div>
          
          <div class="test-actions">
            <button class="action-button view" @click="toggleViewQuestions(test)">
              <span class="action-icon">👁️</span>
              <span class="action-text">عرض</span>
            </button>
            <button class="action-button add-question" @click="addQuestionToTest(test)">
              <span class="action-icon">➕</span>
              <span class="action-text">إضافة سؤال</span>
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
        
        <!-- Questions List -->
        <div class="questions-list" v-if="test.showQuestions && test.questions && test.questions.length > 0">
          <div class="question-container" v-for="(question, qIndex) in test.questions" :key="question.id">
            <div class="question-card">
              <div class="question-info">
                <div class="question-number">{{ qIndex + 1 }}</div>
                <div class="question-text">{{ question.text }}</div>
              </div>
              <div class="question-actions">
                <button class="action-button view" @click="toggleViewResponses(test, question)">
                  <span class="action-icon">👁️</span>
                  <span class="action-text">عرض</span>
                </button>
                <button class="action-button edit" @click="editQuestion(test, question)">
                  <span class="action-icon">✏️</span>
                  <span class="action-text">تعديل</span>
                </button>
                <button class="action-button delete" @click="deleteQuestion(test, question)">
                  <span class="action-icon">🗑</span>
                  <span class="action-text">حذف</span>
                </button>
              </div>
            </div>
            
            <!-- Responses List -->
            <div class="responses-list" v-if="question.showResponses && question.responses && question.responses.length > 0">
              <div class="response-card" v-for="(response, rIndex) in question.responses" :key="rIndex">
                <div class="response-text">{{ response.text }}</div>
              </div>
            </div>
            <div class="empty-responses" v-else-if="question.showResponses">
              لا توجد إجابات متاحة لهذا السؤال
            </div>
          </div>
        </div>
        <div class="empty-questions" v-else-if="test.showQuestions">
          لا توجد أسئلة في هذا الاختبار
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
    
    <!-- Modal for adding/editing a test -->
    <div class="modal" v-if="showAddTestModal">
      <div class="modal-content test-modal">
        <h3>{{ editingTest ? 'تعديل الاختبار' : 'إضافة اختبار جديد' }}</h3>
        <form @submit.prevent="saveTest">
          <div class="form-group">
            <label for="testTitle">عنوان الاختبار</label>
            <input type="text" id="testTitle" v-model="currentTest.title" required>
          </div>
          <div class="modal-actions">
            <button type="button" class="cancel-button" @click="cancelAddEdit">إلغاء</button>
            <button type="submit" class="confirm-button">حفظ</button>
          </div>
        </form>
      </div>
    </div>
    
    <!-- Modal for adding/editing a question -->
    <div class="modal" v-if="showQuestionModal">
      <div class="modal-content question-modal">
        <h3>{{ editingQuestion ? 'تعديل السؤال' : 'إضافة سؤال جديد' }}</h3>
        <form @submit.prevent="saveQuestion">
          <div class="form-group">
            <label for="questionText">نص السؤال</label>
            <input type="text" id="questionText" v-model="currentQuestion.text" required>
          </div>
          
          <div class="responses-section">
            <h4>الإجابات</h4>
            <div class="form-group" v-for="(response, index) in currentQuestion.responses" :key="index">
              <label>إجابة {{ index + 1 }}</label>
              <input type="text" v-model="currentQuestion.responses[index].text" required>
            </div>
          </div>
          
          <div class="modal-actions">
            <button type="button" class="cancel-button" @click="cancelQuestionEdit">إلغاء</button>
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

.test-container {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
}

.test-container:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.08);
}

.test-card {
  padding: 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f1f5f9;
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
  display: flex;
  gap: 1rem;
}

.test-questions-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 2rem;
}

.test-questions-count {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #3b82f6;
}

.questions-value {
  font-size: 1.5rem;
  font-weight: bold;
}

.questions-label {
  font-size: 0.8rem;
  margin-top: 0.25rem;
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

.action-button.add-question {
  color: #10b981;
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

/* Questions styling */
.questions-list {
  padding: 0.5rem 1rem 1.5rem;
  background-color: #f8fafc;
}

.question-container {
  margin-top: 1rem;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);
}

.question-card {
  background-color: white;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f1f5f9;
}

.question-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
}

.question-number {
  background-color: #e0f2fe;
  color: #0369a1;
  height: 1.5rem;
  width: 1.5rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 0.8rem;
}

.question-text {
  font-size: 1rem;
  color: #334155;
}

.question-actions {
  display: flex;
  gap: 0.5rem;
}

/* Responses styling */
.responses-list {
  padding: 0.5rem 1rem 1rem;
  background-color: #f0f9ff;
}

.response-card {
  background-color: white;
  padding: 0.75rem 1rem;
  margin-top: 0.5rem;
  border-radius: 6px;
  display: flex;
  align-items: center;
  border: 1px solid #e2e8f0;
}

.response-text {
  font-size: 0.95rem;
  color: #334155;
  width: 100%;
}

.empty-questions, .empty-responses {
  padding: 1.5rem;
  text-align: center;
  color: #64748b;
  background-color: #f8fafc;
  font-size: 0.9rem;
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

.modal-content.question-modal {
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 1.5rem;
  color: #1e293b;
}

.modal-content h4 {
  margin: 1.5rem 0 1rem;
  color: #334155;
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

.responses-section {
  background-color: #f8fafc;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
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
  
  .test-questions-container {
    margin: 0.5rem 0;
  }
  
  .test-actions {
    justify-content: space-around;
  }
  
  .question-card {
    flex-direction: column;
    align-items: stretch;
    gap: 0.75rem;
  }
  
  .question-actions {
    justify-content: flex-end;
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
          lastUpdated: "15 أبريل 2025",
          showQuestions: false,
          questions: [
            {
              id: 101,
              text: "ما هو الاستنتاج المنطقي لهذه المعطيات؟",
              showResponses: false,
              responses: [
                { text: "الخيار الأول" },
                { text: "الخيار الثاني" },
                { text: "الخيار الثالث" },
                { text: "الخيار الرابع" }
              ]
            },
            {
              id: 102,
              text: "اختر المسار الصحيح لحل المشكلة المعطاة",
              showResponses: false,
              responses: [
                { text: "الخيار الأول" },
                { text: "الخيار الثاني" },
                { text: "الخيار الثالث" },
                { text: "الخيار الرابع" }
              ]
            },
            {
              id: 103,
              text: "ما هي الفرضية المناسبة في هذه الحالة؟",
              showResponses: false,
              responses: [
                { text: "الخيار الأول" },
                { text: "الخيار الثاني" },
                { text: "الخيار الثالث" },
                { text: "الخيار الرابع" }
              ]
            }
          ]
        },
        { 
          id: 2, 
          title: "اختبار الشخصية", 
          lastUpdated: "10 أبريل 2025",
          showQuestions: false,
          questions: [
            {
              id: 201,
              text: "كيف تتصرف عند مواجهة موقف صعب؟",
              showResponses: false,
              responses: [
                { text: "أواجه المشكلة مباشرة" },
                { text: "أطلب المساعدة" },
                { text: "أفكر بعمق قبل اتخاذ القرار" },
                { text: "أؤجل اتخاذ القرار" }
              ]
            },
            {
              id: 202,
              text: "ماذا تفضل في أوقات فراغك؟",
              showResponses: false,
              responses: [
                { text: "القراءة" },
                { text: "مشاهدة الأفلام" },
                { text: "ممارسة الرياضة" },
                { text: "قضاء الوقت مع الأصدقاء" }
              ]
            }
          ]
        },
        { 
          id: 3, 
          title: "اختبار المهارات التقنية", 
          lastUpdated: "5 أبريل 2025",
          showQuestions: false,
          questions: [
            {
              id: 301,
              text: "ما هي لغة البرمجة المناسبة لتطوير تطبيقات الويب؟",
              showResponses: false,
              responses: [
                { text: "JavaScript" },
                { text: "C++" },
                { text: "Swift" },
                { text: "Matlab" }
              ]
            },
            {
              id: 302,
              text: "ما هو البروتوكول المستخدم لنقل صفحات الويب؟",
              showResponses: false,
              responses: [
                { text: "FTP" },
                { text: "HTTP" },
                { text: "SMTP" },
                { text: "SSH" }
              ]
            },
            {
              id: 303,
              text: "أي من التالي يعتبر قاعدة بيانات علائقية؟",
              showResponses: false,
              responses: [
                { text: "MongoDB" },
                { text: "Redis" },
                { text: "MySQL" },
                { text: "Firebase" }
              ]
            },
            {
              id: 304,
              text: "ما هو مصطلح API؟",
              showResponses: false,
              responses: [
                { text: "واجهة برمجة التطبيقات" },
                { text: "بروتوكول إنترنت متقدم" },
                { text: "مؤشر أداء التطبيق" },
                { text: "واجهة اتصال متقدمة" }
              ]
            }
          ]
        },
        { 
          id: 4, 
          title: "اختبار القدرات اللغوية", 
          lastUpdated: "28 مارس 2025",
          showQuestions: false,
          questions: [
            {
              id: 401,
              text: "اختر الكلمة الصحيحة إملائياً",
              showResponses: false,
              responses: [
                { text: "إستقبال" },
                { text: "استقبال" },
                { text: "أستقبال" },
                { text: "إستقبل" }
              ]
            },
            {
              id: 402,
              text: "ما هو جمع كلمة 'معلومة'؟",
              showResponses: false,
              responses: [
                { text: "معلومات" },
                { text: "معالم" },
                { text: "معلمات" },
                { text: "معاليم" }
              ]
            }
          ]
        }
      ],
      searchQuery: "",
      showDeleteModal: false,
      testToDelete: null,
      showAddTestModal: false,
      editingTest: null,
      currentTest: {
        title: "",
        questions: []
      },
      showQuestionModal: false,
      editingQuestion: null,
      currentQuestion: {
        text: "",
        responses: [
          { text: "" },
          { text: "" },
          { text: "" },
          { text: "" }
        ]
      },
      currentTestForQuestion: null
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
    toggleViewQuestions(test) {
      const index = this.tests.findIndex(t => t.id === test.id);
      if (index !== -1) {
        // Create a new copy of the test with updated showQuestions
        const updatedTest = { ...test, showQuestions: !test.showQuestions };
        
        // Replace the old test with the updated one
        this.tests.splice(index, 1, updatedTest);
        
        // If questions are being hidden, also hide all responses
        if (!updatedTest.showQuestions && updatedTest.questions) {
          // Make a copy of all questions with responses hidden
          const updatedQuestions = updatedTest.questions.map(q => {
            return { ...q, showResponses: false };
          });
          
          // Update the questions in the test
          this.tests[index].questions = updatedQuestions;
        }
      }
    },
    toggleViewResponses(test, question) {
      const testIndex = this.tests.findIndex(t => t.id === test.id);
      if (testIndex !== -1) {
        const questionIndex = this.tests[testIndex].questions.findIndex(q => q.id === question.id);
        if (questionIndex !== -1) {
          // Create a copy of the questions array
          const updatedQuestions = [...this.tests[testIndex].questions];
          
          // Create an updated question with toggled showResponses
          const updatedQuestion = {
            ...updatedQuestions[questionIndex],
            showResponses: !updatedQuestions[questionIndex].showResponses
          };
          
          // Replace the old question with the updated one
          updatedQuestions.splice(questionIndex, 1, updatedQuestion);
          
          // Update the questions array in the test
          this.tests[testIndex].questions = updatedQuestions;
        }
      }
    },
    editTest(test) {
      this.editingTest = test;
      this.currentTest = { 
        id: test.id,
        title: test.title,
        questions: [...test.questions]
      };
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
        questions: []
      };
    },
    saveTest() {
      const today = new Date();
      const formattedDate = `${today.getDate()} ${this.getArabicMonth(today.getMonth())} ${today.getFullYear()}`;
      
      if (this.editingTest) {
        // Update existing test
        const index = this.tests.findIndex(t => t.id === this.editingTest.id);
        if (index !== -1) {
          // Create a new object with the updated values
          const updatedTest = {
            ...this.editingTest,
            title: this.currentTest.title,
            lastUpdated: formattedDate
          };
          
          // Replace the old test with the updated one
          this.tests.splice(index, 1, updatedTest);
        }
      } else {
        // Add new test
        const newId = Math.max(0, ...this.tests.map(t => t.id)) + 1;
        const newTest = {
          id: newId,
          title: this.currentTest.title,
          lastUpdated: formattedDate,
          showQuestions: false,
          questions: [] // Initialize with empty questions array
        };
        
        this.tests.push(newTest);
      }
      
      this.cancelAddEdit();
    },
    
    // New methods for question management
    addQuestionToTest(test) {
      this.currentTestForQuestion = test;
      this.editingQuestion = null;
      this.resetCurrentQuestion();
      this.showQuestionModal = true;
    },
    
    editQuestion(test, question) {
      this.currentTestForQuestion = test;
      this.editingQuestion = question;
      
      // Create a deep copy of the question
      this.currentQuestion = {
        id: question.id,
        text: question.text,
        responses: question.responses.map(r => ({ text: r.text }))
      };
      
      // Ensure we have exactly 4 responses
      while (this.currentQuestion.responses.length < 4) {
        this.currentQuestion.responses.push({ text: "" });
      }
      
      this.showQuestionModal = true;
    },
    
    deleteQuestion(test, question) {
      const testIndex = this.tests.findIndex(t => t.id === test.id);
      if (testIndex !== -1) {
        // Create a new array of questions without the one to delete
        const updatedQuestions = this.tests[testIndex].questions.filter(q => q.id !== question.id);
        
        // Update the test with the new questions array
        const updatedTest = { ...this.tests[testIndex], questions: updatedQuestions };
        this.tests.splice(testIndex, 1, updatedTest);
      }
    },
    
    cancelQuestionEdit() {
      this.showQuestionModal = false;
      this.editingQuestion = null;
      this.currentTestForQuestion = null;
      this.resetCurrentQuestion();
    },
    
    resetCurrentQuestion() {
      this.currentQuestion = {
        text: "",
        responses: [
          { text: "" },
          { text: "" },
          { text: "" },
          { text: "" }
        ]
      };
    },
    
    saveQuestion() {
      const testIndex = this.tests.findIndex(t => t.id === this.currentTestForQuestion.id);
      if (testIndex === -1) return;
      
      if (this.editingQuestion) {
        // Update existing question
        const questionIndex = this.tests[testIndex].questions.findIndex(q => q.id === this.editingQuestion.id);
        if (questionIndex !== -1) {
          // Create updated questions array
          const updatedQuestions = [...this.tests[testIndex].questions];
          
          // Update the specific question
          updatedQuestions[questionIndex] = {
            ...this.editingQuestion,
            text: this.currentQuestion.text,
            responses: this.currentQuestion.responses.map(r => ({ text: r.text }))
          };
          
          // Update the test with the new questions array
          const updatedTest = { ...this.tests[testIndex], questions: updatedQuestions };
          this.tests.splice(testIndex, 1, updatedTest);
        }
      } else {
        // Add new question
        const newId = this.getNewQuestionId();
        const newQuestion = {
          id: newId,
          text: this.currentQuestion.text,
          showResponses: false,
          responses: this.currentQuestion.responses.map(r => ({ text: r.text }))
        };
        
        // Create a new array of questions with the new one
        const updatedQuestions = [...this.tests[testIndex].questions, newQuestion];
        
        // Update the test with the new questions array
        const updatedTest = { ...this.tests[testIndex], questions: updatedQuestions };
        this.tests.splice(testIndex, 1, updatedTest);
      }
      
      this.cancelQuestionEdit();
    },
    
    getNewQuestionId() {
      // Find the highest question ID across all tests and add 1
      let maxId = 0;
      this.tests.forEach(test => {
        if (test.questions && test.questions.length > 0) {
          const highestId = Math.max(...test.questions.map(q => q.id));
          maxId = Math.max(maxId, highestId);
        }
      });
      return maxId + 1;
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