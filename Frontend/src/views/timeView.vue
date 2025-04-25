<template>
  <div class="time-manager">
    <!-- Navbar -->
    <Navbar @openUpdateAccount="showUpdateAccount = true"/>
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>
    
    <!-- Main Content -->
    <div class="container app-container py-3">
      <div class="title-container text-center">
        <h1 class="app-title mb-1">نظم وقتك</h1>
        <div class="title-underline"></div>
      </div>
      
      <div class="todo-card">
        <!-- Date Navigation -->
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
              <div class="task-text">{{ task.nomTache }}</div>
              <div class="task-status" :class="{ 'status-completed': task.done }">
                {{ task.done ? 'مكتمل' : 'قيد التنفيذ' }}
              </div>
            </div>
            <label class="checkbox-container">
              <input type="checkbox" class="task-checkbox" v-model="task.done" @change="saveChange(task.done,task.id)" />
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
import { useUserStore } from '@/store/User/userStore';
import TacheService from '@/Services/TacheService.js';

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
    isoDate() {
      return this.selectedDate.toISOString().split('T')[0];
    },
    currentTasks() {
      return this.tasksByDate[this.dateKey] || [];
    }
  },
  methods: {
    async loadTasks() {
      const store = useUserStore();
      const id = store.user.id;

      try {
        const tasks = await TacheService.getTachesByUserandDate(id, this.isoDate);
        this.tasksByDate[this.dateKey] = tasks;
      } catch (error) {
        console.error("Erreur lors du chargement des tâches :", error);
      }
    },
    async saveChange(done,id){
        try{
          return await TacheService.updateTache(id, done);
        }
        catch(err){
          console.error("Erreur lors de la mise à jour de la tâche :", err);
        }
        
    },

    async addTask() {
      if (!this.newTask.trim()) return;

      if (!this.tasksByDate[this.dateKey]) {
        this.tasksByDate[this.dateKey] = [];
      }

      this.tasksByDate[this.dateKey].push({
        nomTache: this.newTask.trim(),
        userId: useUserStore().user.id,
        date: this.isoDate,
      });
      
      try {
        await TacheService.addTache(this.newTask.trim(), useUserStore().user.id, this.isoDate);
        this.loadTasks();
      } catch (error) {
        console.error("Erreur lors de l'ajout de la tâche :", error);
      }
      this.newTask = '';
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
    selectedDate() {
      this.loadTasks();
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
  font-size: 36px;
}

.time-manager {
  font-family: 'Cairo', 'Tajawal', sans-serif;
  background: #f0f7ff;
  min-height: 100vh;
  color: var(--text-dark);
  direction: rtl;
}

.container.app-container {
  max-width: 600px;
  margin: 0 auto;
  padding-top: 1rem;
  padding-bottom: 1rem;
}

.title-container {
  position: relative;
  padding: 15px 0;
  margin-bottom: 20px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.06);
  border: 1px solid #e6edf5;
}

.app-title {
  color: #2c5282;
  font-weight: 700;
  font-size: 2rem;
  position: relative;
  display: inline-block;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
  background-color: #2c5282;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-underline {
  width: 120px;
  height: 3px;
  background: #3182ce;
  border-radius: 2px;
  margin: 0 auto;
  margin-top: 5px;
}

.title-container::before {
  content: "";
  position: absolute;
  top: -5px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 30px;
  background: linear-gradient(135deg, #a78bfa, #4f46e5);
  border-radius: 50%;
  opacity: 0.1;
  z-index: -1;
  filter: blur(10px);
  animation: pulse 3s infinite ease-in-out;
}

@keyframes pulse {
  0% { transform: translateX(-50%) scale(1); opacity: 0.1; }
  50% { transform: translateX(-50%) scale(1.3); opacity: 0.15; }
  100% { transform: translateX(-50%) scale(1); opacity: 0.1; }
}

.todo-card {
  background: var(--card-bg);
  border-radius: 16px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #e2e8f0;
}

.date-navigator {
  background: #f8fafc;
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
}

.date-display {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1e293b;
  text-align: center;
  margin-left: 5px;
  margin-right: 5px;
  letter-spacing: 0.5px;
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.08);
}

.nav-button {
  background: #4f46e5;
  color: white;
  border: none;
  border-radius: 10px;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
  font-size: 1.3rem;
  transition: all 0.2s ease;
}

.nav-button i {
  color: white;
  font-size: 1.5rem;
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.15);
}

.nav-button:hover {
  background: #3730a3;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.today-button {
  background: #4f46e5;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 8px 12px;
  font-weight: 600;
  font-size: 0.9rem;
  letter-spacing: 0.5px;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
  transition: all 0.2s ease;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 5px;
}

.today-button i {
  margin-right: 4px;
  font-size: 1rem;
  color: white;
}

.today-button:hover {
  background: #3730a3;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.task-list {
  max-height: 300px;
  overflow-y: auto;
  margin: 15px 0;
  padding: 10px;
  border-radius: 12px;
  background: #f8f7ff;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.04);
}

.task-item {
  background: white;
  border-radius: 10px;
  padding: 12px 16px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-right: 3px solid var(--primary-color);
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.03);
  transition: all 0.2s ease;
}

.task-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.05);
}

.task-item.completed {
  border-right-color: var(--success-color);
  background: linear-gradient(145deg, #f0fdf4, #f8fff9);
}

.task-content {
  flex-grow: 1;
  margin-left: 15px;
}

.task-text {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-dark);
  transition: color 0.2s ease;
}

.task-status {
  font-size: 0.75rem;
  font-weight: 600;
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  color: var(--pending-color);
  background-color: #fef3c7;
  box-shadow: 0 1px 3px rgba(245, 158, 11, 0.15);
  transition: all 0.2s ease;
}

.status-completed {
  color: var(--success-color);
  background-color: #d1fae5;
  box-shadow: 0 1px 3px rgba(16, 185, 129, 0.15);
}

.task-item.completed .task-text {
  text-decoration: line-through;
  color: var(--text-muted);
}

.checkbox-container {
  display: block;
  position: relative;
  min-width: 30px;
  height: 30px;
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
  height: 30px;
  width: 30px;
  background-color: #fff;
  border: 2px solid #333;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
  transition: all 0.2s ease;
}

.checkbox-container:hover .checkmark {
  background-color: #f0f0f0;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
  transform: scale(1.03);
}

.task-checkbox:checked ~ .checkmark {
  background: #10b981;
  border-color: #0d9668;
  box-shadow: 0 2px 5px rgba(16, 185, 129, 0.4);
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
  left: 10px;
  top: 5px;
  width: 6px;
  height: 12px;
  border: solid white;
  border-width: 0 3px 3px 0;
  transform: rotate(45deg);
}

.task-checkbox:checked ~ .checkmark:after {
  display: block;
  animation: checkmark 0.2s ease-in-out;
}

@keyframes checkmark {
  0% { opacity: 0; transform: rotate(45deg) scale(0.5); }
  50% { opacity: 1; transform: rotate(45deg) scale(1.1); }
  100% { opacity: 1; transform: rotate(45deg) scale(1); }
}

.empty-state {
  text-align: center;
  padding: 30px 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.empty-state-icon {
  background: linear-gradient(145deg, rgba(124, 58, 237, 0.1), rgba(79, 70, 229, 0.15));
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  box-shadow: 0 6px 12px rgba(124, 58, 237, 0.15);
}

.empty-state i {
  font-size: 40px;
}

.empty-state-title {
  font-size: 1.4rem;
  font-weight: 700;
  margin-bottom: 12px;
  color: var(--primary-color);
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.04);
}

.empty-state-subtitle {
  font-size: 1rem;
  color: var(--text-muted);
  margin-bottom: 20px;
}

.add-task-container {
  display: flex;
  margin-top: 15px;
  background: linear-gradient(145deg, #f8f7ff, #f5f3ff);
  border-radius: 12px;
  padding: 6px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
  border: 1px solid var(--border-light);
}

.task-input {
  flex-grow: 1;
  border: none;
  padding: 12px 16px;
  font-size: 1rem;
  background: transparent;
  border-radius: 10px;
  color: var(--text-dark);
  transition: background 0.2s ease;
}

.task-input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.5);
}

.add-button {
  background: #6d28d9;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: 600;
  font-size: 1rem;
  letter-spacing: 0.5px;
  box-shadow: 0 3px 6px rgba(99, 102, 241, 0.2);
  transition: all 0.2s ease;
}

.add-button:hover {
  background: #5b21b6;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(99, 102, 241, 0.3);
}

.add-button i {
  color: white;
  font-weight: bold;
  margin-right: 6px;
}

.task-list::-webkit-scrollbar {
  width: 8px;
}

.task-list::-webkit-scrollbar-track {
  background: #f5f3ff;
  border-radius: 8px;
}

.task-list::-webkit-scrollbar-thumb {
  background: linear-gradient(180deg, var(--primary-color), var(--accent-color));
  border-radius: 8px;
  border: 2px solid #f5f3ff;
}

@media (max-width: 768px) {
  .todo-card {
    padding: 15px 12px;
  }
  
  .date-navigator {
    padding: 12px;
  }
  
  .date-display {
    font-size: 1.2rem;
  }
  
  .task-text {
    font-size: 1rem;
  }
  
  .empty-state-icon {
    width: 70px;
    height: 70px;
  }
  
  .empty-state i {
    font-size: 35px;
  }
  
  .empty-state-title {
    font-size: 1.3rem;
  }
  
  .app-title {
    font-size: 1.8rem;
  }
  
  .title-underline {
    width: 100px;
    height: 3px;
  }
  
  .title-container {
    padding: 15px 0;
    margin-bottom: 20px;
  }
  
  .today-button {
    padding: 6px 10px;
    font-size: 0.8rem;
  }
}
</style>