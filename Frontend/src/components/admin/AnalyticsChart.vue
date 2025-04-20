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

<script>
import { ref, onMounted, computed } from "vue";
import Chart from "chart.js/auto";

export default {
  props: {
    initialData: {
      type: Object,
      default: () => ({
        users: 250,
        chapters: 12,
        quizzes: 45,
        quotes: 100
      })
    }
  },
  
  setup(props) {
    const progressChart = ref(null);
    const chartInstance = ref(null);
    const isRefreshing = ref(false);
    const showDropdown = ref(false);
    const selectedPeriod = ref('week');
    const currentChartType = ref('bar');
    const activeDatasets = ref([true, true, true, true]);
    
    const timePeriods = [
      { label: 'يوم', value: 'day' },
      { label: 'أسبوع', value: 'week' },
      { label: 'شهر', value: 'month' },
      { label: 'سنة', value: 'year' }
    ];
    
    const legendItems = computed(() => [
      { label: 'المستخدمين', color: '#4361ee' },
      { label: 'الفصول', color: '#3a0ca3' },
      { label: 'الاختبارات', color: '#7209b7' },
      { label: 'الاقتباسات', color: '#f72585' }
    ]);
    
    const chartData = {
      day: [180, 8, 30, 75],
      week: [250, 12, 45, 100],
      month: [320, 15, 60, 150],
      year: [420, 24, 120, 280]
    };
    
    const summaryStats = computed(() => {
      const data = chartData[selectedPeriod.value];
      const total = data.reduce((a, b) => a + b, 0);
      
      return [
        { 
          label: 'إجمالي النشاط',
          value: total
        },
        { 
          label: 'متوسط لكل فصل',
          value: Math.round(data[0] / data[1])
        },
        { 
          label: 'نسبة الاختبارات',
          value: Math.round((data[2] / total) * 100) + '%'
        },
        { 
          label: 'نسبة الاقتباسات',
          value: Math.round((data[3] / total) * 100) + '%'
        }
      ];
    });
    
    const initChart = () => {
      if (chartInstance.value) {
        chartInstance.value.destroy();
      }
      
      const ctx = progressChart.value.getContext("2d");
      
      // Gradients for different chart types
      const userGradient = ctx.createLinearGradient(0, 0, 0, 400);
      userGradient.addColorStop(0, '#4361ee');
      userGradient.addColorStop(1, 'rgba(67, 97, 238, 0.6)');
      
      const chapterGradient = ctx.createLinearGradient(0, 0, 0, 400);
      chapterGradient.addColorStop(0, '#3a0ca3');
      chapterGradient.addColorStop(1, 'rgba(58, 12, 163, 0.6)');
      
      const quizGradient = ctx.createLinearGradient(0, 0, 0, 400);
      quizGradient.addColorStop(0, '#7209b7');
      quizGradient.addColorStop(1, 'rgba(114, 9, 183, 0.6)');
      
      const quoteGradient = ctx.createLinearGradient(0, 0, 0, 400);
      quoteGradient.addColorStop(0, '#f72585');
      quoteGradient.addColorStop(1, 'rgba(247, 37, 133, 0.6)');

      const datasets = [];
      let labels = [];
      
      if (currentChartType.value === 'bar') {
        labels = ["المستخدمين", "الفصول", "الاختبارات", "الاقتباسات"];
        datasets.push({
          label: "نظرة عامة",
          data: chartData[selectedPeriod.value],
          backgroundColor: [
            userGradient,
            chapterGradient,
            quizGradient,
            quoteGradient
          ],
          borderRadius: 8,
          maxBarThickness: 60
        });
      } else if (currentChartType.value === 'line') {
        labels = getLabelsForPeriod(selectedPeriod.value);
        
        // Generate simulated data for each category
        const userData = generateDataForPeriod(selectedPeriod.value, chartData[selectedPeriod.value][0]);
        const chapterData = generateDataForPeriod(selectedPeriod.value, chartData[selectedPeriod.value][1]);
        const quizData = generateDataForPeriod(selectedPeriod.value, chartData[selectedPeriod.value][2]);
        const quoteData = generateDataForPeriod(selectedPeriod.value, chartData[selectedPeriod.value][3]);
        
        datasets.push(
          {
            label: "المستخدمين",
            data: userData,
            borderColor: '#4361ee',
            backgroundColor: 'rgba(67, 97, 238, 0.1)',
            tension: 0.4,
            fill: true,
            hidden: !activeDatasets.value[0]
          },
          {
            label: "الفصول",
            data: chapterData,
            borderColor: '#3a0ca3',
            backgroundColor: 'rgba(58, 12, 163, 0.1)',
            tension: 0.4,
            fill: true,
            hidden: !activeDatasets.value[1]
          },
          {
            label: "الاختبارات",
            data: quizData,
            borderColor: '#7209b7',
            backgroundColor: 'rgba(114, 9, 183, 0.1)',
            tension: 0.4,
            fill: true,
            hidden: !activeDatasets.value[2]
          },
          {
            label: "الاقتباسات",
            data: quoteData,
            borderColor: '#f72585',
            backgroundColor: 'rgba(247, 37, 133, 0.1)',
            tension: 0.4,
            fill: true,
            hidden: !activeDatasets.value[3]
          }
        );
      } else if (currentChartType.value === 'doughnut') {
        labels = ["المستخدمين", "الفصول", "الاختبارات", "الاقتباسات"];
        datasets.push({
          data: chartData[selectedPeriod.value],
          backgroundColor: [
            '#4361ee',
            '#3a0ca3',
            '#7209b7',
            '#f72585'
          ],
          borderColor: 'white',
          borderWidth: 2,
          hoverOffset: 10
        });
      }

      const config = {
        type: currentChartType.value,
        data: {
          labels: labels,
          datasets: datasets
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: currentChartType.value === 'doughnut' ? '65%' : undefined,
          plugins: {
            legend: {
              display: currentChartType.value === 'doughnut',
              position: 'bottom',
              labels: {
                usePointStyle: true,
                padding: 15,
                font: {
                  size: 12
                }
              }
            },
            tooltip: {
              backgroundColor: 'rgba(255, 255, 255, 0.95)',
              titleColor: '#2d3748',
              bodyColor: '#4a5568',
              padding: 12,
              titleFont: {
                size: 14,
                weight: 'bold'
              },
              bodyFont: {
                size: 13
              },
              borderColor: 'rgba(0, 0, 0, 0.05)',
              borderWidth: 1,
              displayColors: true,
              boxWidth: 8,
              boxHeight: 8,
              usePointStyle: true,
              callbacks: {
                label: function(context) {
                  let label = context.dataset.label || context.label || '';
                  if (label) {
                    label += ': ';
                  }
                  label += context.raw || context.formattedValue;
                  return label;
                }
              }
            }
          },
          scales: currentChartType.value !== 'doughnut' ? {
            y: {
              beginAtZero: true,
              grid: {
                display: true,
                color: 'rgba(0, 0, 0, 0.03)',
                drawBorder: false
              },
              ticks: {
                font: {
                  size: 12
                },
                color: '#718096',
                padding: 8
              },
              border: {
                display: false
              }
            },
            x: {
              grid: {
                display: false,
                drawBorder: false
              },
              ticks: {
                font: {
                  size: 12
                },
                color: '#718096',
                padding: 8
              },
              border: {
                display: false
              }
            }
          } : {},
          animation: {
            duration: 1000,
            easing: 'easeOutQuart'
          },
          elements: {
            point: {
              radius: 3,
              hoverRadius: 5
            }
          }
        }
      };

      chartInstance.value = new Chart(ctx, config);
    };
    
    // Generate labels based on time period
    const getLabelsForPeriod = (period) => {
      switch(period) {
        case 'day':
          return ['8:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00'];
        case 'week':
          return ['الأحد', 'الاثنين', 'الثلاثاء', 'الأربعاء', 'الخميس', 'الجمعة', 'السبت'];
        case 'month':
          return ['1', '5', '10', '15', '20', '25', '30'];
        case 'year':
          return ['يناير', 'فبراير', 'مارس', 'أبريل', 'مايو', 'يونيو', 'يوليو', 'أغسطس', 'سبتمبر', 'أكتوبر', 'نوفمبر', 'ديسمبر'];
      }
    };
    
    // Generate simulated data for line charts
    const generateDataForPeriod = (period, maxValue) => {
      const numberOfPoints = getLabelsForPeriod(period).length;
      let result = [];
      let lastValue = maxValue * 0.7; // Start at 70% of max
      
      // Create somewhat realistic looking data with trends
      for (let i = 0; i < numberOfPoints; i++) {
        // Change between -10% and +15% from last value
        const change = lastValue * (Math.random() * 0.25 - 0.1);
        lastValue = Math.max(0, Math.min(maxValue, lastValue + change));
        result.push(Math.round(lastValue));
      }
      
      return result;
    };
    
    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value;
    };
    
    const refreshData = () => {
      isRefreshing.value = true;
      
      // Simulate data refresh delay
      setTimeout(() => {
        // Slightly modify data to simulate update
        Object.keys(chartData).forEach(key => {
          chartData[key] = chartData[key].map(val => 
            Math.floor(val * (1 + (Math.random() * 0.15 - 0.05)))
          );
        });
        
        // Update chart
        initChart();
        
        isRefreshing.value = false;
      }, 800);
    };
    
    const changeChartType = (type) => {
      currentChartType.value = type;
      showDropdown.value = false;
      initChart();
    };
    
    const setTimePeriod = (period) => {
      selectedPeriod.value = period;
      initChart();
    };
    
    const toggleDataset = (index) => {
      if (currentChartType.value === 'line') {
        activeDatasets.value[index] = !activeDatasets.value[index];
        if (chartInstance.value) {
          chartInstance.value.data.datasets[index].hidden = !activeDatasets.value[index];
          chartInstance.value.update();
        }
      }
    };
    
    const exportData = () => {
      showDropdown.value = false;
      // Simulation of data export
      const dataStr = JSON.stringify(chartData[selectedPeriod.value]);
      alert('تم تصدير البيانات: ' + dataStr);
    };
    
    onMounted(() => {
      chartData.week = [
        props.initialData.users || 250, 
        props.initialData.chapters || 12, 
        props.initialData.quizzes || 45, 
        props.initialData.quotes || 100
      ];
      
      initChart();
      
      // Close dropdown when clicking outside
      document.addEventListener('click', (e) => {
        if (!e.target.closest('.dropdown') && showDropdown.value) {
          showDropdown.value = false;
        }
      });
    });
    
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
      toggleDataset,
      summaryStats
    };
  }
};
</script>