<template>
    <div class="container" id="app">


        <Navbar @openUpdateAccount="showUpdateAccount = true" />

        <v-dialog v-model="showUpdateAccount" persistent width="auto">
            <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
        </v-dialog>

        <h2 class="text-center mt-4 fw-bold">نظم وقتك</h2>

        <div class="todo-container">
            <div class="test d-flex justify-content-between align-items-center mb-3">
                <div>
                    
                    <button @click="nextDay" class="btn btn-dark btn-sm rounded-btn">&#x25C0;</button>
                    <button @click="prevDay" class="btn btn-dark btn-sm rounded-btn">&#x25B6;</button>
                </div>
                <div class="date-display fw-bold">{{ formattedDate }}</div>
                <button class="btn btn-primary rounded-btn" @click="loadToday">اليوم</button>
            </div>

            <div class="scroll-box">
                <div v-for="(task, index) in currentTasks" :key="index" class="task-item d-flex align-items-center">
                    <span class="task-title">{{ task.text }}</span>
                    <input type="checkbox" class="form-check-input me-2" v-model="task.done" @change="saveTasks" />
                </div>
            </div>

            <div class="mt-3 d-flex">
                <input v-model="newTask" @keyup.enter="addTask" type="text" class="form-control me-2"
                    placeholder="أضف مهمة جديدة..." />
                <button class="btn btn-success" @click="addTask">إضافة</button>
            </div>
        </div>
        <Footer />
    </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
export default {
    name: "TimeView",
    components: {
    Navbar,
    Footer,
    UpdateAccount
  },
    data() {
        return {
            selectedDate: new Date(),
            tasksByDate: {},
            newTask: '',
            monthsArabic: [
                "جانفي", "فيفري", "مارس", "أفريل", "ماي", "جوان",
                "جويلية", "أوت", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
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
            this.selectedDate.setDate(this.selectedDate.getDate() - 1);
            this.selectedDate = new Date(this.selectedDate);
        },
        nextDay() {
            this.selectedDate.setDate(this.selectedDate.getDate() + 1);
            this.selectedDate = new Date(this.selectedDate);
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
};
</script>

<style scoped>
body {
    background-color: #96b4c4;
    font-family: 'Arial', sans-serif;
    margin: 5px;
    text-align: center;
}

h2 {
    font-size: 2rem;
    color: #0d47a1;
    margin-top: 50px;
    font-family: 'Segoe UI', sans-serif;
}
.todo-container {
    border-radius: 20px;
    padding: 50px;
    margin: 40px auto;
    max-width: 900px;
    box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.2);
}

.date-display {
    font-size: 1.3rem;
    margin-bottom: 10px;
}

.task-item {
    border-bottom: 1px solid #ccc;
    padding: 10px 0;
}

.task-item:last-child {
    border-bottom: none;
}

.task-title {
    flex-grow: 1;
    
}

.rounded-btn {
    border-radius: 10px;
    padding: 5px 10px;
    font-size: 1.3rem;
    box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
    transition: background-color 0.3s ease;
    background-color: #5aa7ca;
    margin: 0 5px;
    
}

.scroll-box {
    max-height: 300px;
    overflow-y: auto;
    
}

.test{
    margin: 20px;
    text-align: center; 
    font-size: 1.3rem;
    justify-content: center;

}
</style>