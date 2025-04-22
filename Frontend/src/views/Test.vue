<template>
  <!-- Loading Screen -->
  <div v-if="isLoading" class="loader-container">
    <div class="loader-content">
      <div class="custom-loader">
        <div class="pulse-ring"></div>
        <div class="pulse-dot"></div>
      </div>
      <div class="mt-4 text-center loading-text">...جاري تحميل الاختبار</div>
    </div>
  </div>
  <div v-else class="main-container">
    <!-- Quiz Content -->
    <v-container fluid class="quiz-container">
      <v-sheet class="quiz-wrapper rounded-lg" color="transparent">

        <!-- Progress Navigation -->
        <v-sheet class="progress-tabs mb-6 mx-auto rounded-pill" max-width="700px">
          <v-row no-gutters>
            <v-col v-for="(tab, index) in 5" :key="index">
              <v-sheet class="pa-2 text-center rounded-pill mx-1"
                :color="currentTab === index ? '#1e2a38' : 'lightblue'" height="40"
                :elevation="currentTab === index ? 2 : 0">
                {{ index + 1 }}
              </v-sheet>
            </v-col>
          </v-row>
        </v-sheet>

        <!-- Question Card -->
        <v-card v-for="(question, index1) in questions" :key="index1" class="question-card mx-auto mb-8"
          max-width="700px" elevation="3" rounded="xl" v-show="currentTab === index1">

          <v-card-title class="text-center py-4 text-white question-header" dir="rtl">
            {{ question.contenu }}
          </v-card-title>

          <v-card-text class="answer-options pt-4">
            <v-sheet v-for="(reponse, index2) in question.reponses" :key="index2"
              class="answer-option mb-3 pa-3 d-flex align-center rounded-lg" elevation="1" @click="selectOption(index2)"
              :class="{ 'selected-option': selectedOption === index2 }">
              <span class="option-text" style="padding-right:15px">{{ reponse.contenu }}</span>
              <span class="option-letter me-2" dir="rtl">{{ optionLetters[index2] }}.</span>
            </v-sheet>
          </v-card-text>
        </v-card>

        <!-- Next Button -->
        <div class="text-right mb-6">
          <v-btn fab size="large" color="grey-darken-3" class="next-button" :disabled="selectedOption === null"
            @click="nextQuestion">
            <v-icon>
              {{ currentTab === 4 ? 'mdi-check' : 'mdi-arrow-right' }}
            </v-icon>
          </v-btn>
        </div>
      </v-sheet>
    </v-container>
  </div>

  <v-snackbar v-model="showSnackBar" :color="snackbarColor" location="top" timeout="3000" rounded="xl">
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
import QuestionService from '@/Services/QuestionService.js';
import { useUserStore } from '@/store/User/userStore';
import Axios from 'axios';
export default {
  data() {
    return {
      showSnackBar: false,
      sumA: 0,
      sumB: 0,
      sumC: 0,
      sumD: 0,
      max: 0,
      type: 0,
      snackbarColor: 'error',
      snackbarIcon: 'mdi-alert-circle',
      text: "",
      questions: [],
      isLoading: true,
      currentTab: 0,
      selectedOption: null,
      optionLetters: ['أ', 'ب', 'ج', 'د'],
    }
  },
  created() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1500);
  },
  methods: {
    selectOption(index) {
      this.selectedOption = index;
    },
    async nextQuestion() {
      if (this.selectedOption === 0) {
        this.sumA++;
      } else if (this.selectedOption === 1) {
        this.sumB++;
      } else if (this.selectedOption === 2) {
        this.sumC++;
      } else if (this.selectedOption === 3) {
        this.sumD++;
      }
      if (this.currentTab < 4) {
        this.currentTab++;
        this.selectedOption = null;
      } else {
        this.max = Math.max(this.sumA, this.sumB, this.sumC, this.sumD);
        if (this.max === this.sumA) {
          this.type = 1;
        } else if (this.max === this.sumB) {
          this.type = 2;
        } else if (this.max === this.sumC) {
          this.type = 3;
        } else if (this.max === this.sumD) {
          this.type = 4;
        }
        try {
          const store = useUserStore();
          const id = store.user.id;
          console.log("Type",this.type," id:",id);
          await QuestionService.setPersonnalite(this.type,id);
        } catch (err) {
          console.error(err);
          this.showSnackBar = true;
          this.text = "حدث خطأ أثناء تحديث نوع الشخصية";
        }
        this.$router.push({ name: 'Entrance' });
      }
    },
    async getTest() {
      try {
        const res = await Axios.get("http://localhost:9090/api/Test/getTestUtilisable");
        this.questions = await QuestionService.getAllQuestions({id:res.data.id,utilisable:res.data.utilisable});
      } catch (err) {
        this.showSnackBar = false;
        this.text = "حدث خطأ أثناء تحميل الأسئلة";
        console.error(err);
      }
    }
  },
  mounted() {
    this.getTest();
  }
}
</script>


<style scoped>
.quiz-container {
  min-height: 100vh;
  background-color: #a4c5d9;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.quiz-wrapper {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.loader-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #e3f2fd, #bbdefb);
  z-index: 1001;
}

.loading-text {
  color: #1565c0;
  font-size: 1.25rem;
  font-weight: 500;
}

.custom-loader {
  position: relative;
  width: 100px;
  height: 100px;
}

@keyframes pulsee {
  0% {
    transform: scale(0.8);
    opacity: 0.3;
  }

  50% {
    transform: scale(1.2);
    opacity: 0.6;
  }

  100% {
    transform: scale(0.8);
    opacity: 0.3;
  }
}

.pulse-ring {
  position: absolute;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: rgba(25, 118, 210, 0.2);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: pulsee 2s ease-out infinite;
}

.pulse-dot {
  position: absolute;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #1976d2;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.question-header {
  background-color: #1e2a38;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  font-size: 1.4rem;
  font-weight: 500;
}

.answer-option {
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid #e0e0e0;
}

.answer-option:hover {
  background-color: #f5f5f5;
  transform: translateY(-2px);
}

.selected-option {
  background-color: #e3f2fd;
  border-color: #2196f3;
}

.option-letter {
  font-weight: bold;
  font-size: 1.1rem;
}

.option-text {
  text-align: right;
  width: 100%;
}

.card-bottom-curve {
  height: 40px;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  margin-top: -20px;
  background-color: white;
  position: relative;
}

.next-button {
  transition: transform 0.3s ease;
}

.next-button:hover:not(:disabled) {
  transform: scale(1.1);
}

@keyframes pulse {
  0% {
    opacity: 0.6;
  }

  50% {
    opacity: 1;
  }

  100% {
    opacity: 0.6;
  }
}
</style>