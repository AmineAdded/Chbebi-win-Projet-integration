<template>
  <div class="dashboard-container">
    <!-- Sidebar -->
     <div class="nav"><Sidebar /></div>
    

    <!-- Main Content -->
    <div class="main-content">
      <DashboardOverview />

      <UsersTable /><br><br>
      <ChaptersTable /><br><br>
      <QuizzesTable /><br><br>
      <TestsTable /><br><br>
      <QuotesTable /><br><br>
      <AnalyticsChart /><br>
    </div>
  </div>
</template>

<script>
import Sidebar from "@/components/admin/Sidebar.vue";
import DashboardOverview from "@/components/admin/DashboardOverview.vue";
import UsersTable from "@/components/admin/UsersTable.vue";
import ChaptersTable from "@/components/admin/ChaptersTable.vue";
import QuizzesTable from "@/components/admin/QuizzesTable.vue";
import TestsTable from "@/components/admin/TestsTable.vue";
import QuotesTable from "@/components/admin/QuotesTable.vue";
import AnalyticsChart from "@/components/admin/AnalyticsChart.vue";

export default {
  components: {
    Sidebar,
    DashboardOverview,
    UsersTable,
    ChaptersTable,
    QuizzesTable,
    TestsTable,
    QuotesTable,
    AnalyticsChart,
  },
};
</script>

<style scoped>
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
