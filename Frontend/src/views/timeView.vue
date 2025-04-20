<template>
    <div class="time-manager">
      <!-- Navbar -->
      <Navbar @openUpdateAccount="showUpdateAccount = true"/>
      <v-dialog v-model="showUpdateAccount" persistent width="auto">
        <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
      </v-dialog>
      
      <!-- Main Content -->
      <div class="container app-container py-4">
        <div class="title-container text-center">
          <h1 class="app-title mb-0">نظم وقتك</h1>
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
              <div class="task-text">{{ task.text }}</div>
              <input type="checkbox" class="task-checkbox" v-model="task.done" @change="saveTasks" />
            </div>
          </div>
          
          <!-- Empty State -->
          <div v-else class="empty-state">
            <i class="mdi mdi-calendar-blank-outline"></i>
            <p>لا توجد مهام لهذا اليوم</p>
            <p class="text-muted">أضف مهمة جديدة لبدء تنظيم يومك</p>
          </div>
          
          <!-- Add Task -->
          <div class="add-task-container">
            <input v-model="newTask" @keyup.enter="addTask" type="text" class="task-input" 
                   placeholder="أضف مهمة جديدة..." />
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
    --primary-color: #5aa7ca;
    --secondary-color: #96b4c4;
    --accent-color: #0d47a1;
    --bg-light: #f8fbfd;
    --text-dark: #2c3e50;
  }
  
  .time-manager {
    font-family: 'Segoe UI', 'Arial', sans-serif;
    background: linear-gradient(135deg, var(--bg-light) 0%, #e6f7ff 100%);
    min-height: 100vh;
    color: var(--text-dark);
    direction: rtl;
  }
  
  .app-container {
    max-width: 900px;
    margin: 0 auto;
  }
  
  .title-container {
    position: relative;
    padding: 20px 0;
    margin-bottom: 30px;
  }
  
  .app-title {
    color: var(--accent-color);
    font-weight: 700;
    text-shadow: 1px 1px 3px rgba(0,0,0,0.1);
    position: relative;
    display: inline-block;
  }
  
  .app-title:after {
    content: '';
    position: absolute;
    bottom: -10px;
    left: 50%;
    transform: translateX(-50%);
    width: 80px;
    height: 4px;
    background: var(--primary-color);
    border-radius: 2px;
  }
  
  .todo-card {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 20px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    padding: 30px;
    margin-bottom: 40px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.3);
  }
  
  .date-navigator {
    background: rgba(90, 167, 202, 0.1);
    border-radius: 15px;
    padding: 15px;
    margin-bottom: 20px;
  }
  
  .date-display {
    font-size: 1.5rem;
    font-weight: 600;
    color: var(--accent-color);
    text-align: center;
    margin-left: 20px;
    margin-right: 20px;
  }
  
  .nav-button {
    background-color: #5aa7ca;
    color: white;
    border: none;
    border-radius: 10px;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    box-shadow: 0 3px 5px rgba(0, 0, 0, 0.1);
  }
  
  .nav-button:hover {
    background-color: var(--accent-color);
    transform: translateY(-2px);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
  }
  
  .today-button {
    background-color: var(--accent-color);
    color: #5aa7ca;
    border: none;
    border-radius: 10px;
    padding: 8px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    box-shadow: 0 3px 5px rgba(0, 0, 0, 0.1);
  }
  
  .today-button:hover {
    background-color: #0a3b8c;
    transform: translateY(-2px);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
  }
  
  .task-list {
    max-height: 350px;
    overflow-y: auto;
    margin: 20px 0;
    padding: 5px;
    border-radius: 10px;
    background: rgba(248, 251, 253, 0.8);
  }
  
  .task-item {
    background: white;
    border-radius: 10px;
    padding: 15px;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    transition: all 0.3s ease;
    border-right: 4px solid var(--primary-color);
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  }
  
  .task-item:hover {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
  
  .task-item.completed {
    border-right-color: #4caf50;
    opacity: 0.7;
  }
  
  .task-checkbox {
    margin-right: 15px;
    width: 22px;
    height: 22px;
    cursor: pointer;
  }
  
  .task-text {
    flex-grow: 1;
    font-size: 1.1rem;
    transition: all 0.3s ease;
  }
  
  .task-item.completed .task-text {
    text-decoration: line-through;
    color: #6c757d;
  }
  
  .add-task-container {
    display: flex;
    margin-top: 20px;
    background: white;
    border-radius: 15px;
    padding: 5px;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
  }
  
  .task-input {
    flex-grow: 1;
    border: none;
    padding: 12px 15px;
    font-size: 1.1rem;
    background: transparent;
    border-radius: 15px;
  }
  
  .task-input:focus {
    outline: none;
  }
  
  .add-button {
    background-color: var(--primary-color);
    color: white;
    border: none;
    border-radius: 10px;
    padding: 8px 20px;
    font-weight: 600;
    transition: all 0.3s ease;
    margin-right: 5px;
  }
  
  .add-button:hover {
    background-color: var(--accent-color);
  }
  
  .navbar {
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.3);
    margin-bottom: 20px;
  }
  
  .footer {
    background: rgba(255, 255, 255, 0.9);
    border-top: 1px solid rgba(255, 255, 255, 0.3);
    padding: 20px 0;
    margin-top: 40px;
    text-align: center;
    backdrop-filter: blur(10px);
  }
  
  /* Scrollbar styling */
  .task-list::-webkit-scrollbar {
    width: 8px;
  }
  
  .task-list::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
  }
  
  .task-list::-webkit-scrollbar-thumb {
    background: var(--primary-color);
    border-radius: 10px;
  }
  
  .task-list::-webkit-scrollbar-thumb:hover {
    background: var(--accent-color);
  }
  
  /* Empty state */
  .empty-state {
    text-align: center;
    padding: 30px;
    color: #6c757d;
  }
  
  .empty-state i {
    font-size: 48px;
    margin-bottom: 15px;
    color: var(--primary-color);
  }
  </style>