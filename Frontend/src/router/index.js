import { createRouter, createWebHistory } from "vue-router";
import PublicHomeView from "../views/PublicHomeView.vue";
import PrivateHomeView from "../views/PrivateHomeView.vue";
import AvantTest from "../views/AvantTest.vue";
import Test from "../views/Test.vue";
import EntranceView from "@/views/EntranceView.vue";
import DetailsPersonnality from "@/views/DetailsPersonnality.vue";

import AdminDashboard from "@/AdminDashboard.vue";
import UsersTable from "@/components/admin/UsersTable.vue";
import ChaptersTable from "@/components/admin/ChaptersTable.vue";
import QuizzesTable from "@/components/admin/QuizzesTable.vue";
import TestsTable from "@/components/admin/TestsTable.vue";
import QuotesTable from "@/components/admin/QuotesTable.vue";
import AnalyticsChart from "@/components/admin/AnalyticsChart.vue";

const routes = [
  { path: "/admin", component: AdminDashboard },
  { path: "/admin/users", component: UsersTable },
  { path: "/admin/chapters", component: ChaptersTable },
  { path: "/admin/quizzes", component: QuizzesTable },
  { path: "/admin/tests", component: TestsTable },
  { path: "/admin/quotes", component: QuotesTable },
  { path: "/admin/analytics", component: AnalyticsChart },



  {
    path: "/",
    name: "publichome",
    component: PublicHomeView,
  },
  {
    path: "/privatehome",
    name: "privatehome",
    component: PrivateHomeView,
  },
  {
    path: "/AvantTest",
    name: "avantTest",
    component: AvantTest,
  },
  {
    path: "/Test",
    name: "Test",
    component: Test,
  },
  {
    path: "/Entrance",
    name: "Entrance",
    component: EntranceView,
  },
  {
    path: "/DetailsPersonnality",
    name: "DetailsPersonnality",
    component: DetailsPersonnality,
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
