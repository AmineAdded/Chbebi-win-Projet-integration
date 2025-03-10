<template>
  <v-container fluid class="agenda-container">
    <v-card class="agenda-card" elevation="0">
      <div class="month-navigation">
        <button class="nav-button" @click="previousMonth">&lt;</button>
        <h3 class="month-title">{{ currentMonth }} {{ currentYear }}</h3>
        <button class="nav-button" @click="nextMonth">&gt;</button>
      </div>
      
      <div class="calendar-wrapper">
        <div class="weekday-row">
          <div v-for="day in weekDays" :key="day" class="weekday">
            {{ day }}
          </div>
        </div>
        
        <div class="calendar">
          <div 
            v-for="day in calendarDays" 
            :key="day.fullDate" 
            class="day" 
            :class="{ 
              'today': day.isToday, 
              'other-month': !day.isCurrentMonth 
            }"
          >
            {{ day.date }}
          </div>
        </div>
      </div>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "Agenda",
  data() {
    return {
      currentDate: new Date(),
      weekDays: [],
    };
  },
  computed: {
    currentMonth() {
      return new Intl.DateTimeFormat("ar", { month: "long" }).format(this.currentDate);
    },
    currentYear() {
      return this.currentDate.getFullYear();
    },
    calendarDays() {
      const days = [];
      const year = this.currentDate.getFullYear();
      const month = this.currentDate.getMonth();
      
      // First day of the month
      const firstDay = new Date(year, month, 1);
      // Last day of the month
      // const lastDay = new Date(year, month + 1, 0);
      
      // Get the day of the week for the first day (0 = Sunday, 1 = Monday, etc.)
      let firstDayOfWeek = firstDay.getDay();
      if (firstDayOfWeek === 0) firstDayOfWeek = 7; // Make Sunday the 7th day
      
      // Calculate the date of the first calendar day (may be in the previous month)
      const firstCalendarDay = new Date(firstDay);
      firstCalendarDay.setDate(firstCalendarDay.getDate() - (firstDayOfWeek - 1));
      
      // Generate 42 days (6 weeks) for the calendar
      const today = new Date();
      
      for (let i = 0; i < 42; i++) {
        const currentDate = new Date(firstCalendarDay);
        currentDate.setDate(firstCalendarDay.getDate() + i);
        
        days.push({
          date: currentDate.getDate(),
          fullDate: currentDate.toISOString(),
          isCurrentMonth: currentDate.getMonth() === month,
          isToday: 
            currentDate.getDate() === today.getDate() && 
            currentDate.getMonth() === today.getMonth() && 
            currentDate.getFullYear() === today.getFullYear()
        });
      }
      
      return days;
    }
  },
  mounted() {
    this.initWeekdays();
  },
  methods: {
    initWeekdays() {
      // Initialize weekday names in Arabic
      this.weekDays = ['السبت', 'الأحد', 'الإثنين', 'الثلاثاء', 'الأربعاء', 'الخميس', 'الجمعة'];
    },
    nextMonth() {
      const newDate = new Date(this.currentDate);
      newDate.setMonth(newDate.getMonth() + 1);
      this.currentDate = newDate;
    },
    previousMonth() {
      const newDate = new Date(this.currentDate);
      newDate.setMonth(newDate.getMonth() - 1);
      this.currentDate = newDate;
    }
  }
};
</script>

<style scoped>
/* Add these styles to make it more compact */
.agenda-container {
  padding: 0;
}

.agenda-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.month-navigation {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 15px;
    background-color: #b2cfe0; /* Match the blue from the image */
    border-bottom: 1px solid #e0e0e0;
}

.day.today {
    background-color: #ffff00; /* Bright yellow for today */
}

/* Make the navigation arrows more visible */
.nav-button {
    background-color: white;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    font-weight: bold;
    color: #333;
    border: 1px solid #e0e0e0;
}

.month-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin: 0;
  color: #333;
}

.calendar-wrapper {
  padding: 10px;
}

.weekday-row {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  margin-bottom: 5px;
}

.weekday {
  text-align: center;
  font-size: 0.75rem;
  font-weight: bold;
  padding: 2px 0;
  color: #666;
}

.calendar {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0;
}

.day {
  height: 25px; /* Make days smaller */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  border: 1px solid #eee;
}

.today {
  background-color: #ffff00;
  font-weight: bold;
}

.other-month {
  color: #ccc;
}


</style>


