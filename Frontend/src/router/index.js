import { createRouter, createWebHistory } from "vue-router";
import PublicHomeView from "../views/PublicHomeView.vue";
import PrivateHomeView from "../views/PrivateHomeView.vue";
import AvantTest from "../views/AvantTest.vue";
import Test from "../views/Test.vue";
import EntranceView from "@/views/EntranceView.vue";
import DetailsPersonnality from "@/views/DetailsPersonnality.vue";

const routes = [
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
