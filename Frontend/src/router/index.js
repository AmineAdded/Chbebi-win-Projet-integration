import { createRouter, createWebHistory } from "vue-router";
import PublicHomeView from "../views/PublicHomeView.vue";
import PrivateHomeView from "../views/PrivateHomeView.vue";
import AvantTest from "../views/AvantTest.vue";
import Test from "../views/Test.vue";
import EntranceView from "@/views/EntranceView.vue";
import DetailsPersonnality from "@/views/DetailsPersonnality.vue";

import AdminDashboard from "@/views/AdminDashboard.vue";
import SuperAdminVerify from "@/views/SuperAdminVerify.vue";

const routes = [
  { path: "/admin", component: AdminDashboard ,name:"admin", meta: { requiresAuthentication: true }},

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
  } ,
  {
    path: '/islam',
    name: 'Islam',
    component: () => import('@/views/IslamicView.vue')
  }  ,
  {
    path: '/agenda',
    name: 'AgendaView',
    component: () => import('@/views/AgendaView.vue')
  }  ,
  {
    path: '/time-management',
    name: 'timeView',
    component: () => import('@/views/timeView.vue')
  }  ,
  {
    path: '/islamic/:topicId',
    name: 'TopicChaptersView',
    component: () => import('@/views/TopicChaptersView.vue'),
    props: true,
  },
  {
    path: '/super-admin-verify',
    component: SuperAdminVerify,
    meta: { requiresAuthentication: true }
  },
  {
    path: '/super-admin',
    component: () => import('@/views/SuperAdminView.vue'),
    meta: { 
      requiresAuth: true,
      requiresSuperAdmin: true
    }
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
router.beforeEach((to, from, next) => {
  const accessToken = localStorage.getItem("accessToken");
  const role = localStorage.getItem("role"); 

  // Route Admin
  if (to.path === "/admin") {
    if (accessToken && (role ==1|| role ==2)) {
      next(); // admin autorisé
    } else if (accessToken && (role ==0 )) {
      next({ name: "privatehome" }); // utilisateur redirigé vers sa page
    } else {
      next({ name: "publichome" }); // non connecté
    }
  }

  // Route PrivateUser
  else if (to.path === "/privatehome") {
    if (accessToken && role ==0) {
      next(); // utilisateur autorisé
    } else if (accessToken && (role ==1 || role ==2)) {
      next({ name: "admin" }); // admin redirigé vers dashboard
    } else {
      next({ name: "publichome" }); // non connecté
    }
  }

  // Autres routes
  else {
    next(); // accès libre
  }
});




export default router;
