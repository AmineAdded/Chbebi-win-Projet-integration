<template>
  <section class="overview">
    <div class="welcome-section">
      <h1>مرحباً بك في لوحة القيادة</h1>
      <p class="date-display">{{ currentDate }}</p>
    </div>

    <div class="cards-container">
      <div class="stat-card users-card">
        <div class="card-icon">👥</div>
        <div class="card-content">
          <h3>المستخدمين</h3>
          <div class="stat-number">{{ stats.users }}</div>
          <div class="stat-label">نشط</div>
          <div class="progress-bar">
            <div class="progress-value" :style="{ width: userProgress + '%' }"></div>
          </div>
        </div>
      </div>

      <div class="stat-card chapters-card">
        <div class="card-icon">📖</div>
        <div class="card-content">
          <h3>الفصول</h3>
          <div class="stat-number">{{ stats.chapters }}</div>
          <div class="stat-label">منشورة</div>
          <div class="progress-bar">
            <div class="progress-value" :style="{ width: chapterProgress + '%' }"></div>
          </div>
        </div>
      </div>

      <div class="stat-card quizzes-card">
        <div class="card-icon">📝</div>
        <div class="card-content">
          <h3>الاختبارات</h3>
          <div class="stat-number">{{ stats.tests }}</div>
          <div class="stat-label">متاحة</div>
          <div class="progress-bar">
            <div class="progress-value" :style="{ width: quizProgress + '%' }"></div>
          </div>
        </div>
      </div>

      <div class="stat-card quotes-card">
        <div class="card-icon">💬</div>
        <div class="card-content">
          <h3>الاقتباسات</h3>
          <div class="stat-number">{{ stats.quotes }}</div>
          <div class="stat-label">مشاركة</div>
          <div class="progress-bar">
            <div class="progress-value" :style="{ width: quoteProgress + '%' }"></div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { ref, onMounted, computed } from "vue";
import {fetchStatistics} from '@/Services/statiqueService';

export default {
  setup() {
    const currentDate = new Date().toLocaleDateString("ar-SA", {
      weekday: "long",
      year: "numeric",
      month: "long",
      day: "numeric",
    });

    const stats = ref({
      users: 0,
      chapters: 0,
      tests: 0,
      quotes: 0,
    });

    const loadStats = async () => {
      try {
        const data = await fetchStatistics(); // par défaut "week"
        stats.value = data;
      } catch (err) {
        console.error("Erreur chargement statistiques:", err);
      }
    };

    const total = computed(() => {
      return (
        stats.value.users +
        stats.value.chapters +
        stats.value.tests +
        stats.value.quotes
      );
    });

    const percent = (count) =>
      total.value === 0 ? 0 : Math.round((count / total.value) * 100);

    const userProgress = computed(() => percent(stats.value.users));
    const chapterProgress = computed(() => percent(stats.value.chapters));
    const quizProgress = computed(() => percent(stats.value.tests));
    const quoteProgress = computed(() => percent(stats.value.quotes));

    onMounted(() => {
      loadStats();
    });

    return {
      currentDate,
      stats,
      userProgress,
      chapterProgress,
      quizProgress,
      quoteProgress,
    };
  },
};
</script>


<style scoped>
.overview {
  padding: 24px;
  margin-bottom: 32px;
  border-radius: 16px;
  background-color: #f6f8fa;
}

.welcome-section {
  margin-bottom: 24px;
  text-align: right;
}

h1 {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 8px;
}

.date-display {
  color: #666;
  font-size: 1rem;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 20px;
}

.stat-card {
  background-color: #ffffff;
  border-radius: 16px;
  padding: 20px;
  display: flex;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.stat-card::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 6px;
  height: 100%;
}

.users-card::after { background-color: #4361ee; }
.chapters-card::after { background-color: #3a0ca3; }
.quizzes-card::after { background-color: #7209b7; }
.final-tests-card::after { background-color: #f72585; }
.quotes-card::after { background-color: #4cc9f0; }

.card-icon {
  font-size: 2.5rem;
  margin-left: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-content {
  flex: 1;
}

h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #666;
  margin: 0 0 8px 0;
}

.stat-number {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 12px;
}

.progress-bar {
  width: 100%;
  height: 6px;
  background-color: #eaeef2;
  border-radius: 3px;
}

.progress-value {
  height: 100%;
  border-radius: 3px;
  transition: width 1s ease-in-out;
}

.users-card .progress-value { background-color: #4361ee; }
.chapters-card .progress-value { background-color: #3a0ca3; }
.quizzes-card .progress-value { background-color: #7209b7; }
.final-tests-card .progress-value { background-color: #f72585; }
.quotes-card .progress-value { background-color: #4cc9f0; }

@media (max-width: 768px) {
  .cards-container {
    grid-template-columns: 1fr;
  }
}
</style>