<template>
  <div class="islamic-container">
    <Navbar @openUpdateAccount="showUpdateAccount = true" />
    
    <v-dialog v-model="showUpdateAccount" persistent width="auto">
      <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
    </v-dialog>


    <v-container fluid>
      <h2 class="section-title">مواضيع دينية</h2>
      <div class="cards-wrapper">
        
        <div v-for="(topic, index) in topics" :key="index" class="card ">
          <router-link
  :to="{ name: 'TopicChaptersView', params: { topicId: topic.id } }"
  class="card">
          <img :src="topic.image" alt="Islamic Topic" class="card-img" />
          <div class="card-body">
            <h3 class="card-title">{{ topic.title }}</h3>
            <p class="card-desc">{{ topic.description }}</p>
          </div>
        </router-link>
        </div>
      </div>
    </v-container>

    <Footer />
  </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";

export default {
  name: "IslamicView",
  components: {
    Navbar,
    Footer,
    UpdateAccount
  },
  data() {
    return {
      showUpdateAccount: false,
      topics: [
        {
          id:"arkan-islam",
          title: "أركان الإسلام",
          description: "تعرف على الركائز الخمسة التي يقوم عليها دين الإسلام.",
          image: "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
        },
        {
          id:"kisas-anbiya",
          title: "قصص الأنبياء",
          description: "قصص ممتعة ومؤثرة من حياة أنبياء الله عليهم السلام.",
          image: "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
        },
        {
          id:"akhlaq-muslim",
          title: "أخلاق المسلم",
          description: "تعرّف على الأخلاق الحميدة التي يجب أن يتحلى بها المسلم.",
          image: "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
        },
        {
          id:"koran-karim",
          title: "القرآن الكريم",
          description: "اكتشف إعجاز القرآن وأهمية تلاوته وفهمه.",
          image: "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
        },
        
      ],
    };
  },
};
</script>

<style scoped>
.islamic-container {
  background-color: #e3f2fd;
  min-height: 100vh;
  direction: rtl;
  padding-bottom: 30px;
}

.section-title {
  text-align: center;
  color: #0d47a1;
  margin-top: 30px;
  font-size: 2rem;
  font-weight: bold;
}

.cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 25px;
  padding: 40px 20px;
}

.card {
  text-decoration: none;

  width: 300px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
}

.card-img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.card-body {
  padding: 20px;
}

.card-title {
  font-size: 1.3rem;
  color: #1565c0;
  margin-bottom: 10px;
}

.card-desc {
  font-size: 1rem;
  color: #424242;
  line-height: 1.5;
}
a.no-style {
  text-decoration: none;
  color: inherit;
}

/* Responsive */
@media (max-width: 768px) {
  .cards-wrapper {
    flex-direction: column;
    align-items: center;
  }
}
</style>
