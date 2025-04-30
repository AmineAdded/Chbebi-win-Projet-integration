<template>
  <div class="tests-dashboard">
    <div class="dashboard-header">
      <h2><span class="icon">🧪</span> الاختبارات</h2>
      <div class="actions-group">
        <div class="search-container">
          <input type="text" v-model="searchQuery" placeholder="بحث في الاختبارات..." class="search-input" />
          <span class="search-icon">🔍</span>
        </div>
        <button class="add-button" @click="showAddTestModal = true">
          <span class="add-icon">+</span> إضافة اختبار
        </button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>جاري تحميل البيانات...</p>
    </div>

    <div class="tests-list" v-else-if="filteredTests.length > 0">
      <div class="test-container" v-for="(test, index) in filteredTests" :key="test.id">
        <div class="test-card">
          <div class="test-info">
            <div class="test-number">{{ index + 1 }}</div>
            <div class="test-details">
              <h3 class="test-title">{{ test.title || test.nom_test }}</h3>
              <div class="test-meta">
                <span>آخر تحديث: {{ test.lastUpdated }}</span>
                <span v-if="test.type_test">النوع: {{ test.type_test }}</span>
                <span>متاح للاستخدام: {{ test.utilisable ? 'نعم' : 'لا' }}</span>
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
            <div class="responses-list"
              v-if="question.showResponses && question.responses && question.responses.length > 0">
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
        <p>هل أنت متأكد من حذف "{{ testToDelete?.title || testToDelete?.nom_test }}"؟</p>
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
          <div class="form-group">
            <label for="testType">نوع الاختبار</label>
            <input type="text" id="testType" v-model="currentTest.type_test">
          </div>
          <div class="form-group checkbox-group">
            <label>
              <input type="checkbox" v-model="currentTest.utilisable" @change="handleUtilisableChange">
              متاح للاستخدام
            </label>
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

    <!-- Personnalité Test Alert Modal -->
    <div class="modal" v-if="showPersonnaliteAlert">
      <div class="modal-content alert-modal">
        <h3>تنبيه: اختبار الشخصية</h3>
        <p>يمكن أن يكون هناك اختبار شخصية واحد فقط متاح للاستخدام في النظام.</p>
        <p>إذا قمت بتفعيل هذا الاختبار، سيتم إلغاء تفعيل الاختبار الآخر من نوع "personnalité" تلقائيًا.</p>
        <p>هل تريد المتابعة؟</p>
        <div class="modal-actions">
          <button class="cancel-button" @click="cancelPersonnaliteAlert">إلغاء</button>
          <button class="confirm-button" @click="confirmPersonnaliteAlert">متابعة</button>
        </div>
      </div>
    </div>

    <!-- Error Alert -->
    <div class="error-alert" v-if="error">
      <div class="error-content">
        <span class="error-icon">⚠️</span>
        <span class="error-message">{{ error }}</span>
        <button class="error-close" @click="error = null">×</button>
      </div>
    </div>

    <!-- Success Alert -->
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
import QuestionService from '@/Services/QuestionService';
import TestService from '@/Services/TestService';
import ResponseService from '@/Services/ResponseService';

export default {
  data() {
    return {
      tests: [],
      searchQuery: "",
      showDeleteModal: false,
      testToDelete: null,
      showAddTestModal: false,
      editingTest: null,
      currentTest: {
        title: "",
        type_test: "",
        utilisable: true,
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
      currentTestForQuestion: null,
      loading: false,
      error: null,
      successMessage: null,
      showPersonnaliteAlert: false,
      personnaliteTestBackup: null
    };
  },
  computed: {
    filteredTests() {
      if (!this.searchQuery) return this.tests;

      const query = this.searchQuery.toLowerCase();
      return this.tests.filter(test =>
        (test.title ? test.title.toLowerCase().includes(query) : false) ||
        (test.nomTest ? test.nomTest.toLowerCase().includes(query) : false)
      );
    }
  },
  async created() {
    try {
      await this.fetchTests();
    } catch (error) {
      this.error = "فشل في تحميل الاختبارات";
      console.error("Error loading tests:", error);
    }
  },
  methods: {
    async fetchTests() {
      try {
        this.loading = true;
        const tests = await TestService.getAllTests();
        console.log(`Fetched ${tests.length} tests:`, tests);

        this.tests = tests.map(test => ({
          ...test,
          showQuestions: false,
          title: test.nomTest,
          type_test: test.typeTest,
          utilisable: test.utilisable === 1,
          lastUpdated: this.formatDate(new Date())
        }));
      } catch (error) {
        console.error("Error fetching tests:", error);
        this.error = "  فشل في تحميل الاختبارات";
      } finally {
        this.loading = false;
      }
    },

    async toggleViewQuestions(test) {
      const index = this.tests.findIndex(t => t.id === test.id);
      if (index !== -1) {
        const updatedTest = { ...test, showQuestions: !test.showQuestions };

        try {
          this.loading = true;

          const questions = await QuestionService.getAllQuestions(test);
          console.log(`Fetched ${questions.length} questions:`, questions);

          updatedTest.questions = questions.map(q => ({
            ...q,
            showResponses: false,
            text: q.contenu,
            responses: q.reponses ? q.reponses.map(r => ({
              ...r,
              text: r.contenu
            })) : []
          }));
        } catch (error) {
          console.error("Error fetching test questions:", error);
          this.error = "فشل في تحميل الأسئلة";
          updatedTest.showQuestions = false;
        } finally {
          this.loading = false;
        }


        this.tests.splice(index, 1, updatedTest);

        if (!updatedTest.showQuestions && updatedTest.questions) {
          const updatedQuestions = updatedTest.questions.map(q => ({
            ...q,
            showResponses: false
          }));
          this.tests[index].questions = updatedQuestions;
        }
      }
    },

    toggleViewResponses(test, question) {
      const testIndex = this.tests.findIndex(t => t.id === test.id);
      if (testIndex !== -1) {
        const questionIndex = this.tests[testIndex].questions.findIndex(q => q.id === question.id);
        if (questionIndex !== -1) {
          const updatedQuestions = [...this.tests[testIndex].questions];

          const updatedQuestion = {
            ...updatedQuestions[questionIndex],
            showResponses: !updatedQuestions[questionIndex].showResponses
          };

          updatedQuestions.splice(questionIndex, 1, updatedQuestion);

          this.tests[testIndex].questions = updatedQuestions;
        }
      }
    },

    editTest(test) {
      this.editingTest = test;

      this.currentTest = {
        id: test.id,
        title: test.title || test.nomTest || "",
        type_test: test.type_test || test.typeTest || "",
        utilisable: test.utilisable === 1 || test.utilisable === true,
        questions: [...(test.questions || [])]
      };

      console.log("Editing test:", this.currentTest);

      this.showAddTestModal = true;
    },

    async handleUtilisableChange() {
      if (
        this.currentTest.type_test.toLowerCase() === "personnalité" &&
        this.currentTest.utilisable
      ) {
        this.personnaliteTestBackup = { ...this.currentTest };
        this.showPersonnaliteAlert = true;
      }
    },

    cancelPersonnaliteAlert() {
      if (this.personnaliteTestBackup) {
        this.currentTest.utilisable = this.personnaliteTestBackup.utilisable;
      } else {
        this.currentTest.utilisable = false;
      }
      this.showPersonnaliteAlert = false;
      this.personnaliteTestBackup = null;
    },

    async confirmPersonnaliteAlert() {
      try {
        this.loading = true;

        const personnaliteTests = await TestService.getTestsByType("personnalité");
        console.log("Found personnalité tests:", personnaliteTests);

        for (const test of personnaliteTests) {
          if (this.editingTest && test.id === this.editingTest.id) {
            continue;
          }

          if (test.utilisable === 1 || test.utilisable === true) {
            console.log(`Setting test ${test.id} (${test.nomTest || test.title}) to non-utilisable`);

            await TestService.updateTest(test.id, {
              nomTest: test.nomTest || test.title,
              typeTest: test.typeTest || test.type_test,
              utilisable: 0
            });

            const index = this.tests.findIndex(t => t.id === test.id);
            if (index !== -1) {
              this.tests[index] = {
                ...this.tests[index],
                utilisable: false
              };
            }
          }
        }

        this.showPersonnaliteAlert = false;
        this.personnaliteTestBackup = null;
        this.successMessage = "تم تحديث حالة الاختبارات بنجاح";
      } catch (error) {
        console.error("Error updating personnalité tests:", error);
        this.error = "فشل في تحديث حالة الاختبارات";
      } finally {
        this.loading = false;
      }
    },

    async deleteTest(id) {
      try {
        this.loading = true;
        await TestService.deleteTest(id);
        this.tests = this.tests.filter(t => t.id !== id);
        this.showDeleteModal = false;
        this.testToDelete = null;
        this.successMessage = "تم حذف الاختبار بنجاح";
      } catch (error) {
        console.error("Error deleting test:", error);
        this.error = "فشل في حذف الاختبار";
      } finally {
        this.loading = false;
      }
    },

    editQuestion(test, question) {
      this.currentTestForQuestion = test;
      this.editingQuestion = question;

      this.currentQuestion = {
        id: question.id,
        text: question.contenu || question.text,
        responses: question.responses && question.responses.length > 0 ?
          [...question.responses.map(r => ({ ...r }))] :
          [{ text: "" }, { text: "" }, { text: "" }, { text: "" }]
      };

      while (this.currentQuestion.responses.length < 4) {
        this.currentQuestion.responses.push({ text: "" });
      }

      this.showQuestionModal = true;
    },

    addQuestionToTest(test) {
      this.currentTestForQuestion = test;
      this.editingQuestion = null;
      this.currentQuestion = {
        text: "",
        responses: [
          { text: "" },
          { text: "" },
          { text: "" },
          { text: "" }
        ]
      };
      this.showQuestionModal = true;
    },

    confirmDeleteTest(test) {
      this.testToDelete = test;
      this.showDeleteModal = true;
    },

    cancelDelete() {
      this.showDeleteModal = false;
      this.testToDelete = null;
    },

    cancelAddEdit() {
      this.showAddTestModal = false;
      this.editingTest = null;
      this.currentTest = {
        title: "",
        type_test: "",
        utilisable: true,
        questions: []
      };
    },

    async saveTest() {
      try {
        this.loading = true;
        console.log("Saving test...", this.currentTest);

        const testData = {
          nomTest: this.currentTest.title,
          typeTest: this.currentTest.type_test,
          utilisable: this.currentTest.utilisable ? 1 : 0
        };

        console.log("Prepared test data for API:", testData);

        let savedTest;

        // Check if this is a personnalité test and it's utilisable
        if (
          testData.typeTest.toLowerCase() === "personnalité" &&
          testData.utilisable === 1
        ) {
          // Check if there's already an active personnalité test before proceeding
          const personnaliteTests = await TestService.getTestsByType("personnalité");
          const hasActiveTest = personnaliteTests.some(test =>
            (test.utilisable === 1 || test.utilisable === true) &&
            (!this.editingTest || test.id !== this.currentTest.id)
          );

          if (hasActiveTest && !this.showPersonnaliteAlert) {
            // Store the current test settings before showing the alert
            this.personnaliteTestBackup = { ...this.currentTest };
            this.showPersonnaliteAlert = true;
            this.loading = false;
            return; // Stop here and wait for user confirmation
          }
        }

        if (this.editingTest) {
          console.log("Updating existing test with ID:", this.currentTest.id);

          savedTest = await TestService.updateTest(this.currentTest.id, testData);
          console.log("Update response:", savedTest);

          const index = this.tests.findIndex(t => t.id === this.currentTest.id);
          if (index !== -1) {
            this.tests[index] = {
              ...this.tests[index],
              nomTest: testData.nomTest,
              typeTest: testData.typeTest,
              utilisable: testData.utilisable === 1,
              title: testData.nomTest,
              type_test: testData.typeTest,
              lastUpdated: this.formatDate(new Date())
            };
          }

          this.successMessage = "تم تحديث الاختبار بنجاح";
        } else {
          savedTest = await TestService.createTest(testData);

          this.tests.push({
            ...savedTest,
            title: savedTest.nomTest,
            type_test: savedTest.typeTest,
            showQuestions: false,
            questions: [],
            lastUpdated: this.formatDate(new Date()),
            utilisable: savedTest.utilisable === 1
          });

          this.successMessage = "تم إنشاء الاختبار بنجاح";
        }

        if (testData.typeTest.toLowerCase() === "personnalité") {
          await this.fetchTests();
        }

        this.cancelAddEdit();
      } catch (error) {
        console.error("Error saving test:", error);
        console.error("Error details:", error.response?.data || error.message);
        this.error = this.editingTest ? "فشل في تحديث الاختبار" : "فشل في إنشاء الاختبار";
      } finally {
        this.loading = false;
      }
    },

    async handlePersonnaliteLogic() {
      try {
        const personnaliteTests = await TestService.getTestsByType("personnalité");

        for (const test of personnaliteTests) {
          if (this.editingTest && test.id === this.editingTest.id) {
            continue;
          }

          if (test.utilisable === 1 || test.utilisable === true) {
            console.log(`Setting test ${test.id} to non-utilisable`);
            await TestService.updateTest(test.id, {
              nomTest: test.nomTest || test.title,
              typeTest: test.typeTest || test.type_test,
              utilisable: 0
            });
          }
        }
      } catch (error) {
        console.error("Error in handlePersonnaliteLogic:", error);
        throw error;
      }
    },

    async saveQuestion() {
      if (!this.currentTestForQuestion) return;

      try {
        this.loading = true;

        const questionData = {
          contenu: this.currentQuestion.text,
          testId: this.currentTestForQuestion.id
        };

        let savedQuestion;
        if (this.editingQuestion) {
          savedQuestion = await QuestionService.updateQuestion(this.currentQuestion.id, questionData);
        } else {
          savedQuestion = await QuestionService.createQuestion(questionData);
        }

        for (const response of this.currentQuestion.responses) {
          if (response.text.trim() !== '') {
            const responseData = {
              contenu: response.text,
              questionId: savedQuestion.id
            };

            if (response.id) {
              await ResponseService.updateReponse(response.id, responseData);
            } else {
              await ResponseService.createReponse(responseData);
            }
          }
        }

        await this.fetchTests();
        this.cancelQuestionEdit();
        this.successMessage = this.editingQuestion ? "تم تحديث السؤال بنجاح" : "تم إضافة السؤال بنجاح";
      } catch (error) {
        console.error("Error saving question:", error);
        this.error = "فشل في حفظ السؤال: " + (error.response?.data || error.message);
      } finally {
        this.loading = false;
      }
    },

    cancelQuestionEdit() {
      this.showQuestionModal = false;
      this.editingQuestion = null;
      this.currentTestForQuestion = null;
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

    async deleteQuestion(test, question) {
      try {
        this.loading = true;
        await QuestionService.deleteQuestion(question.id);

        const testIndex = this.tests.findIndex(t => t.id === test.id);
        if (testIndex !== -1 && this.tests[testIndex].questions) {
          this.tests[testIndex].questions = this.tests[testIndex].questions.filter(
            q => q.id !== question.id
          );
        }

        this.successMessage = "تم حذف السؤال بنجاح";
      } catch (error) {
        console.error("Error deleting question:", error);
        this.error = "فشل في حذف السؤال";
      } finally {
        this.loading = false;
      }
    },

    formatDate(date) {
      return new Intl.DateTimeFormat('ar-SA', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      }).format(date);
    }
  }
};
</script>
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

.empty-questions,
.empty-responses {
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

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.form-group input[type="text"] {
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

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  background-color: #f8fafc;
  border-radius: 12px;
}

.loading-spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
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

.error-alert {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #fee2e2;
  border-left: 4px solid #ef4444;
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 350px;
  z-index: 1000;
}

.error-content {
  display: flex;
  align-items: center;
}

.error-icon {
  margin-left: 0.75rem;
}

.error-message {
  flex-grow: 1;
  color: #b91c1c;
}

.error-close {
  background: none;
  border: none;
  color: #b91c1c;
  font-size: 1.2rem;
  cursor: pointer;
  margin-right: -0.5rem;
  padding: 0.25rem;
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