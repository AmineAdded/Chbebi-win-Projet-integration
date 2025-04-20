<template>
  <div class="time-manager">
    <!-- Navbar -->
    <Navbar @openUpdateAccount="showUpdateAccount = true"/>
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>
    
    <!-- Main Content -->
    <div class="container app-container py-5">
      <div class="title-container text-center">
        <h1 class="app-title mb-2">نظم وقتك</h1>
        <div class="title-underline"></div>
      </div>
      
      <div class="todo-card">
        <!-- Date Navigation -->
        <div class="date-navigator">
          <div class="d-flex justify-content-between align-items-center">
            <button @click="prevDay" class="nav-button">
              <i class="mdi mdi-chevron-right"></i>
            </button>
            
            <div class="date-display">{{ formattedDate }}</div>
            
            <button @click="nextDay" class="nav-button">
              <i class="mdi mdi-chevron-left"></i>
            </button>
          </div>
          
          <div class="text-center mt-3">
            <button @click="loadToday" class="today-button">
              <i class="mdi mdi-calendar-today me-1"></i> اليوم
            </button>
          </div>
        </div>
        
        <!-- Task List -->
        <div class="task-list" v-if="currentTasks.length > 0">
          <div v-for="(task, index) in currentTasks" :key="index" 
               class="task-item" :class="{ 'completed': task.done }">
            <div class="task-content">
              <div class="task-text">{{ task.text }}</div>
              <div class="task-status" :class="{ 'status-completed': task.done }">
                {{ task.done ? 'مكتمل' : 'قيد التنفيذ' }}
              </div>
            </div>
            <label class="checkbox-container">
              <input type="checkbox" class="task-checkbox" v-model="task.done" @change="saveTasks" />
              <span class="checkmark"></span>
            </label>
          </div>
        </div>
        
        <!-- Empty State -->
        <div v-else class="empty-state">
          <div class="empty-state-icon">
            <i class="mdi mdi-calendar-clock gradient-icon"></i>
          </div>
          <p class="empty-state-title">لا توجد مهام لهذا اليوم</p>
          <p class="empty-state-subtitle">أضف مهمة جديدة لبدء تنظيم يومك</p>
          <button class="empty-state-button" @click="focusTaskInput">
            <i class="mdi mdi-plus-circle me-2"></i>
            إضافة مهمة جديدة
          </button>
        </div>
        
        <!-- Add Task -->
        <div class="add-task-container">
          <input 
            ref="taskInput"
            v-model="newTask" 
            @keyup.enter="addTask" 
            type="text" 
            class="task-input" 
            placeholder="أضف مهمة جديدة..." 
          />
          <button class="add-button" @click="addTask">
            <i class="mdi mdi-plus me-1"></i> إضافة
          </button>
        </div>
      </div>
    </div>
    
    <!-- Footer -->
    <Footer/>
    
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue';
import Footer from '@/components/Footer.vue';
import { defineComponent } from "vue";
import UpdateAccount from '@/components/UpdateAccount.vue';

export default defineComponent({
  components: {
    Navbar,
    Footer,
    UpdateAccount
  },

  name: "TimeManager",
  data() {
    return {
      selectedDate: new Date(),
      tasksByDate: {},
      newTask: '',
      showUpdateAccount: false,
      monthsArabic: [
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
      ]
    };
  },
  computed: {
    formattedDate() {
      const day = this.selectedDate.getDate();
      const month = this.monthsArabic[this.selectedDate.getMonth()];
      const year = this.selectedDate.getFullYear();
      return `${day} ${month} ${year}`;
    },
    dateKey() {
      return this.selectedDate.toDateString();
    },
    currentTasks() {
      return this.tasksByDate[this.dateKey] || [];
    }
  },
  methods: {
    saveTasks() {
      localStorage.setItem("tasksByDate", JSON.stringify(this.tasksByDate));
    },
    loadTasks() {
      const stored = localStorage.getItem("tasksByDate");
      if (stored) {
        this.tasksByDate = JSON.parse(stored);
      }
    },
    addTask() {
      if (!this.newTask.trim()) return;
      
      if (!this.tasksByDate[this.dateKey]) {
        this.tasksByDate[this.dateKey] = [];
      }
      
      this.tasksByDate[this.dateKey].push({
        text: this.newTask.trim(),
        done: false
      });
      
      this.newTask = '';
      this.saveTasks();
    },
    prevDay() {
      const newDate = new Date(this.selectedDate);
      newDate.setDate(newDate.getDate() - 1);
      this.selectedDate = newDate;
    },
    nextDay() {
      const newDate = new Date(this.selectedDate);
      newDate.setDate(newDate.getDate() + 1);
      this.selectedDate = newDate;
    },
    loadToday() {
      this.selectedDate = new Date();
    },
    focusTaskInput() {
      this.$refs.taskInput.focus();
    }
  },
  mounted() {
    this.loadTasks();
  },
  watch: {
    tasksByDate: {
      handler() {
        this.saveTasks();
      },
      deep: true
    }
  }
});
</script>

<style scoped>
:root {
  --primary-color: #6d28d9;
  --primary-light: #8b5cf6;
  --primary-dark: #5b21b6;
  --secondary-color: #7c3aed;
  --accent-color: #4f46e5;
  --success-color: #10b981;
  --pending-color: #f59e0b;
  --bg-light: #f5f3ff;
  --card-bg: #ffffff;
  --text-dark: #1e293b;
  --text-light: #f9fafb;
  --text-muted: #64748b;
  --border-light: #e0e7ff;
  --shadow-color: rgba(109, 40, 217, 0.2);
}

.gradient-icon {
  background: linear-gradient(45deg, #7c3aed, #4f46e5);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 48px;
}

.time-manager {
  font-family: 'Cairo', 'Tajawal', sans-serif;
  background: #f0f7ff; /* Bleu très clair, presque blanc */
  min-height: 100vh;
  color: var(--text-dark);
  direction: rtl;
}
.container.app-container {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 2rem;
  padding-bottom: 2rem;
}


.app-container {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 2.5rem;
  padding-bottom: 2.5rem;
}

.title-container {
  position: relative;
  padding: 25px 0;
  margin-bottom: 40px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e6edf5;
}

.app-title {
  color: #2c5282; /* Bleu foncé solide */
  font-weight: 700;
  font-size: 2.5rem; /* Taille réduite */
  position: relative;
  display: inline-block;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
  letter-spacing: 0.5px;
}
.app-title {
  background-color: #2c5282;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-underline {
  width: 160px;
  height: 5px;
  background: #3182ce; /* Bleu uni au lieu du dégradé */
  border-radius: 3px;
  margin: 0 auto;
  margin-top: 10px;
}
.title-container::before {
  content: "";
  position: absolute;
  top: -5px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #a78bfa, #4f46e5);
  border-radius: 50%;
  opacity: 0.1;
  z-index: -1;
  filter: blur(15px);
  animation: pulse 3s infinite ease-in-out;
}
@keyframes pulse {
  0% { transform: translateX(-50%) scale(1); opacity: 0.1; }
  50% { transform: translateX(-50%) scale(1.5); opacity: 0.15; }
  100% { transform: translateX(-50%) scale(1); opacity: 0.1; }
}

.todo-card {
  background: var(--card-bg);
  border-radius: 24px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2); /* Ombre plus prononcée */
  padding: 40px;
  margin-bottom: 40px;
  border: 1px solid #e2e8f0; /* Bordure plus visible */
}

.date-navigator {
  background: #f8fafc; /* Couleur de fond plus claire et nette */
  border-radius: 16px;
  padding: 28px;
  margin-bottom: 35px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1); /* Ombre plus prononcée */
  border: 1px solid #e2e8f0; /* Ajout d'une bordure subtile */
}

.date-display {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-color);
  text-align: center;
  margin-left: 20px;
  margin-right: 20px;
  letter-spacing: 0.5px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.05);
}

.nav-button {
  background: #4f46e5; /* Couleur solide au lieu d'un dégradé pour une meilleure visibilité */
  color: white;
  border: none;
  border-radius: 14px;
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3); /* Ombre plus prononcée */
  font-size: 1.8rem; /* Icône plus grande */
  transition: all 0.3s ease;
}
.nav-button i {
  color: white;
  font-size: 2rem; /* Icône plus grande */
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2); /* Ombre de texte pour meilleure visibilité */
}

.nav-button:hover {
  background: #3730a3; /* Plus foncé au survol */
  transform: translateY(-3px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.4);
}

.today-button {
  background: linear-gradient(145deg, var(--secondary-color), var(--accent-color));
  color: white;
  border: none;
  border-radius: 14px;
  padding: 14px 36px;
  font-weight: 600;
  font-size: 1.2rem;
  letter-spacing: 0.5px;
  box-shadow: 0 6px 12px rgba(124, 58, 237, 0.3);
  transition: all 0.3s ease;
}

.today-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(124, 58, 237, 0.4);
}

.task-list {
  max-height: 450px;
  overflow-y: auto;
  margin: 30px 0;
  padding: 15px;
  border-radius: 16px;
  background: #f8f7ff;
  box-shadow: inset 0 3px 6px rgba(0,0,0,0.05);
}

.task-item {
  background: white;
  border-radius: 14px;
  padding: 22px 26px;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-right: 5px solid var(--primary-color);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.task-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.06);
}

.task-item.completed {
  border-right-color: var(--success-color);
  background: linear-gradient(145deg, #f0fdf4, #f8fff9);
}

.task-content {
  flex-grow: 1;
  margin-left: 20px;
}

.task-text {
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--text-dark);
  transition: color 0.3s ease;
}

.task-status {
  font-size: 0.9rem;
  font-weight: 600;
  display: inline-block;
  padding: 6px 16px;
  border-radius: 30px;
  color: var(--pending-color);
  background-color: #fef3c7;
  box-shadow: 0 2px 5px rgba(245, 158, 11, 0.2);
  transition: all 0.3s ease;
}

.status-completed {
  color: var(--success-color);
  background-color: #d1fae5;
  box-shadow: 0 2px 5px rgba(16, 185, 129, 0.2);
}

.task-item.completed .task-text {
  text-decoration: line-through;
  color: var(--text-muted);
}

/* Significantly improved checkbox styling */
.checkbox-container {
  display: block;
  position: relative;
  min-width: 38px;
  height: 38px;
  margin: 0;
  cursor: pointer;
}

.task-checkbox {
  position: absolute;
  opacity: 0;
  height: 0;
  width: 0;
  cursor: pointer;
}

.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 38px;
  width: 38px;
  background-color: #fff;
  border: 3px solid #333; /* Darker border for better visibility */
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Stronger shadow */
  transition: all 0.3s ease;
}

.checkbox-container:hover .checkmark {
  background-color: #f0f0f0;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
  transform: scale(1.05);
}

.task-checkbox:checked ~ .checkmark {
  background: #10b981; /* Solid color instead of gradient for better visibility */
  border-color: #0d9668; /* Darker border for the checked state */
  box-shadow: 0 4px 10px rgba(16, 185, 129, 0.5); /* More pronounced shadow */
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
  left: 13px;
  top: 6px;
  width: 8px;
  height: 16px;
  border: solid white;
  border-width: 0 4px 4px 0; /* Thicker checkmark */
  transform: rotate(45deg);
}

.task-checkbox:checked ~ .checkmark:after {
  display: block;
  animation: checkmark 0.3s ease-in-out;
}

@keyframes checkmark {
  0% { opacity: 0; transform: rotate(45deg) scale(0.5); }
  50% { opacity: 1; transform: rotate(45deg) scale(1.2); }
  100% { opacity: 1; transform: rotate(45deg) scale(1); }
}

/* Enhanced empty state */
.empty-state {
  text-align: center;
  padding: 50px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.empty-state-icon {
  background: linear-gradient(145deg, rgba(124, 58, 237, 0.1), rgba(79, 70, 229, 0.15));
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30px;
  box-shadow: 0 12px 24px rgba(124, 58, 237, 0.2);
}

.empty-state i {
  font-size: 60px;
}
.date-display {
  font-size: 2rem;
  font-weight: 700;
  color: #1e293b; /* Couleur plus foncée pour le texte */
  text-align: center;
  margin-left: 20px;
  margin-right: 20px;
  letter-spacing: 0.5px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}
.today-button {
  background: #4f46e5; /* Couleur solide */
  color: white;
  border: none;
  border-radius: 14px;
  padding: 14px 36px;
  font-weight: 700; /* Texte plus gras */
  font-size: 1.2rem;
  letter-spacing: 0.5px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25);
  transition: all 0.3s ease;
}
.today-button i {
  margin-right: 8px;
  font-size: 1.3rem; /* Icône légèrement plus grande */
  color: white;
}
.today-button:hover {
  background: #3730a3; /* Plus foncé au survol */
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.35);
}
.empty-state-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 16px;
  color: var(--primary-color);
  text-shadow: 1px 1px 2px rgba(0,0,0,0.05);
}

.empty-state-subtitle {
  font-size: 1.2rem;
  color: var(--text-muted);
  margin-bottom: 30px;
}

.empty-state-button {
  background: linear-gradient(145deg, var(--secondary-color), var(--accent-color));
  color: white;
  border: none;
  border-radius: 14px;
  padding: 16px 36px;
  font-weight: 600;
  font-size: 1.2rem;
  margin-top: 10px;
  box-shadow: 0 6px 12px rgba(124, 58, 237, 0.3);
  transition: all 0.3s ease;
}

.empty-state-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(124, 58, 237, 0.4);
}

.add-task-container {
  display: flex;
  margin-top: 30px;
  background: linear-gradient(145deg, #f8f7ff, #f5f3ff);
  border-radius: 16px;
  padding: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.06);
  border: 1px solid var(--border-light);
}

.task-input {
  flex-grow: 1;
  border: none;
  padding: 18px 24px;
  font-size: 1.2rem;
  background: transparent;
  border-radius: 12px;
  color: var(--text-dark);
  transition: background 0.3s ease;
}

.task-input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.5);
}

.add-button {
  background: #6d28d9; /* Solid color for better text visibility */
  color: white;
  border: none;
  border-radius: 12px;
  padding: 14px 32px;
  font-weight: 700; /* Bolder text */
  font-size: 1.2rem;
  letter-spacing: 0.5px;
  box-shadow: 0 6px 12px rgba(99, 102, 241, 0.25);
  transition: all 0.3s ease;
}

.add-button:hover {
  background: #5b21b6; /* Slightly darker on hover */
  color: white; /* Explicitly set color on hover */
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(99, 102, 241, 0.35);
}
.add-button i {
  color: white;
  font-weight: bold;
  margin-right: 8px;
}
/* Scrollbar styling */
.task-list::-webkit-scrollbar {
  width: 12px;
}

.task-list::-webkit-scrollbar-track {
  background: #f5f3ff;
  border-radius: 10px;
}

.task-list::-webkit-scrollbar-thumb {
  background: linear-gradient(180deg, var(--primary-color), var(--accent-color));
  border-radius: 10px;
  border: 3px solid #f5f3ff;
}

@media (max-width: 768px) {
  .todo-card {
    padding: 30px 20px;
  }
  
  .date-navigator {
    padding: 20px;
  }
  
  .date-display {
    font-size: 1.6rem;
  }
  
  .task-text {
    font-size: 1.2rem;
  }
  
  .empty-state-icon {
    width: 100px;
    height: 100px;
  }
  
  .empty-state i {
    font-size: 50px;
  }
  
  .empty-state-title {
    font-size: 1.5rem;
  }
  @media (max-width: 768px) {
  .app-title {
    font-size: 2.5rem;
  }
  
  .title-underline {
    width: 150px;
    height: 6px;
  }
  
  .title-container {
    padding: 25px 0;
    margin-bottom: 40px;
  }
}
}
</style>