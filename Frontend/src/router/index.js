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
    path: '/islamic/:topicId',
    name: 'TopicChaptersView',
    component: () => import('@/views/TopicChaptersView.vue'),
    props: true,
  }
  
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
