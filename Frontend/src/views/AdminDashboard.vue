<template>
  <div v-if="isLoading" class="loader-container">
    <div class="loader-content">
      <div class="custom-loader">
        <div class="pulse-ring"></div>
        <div class="pulse-dot"></div>
      </div>
      <div class="mt-4 text-center loading-text">جاري التحميل...</div>
    </div>
  </div>

  <div v-else class="dashboard-container">
    <!-- Sidebar -->
    <div class="nav">
      <Sidebar
        @showUpdateAccount="show = true"
        @menuSelected="scrollToSection"
      />
    </div>

    <!-- Main Content -->
    <div class="main-content">
      <DashboardOverview />

      <div ref="chaptersSection">
        <ChaptersTable />
      </div>
      <br /><br />

      <div ref="quizzesSection">
        <QuizzesTable />
      </div>
      <br /><br />

      <div ref="testsSection">
        <TestsTable />
      </div>
      <br /><br />

      <div ref="quotesSection">
        <QuotesTable />
      </div>
      <br /><br />

      <div ref="workshopSection">
        <WorkshopTable />
      </div>
      <br /><br />

      <div ref="analyticsSection">
        <AnalyticsChart />
      </div>

      <v-dialog v-model="show" persistent width="auto">
        <UpdateAccount @closeUpdateAccount="show = false" />
      </v-dialog>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/admin/Sidebar.vue";
import DashboardOverview from "@/components/admin/DashboardOverview.vue";
import ChaptersTable from "@/components/admin/ChaptersTable.vue";
import TestsTable from "@/components/admin/TestsTable.vue";
import QuotesTable from "@/components/admin/QuotesTable.vue";
import AnalyticsChart from "@/components/admin/AnalyticsChart.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";
import WorkshopTable from "@/components/admin/WorkshopTable.vue";

export default {
  components: {
    Sidebar,
    DashboardOverview,
    ChaptersTable,
    TestsTable,
    QuotesTable,
    AnalyticsChart,
    UpdateAccount,
    WorkshopTable
  },
  name: "AdminView",
  data() {
    return {
      isLoading: true,
      show: false,
    };
  },
  created() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1000);
  },
  methods: {
    scrollToSection(section) {
      const sectionRefMap = {
        classes: "chaptersSection",
        surveys: "quizzesSection",
        tests: "testsSection",
        quotes: "quotesSection",
        analytics: "analyticsSection",
        workshops: "workshopSection",
      };

      const refName = sectionRefMap[section];
      if (refName && this.$refs[refName]) {
        this.$refs[refName].scrollIntoView({ behavior: "smooth" });
      }
    },
  },
};
</script>

<style scoped>
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
@keyframes pulse {
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
  animation: pulse 2s ease-out infinite;
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
.dashboard-container {
  display: flex;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  background: white;
  margin-right: 250px;
  width: calc(100% - 270px);
}

@media (max-width: 900px) {
  .sidebar {
    display: none;
  }
  .main-content {
    margin-right: 0px;
    width: 100%;
  }
  .nav {
    display: none;
  }
}
</style>
