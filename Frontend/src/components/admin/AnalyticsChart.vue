<template>
  <section class="analytics-card">
    <div class="card-header">
      <h2>
        <span class="icon-wrapper">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
            <line x1="3" y1="9" x2="21" y2="9"></line>
            <line x1="9" y1="21" x2="9" y2="9"></line>
          </svg>
        </span>
        الإحصائيات
      </h2>
      <div class="card-actions">
        <button class="action-btn" @click="refreshData" :class="{ 'rotating': isRefreshing }">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M23 4v6h-6"></path>
            <path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"></path>
          </svg>
        </button>
        <div class="dropdown">
          <button class="action-btn" @click="toggleDropdown">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="1"></circle>
              <circle cx="12" cy="5" r="1"></circle>
              <circle cx="12" cy="19" r="1"></circle>
            </svg>
          </button>
          <div class="dropdown-menu" v-if="showDropdown">
            <button @click="changeChartType('bar')">مخطط شريطي</button>
            <button @click="changeChartType('line')">مخطط خطي</button>
            <button @click="changeChartType('doughnut')">مخطط دائري</button>
            <button @click="exportData">تصدير البيانات</button>
          </div>
        </div>
      </div>
    </div>
    
    <div class="time-selector">
      <button 
        v-for="period in timePeriods" 
        :key="period.value" 
        @click="setTimePeriod(period.value)"
        :class="{ active: selectedPeriod === period.value }"
        class="time-btn"
      >
        {{ period.label }}
      </button>
    </div>
    
    <div class="chart-container">
      <canvas ref="progressChart"></canvas>
      <div class="loading-overlay" v-if="isRefreshing">
        <div class="spinner"></div>
      </div>
    </div>
    
    <div class="chart-legend">
      <div 
        class="legend-item"
        v-for="(item, index) in legendItems" 
        :key="index"
        @click="toggleDataset(index)"
        :class="{ 'legend-item-inactive': !activeDatasets[index] }"
      >
        <span class="color-dot" :style="{ backgroundColor: item.color }"></span> 
        {{ item.label }}
      </div>
    </div>
    
    <div class="summary-row">
      <div class="summary-item" v-for="(stat, index) in summaryStats" :key="index">
        <div class="summary-value">{{ stat.value }}</div>
        <div class="summary-label">{{ stat.label }}</div>
      </div>
    </div>
  </section>
</template>

<script>
import { ref, onMounted, computed, watch } from "vue";
import Chart from "chart.js/auto";
import {fetchStatistics} from '@/Services/statiqueService'; // <- service importé

export default {
  setup() {
    const progressChart = ref(null);
    const chartInstance = ref(null);
    const isRefreshing = ref(false);
    const showDropdown = ref(false);
    const selectedPeriod = ref("week");
    const currentChartType = ref("bar");
    const activeDatasets = ref([true, true, true, true]);

    const chartData = ref({
      users: 0,
      chapters: 0,
      quizzes: 0,
      quotes: 0
    });

    const timePeriods = [
      { label: "يوم", value: "day" },
      { label: "أسبوع", value: "week" },
      { label: "شهر", value: "month" },
      { label: "سنة", value: "year" }
    ];

    const legendItems = computed(() => [
      { label: "المستخدمين", color: "#4361ee" },
      { label: "الفصول", color: "#3a0ca3" },
      { label: "الاختبارات", color: "#7209b7" },
      { label: "الاقتباسات", color: "#f72585" }
    ]);

    const summaryStats = computed(() => {
      const data = [
        chartData.value.users,
        chartData.value.chapters,
        chartData.value.quizzes,
        chartData.value.quotes
      ];
      const total = data.reduce((a, b) => a + b, 0);
      return [
        { label: "إجمالي النشاط", value: total },
        {
          label: "متوسط لكل فصل",
          value: chartData.value.chapters === 0
            ? 0
            : Math.round(chartData.value.users / chartData.value.chapters)
        },
        {
          label: "نسبة الاختبارات",
          value: total === 0 ? "0%" : Math.round((chartData.value.quizzes / total) * 100) + "%"
        },
        {
          label: "نسبة الاقتباسات",
          value: total === 0 ? "0%" : Math.round((chartData.value.quotes / total) * 100) + "%"
        }
      ];
    });

    const loadStatistics = async () => {
      try {
        const data = await fetchStatistics(selectedPeriod.value); // <- appel au service
        chartData.value = {
          users: data.users,
          chapters: data.chapters,
          quizzes: data.tests, // backend retourne "tests"
          quotes: data.quotes
        };
        initChart();
      } catch (error) {
        console.error("Erreur API:", error);
      }
    };

    const initChart = () => {
      if (chartInstance.value) chartInstance.value.destroy();
      const ctx = progressChart.value.getContext("2d");

      const createGradient = (color1, color2) => {
        const gradient = ctx.createLinearGradient(0, 0, 0, 400);
        gradient.addColorStop(0, color1);
        gradient.addColorStop(1, color2);
        return gradient;
      };

      const config = {
        type: currentChartType.value,
        data: {
          labels: ["المستخدمين", "الفصول", "الاختبارات", "الاقتباسات"],
          datasets: [
            {
              label: "نظرة عامة",
              data: [
                chartData.value.users,
                chartData.value.chapters,
                chartData.value.quizzes,
                chartData.value.quotes
              ],
              backgroundColor: [
                createGradient("#4361ee", "rgba(67, 97, 238, 0.6)"),
                createGradient("#3a0ca3", "rgba(58, 12, 163, 0.6)"),
                createGradient("#7209b7", "rgba(114, 9, 183, 0.6)"),
                createGradient("#f72585", "rgba(247, 37, 133, 0.6)")
              ],
              borderRadius: 8,
              maxBarThickness: 60
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false
            }
          },
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      };

      chartInstance.value = new Chart(ctx, config);
    };

    const refreshData = () => {
      isRefreshing.value = true;
      setTimeout(async () => {
        await loadStatistics();
        isRefreshing.value = false;
      }, 800);
    };

    const setTimePeriod = (period) => {
      selectedPeriod.value = period;
    };

    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value;
    };

    const changeChartType = (type) => {
      currentChartType.value = type;
      initChart();
    };

    const exportData = () => {
      showDropdown.value = false;
      const dataStr = JSON.stringify(chartData.value);
      alert("تم تصدير البيانات: " + dataStr);
    };

    onMounted(() => {
      loadStatistics();
    });

    watch(selectedPeriod, loadStatistics);

    return {
      progressChart,
      isRefreshing,
      refreshData,
      showDropdown,
      toggleDropdown,
      changeChartType,
      timePeriods,
      selectedPeriod,
      setTimePeriod,
      exportData,
      legendItems,
      activeDatasets,
      summaryStats
    };
  }
};
</script>

<style scoped>
.analytics-card {
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
  padding: 22px;
  margin-bottom: 24px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.analytics-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, #4361ee, #3a0ca3, #7209b7, #f72585);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.analytics-card:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.analytics-card:hover::after {
  opacity: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

h2 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #2d3748;
  margin: 0;
  display: flex;
  align-items: center;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  background: linear-gradient(135deg, #4361ee18, #3a0ca328);
  border-radius: 10px;
  margin-left: 12px;
  color: #4361ee;
}

.card-actions {
  display: flex;
  gap: 8px;
  position: relative;
}

.action-btn {
  background: none;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #718096;
}

.action-btn:hover {
  background-color: #f7fafc;
  color: #3a0ca3;
}

.rotating {
  animation: rotate 1s infinite linear;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  top: 100%;
  width: 160px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  padding: 6px 0;
  z-index: 10;
  margin-top: 8px;
  transform-origin: top right;
  animation: dropdown 0.2s ease-out forwards;
}

@keyframes dropdown {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

.dropdown-menu button {
  display: block;
  width: 100%;
  text-align: right;
  padding: 8px 16px;
  background: none;
  border: none;
  font-size: 0.9rem;
  color: #4a5568;
  cursor: pointer;
  transition: all 0.15s ease;
}

.dropdown-menu button:hover {
  background-color: #f7fafc;
  color: #3a0ca3;
}

.time-selector {
  display: flex;
  gap: 6px;
  justify-content: flex-end;
  margin-bottom: 16px;
}

.time-btn {
  padding: 5px 10px;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #718096;
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.time-btn:hover {
  border-color: #cbd5e0;
  color: #4a5568;
}

.time-btn.active {
  background-color: #4361ee;
  border-color: #4361ee;
  color: white;
}

.chart-container {
  height: 280px;
  margin-bottom: 16px;
  position: relative;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
  border-radius: 8px;
}

.spinner {
  width: 30px;
  height: 30px;
  border: 3px solid rgba(67, 97, 238, 0.3);
  border-radius: 50%;
  border-top-color: #4361ee;
  animation: spin 1s ease infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.chart-legend {
  display: flex;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  margin: 16px 0;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  color: #4a5568;
  padding: 5px 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.legend-item:hover {
  background-color: #f7fafc;
}

.legend-item-inactive {
  opacity: 0.5;
}

.color-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  margin-left: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  border-top: 1px solid #f0f4f8;
  padding-top: 16px;
}

.summary-item {
  text-align: center;
  padding: 0 10px;
}

.summary-value {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2d3748;
}

.summary-label {
  font-size: 0.8rem;
  color: #718096;
  margin-top: 4px;
}

@media (max-width: 768px) {
  .analytics-card {
    padding: 18px;
  }
  
  .chart-container {
    height: 250px;
  }
  
  .summary-row {
    flex-wrap: wrap;
    gap: 10px;
  }
  
  .summary-item {
    flex: 1 0 40%;
  }
}
</style>