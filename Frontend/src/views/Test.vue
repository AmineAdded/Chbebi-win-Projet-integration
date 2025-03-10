<template>
  <div>
    <!-- Loading Screen -->
    <div v-if="isLoading" class="loader-container">
      <div class="loader-content">
        <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
        <span class="loader-text">...جاري تحميل الاختبار</span>
      </div>
    </div>

    <!-- Quiz Content -->
    <v-container v-else fluid class="quiz-container">
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
        <v-card class="question-card mx-auto mb-8" max-width="700px" elevation="3" rounded="xl">
          <!-- Question Header -->
          <v-card-title class="text-center py-4 text-white question-header" dir="rtl">
            كيفاش تتعامل مع المشاكل ؟
          </v-card-title>

          <!-- Answer Options -->
          <v-card-text class="answer-options pt-4">
            <v-sheet v-for="(option, index) in options" :key="index"
              class="answer-option mb-3 pa-3 d-flex align-center rounded-lg" elevation="1" @click="selectOption(index)"
              :class="{ 'selected-option': selectedOption === index }">
              <span class="option-text" style="padding-right:15px">{{ option }}</span>
              <span class="option-letter me-2" dir="rtl">{{ optionLetters[index] }}.</span>

            </v-sheet>
          </v-card-text>

          <!-- Bottom Part (Rounded) -->
          <v-sheet class="card-bottom-curve"></v-sheet>
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
</template>

<script>
export default {
  data() {
    return {
      isLoading: true,
      currentTab: 0,
      selectedOption: null,
      optionLetters: ['أ', 'ب', 'ج', 'د'],
      options: [
        'التشجيع من الناس',
        'تحقيق اهدافي الشخصية',
        'الابداع و التحديات الجديدة',
        'تنظيم الأشياء و قيادة الفريق'
      ]
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
    nextQuestion() {
      if (this.currentTab < 4) {
        this.currentTab++;
        this.selectedOption = null;
      } else {
        this.$router.push({ name: 'Entrance' });
      }
    }
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
  justify-content: center;
  align-items: center;
  background: rgba(25, 118, 210, 0.03);
  z-index: 1001;
  backdrop-filter: blur(8px);
}

.loader-text {
  color: #1976d2;
  font-size: 2em;
  font-weight: 500;
  animation: pulse 2s infinite;
  padding-right: 14px;
  padding-top: 6px;
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