import { createRouter, createWebHistory } from "vue-router";
import PublicHomeView from "../views/PublicHomeView.vue";
import PrivateHomeView from "../views/PrivateHomeView.vue";
import AvantTest from "../views/AvantTest.vue";
import Test from "../views/Test.vue";
import EntranceView from "@/views/EntranceView.vue";
import DetailsPersonnality from "@/views/DetailsPersonnality.vue";

import AdminDashboard from "@/views/AdminDashboard.vue";

const routes = [
  { path: "/admin", component: AdminDashboard },

  {
    path: "/",
    name: "publichome",
    component: PublicHomeView,
  },
  {
    path: "/privatehome",
    name: "privatehome",
    component: PrivateHomeView,
    meta: { requiresAuth: true }
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
  },
  {
    path: '/reset-password',
    name: 'ResetPassword',
    component: () => import('@/views/ResetPassword.vue')
  },
  {
    path: '/password',
    name: 'Password',
    component: () => import('@/components/ForgotPassword.vue')
  }  
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
router.beforeEach((to, from, next) => {
  const accessToken = localStorage.getItem("accessToken");

  if (to.meta.requiresAuth && !accessToken) {
    next({ name: "publichome" });
  } else {
    next();
  }
});


export default router;
