import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AvantTest from "../views/AvantTest.vue";
import Test from "../views/Test.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
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
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
