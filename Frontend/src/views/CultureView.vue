<template>
    <div class="container">
      <Navbar />
  
      <v-container fluid>
        <h2 class="section-title">الثقافة التونسية</h2>
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
  
  export default {
    name: "CultureView",
    components: {
      Navbar,
      Footer,
    },
    data() {
      return {
        topics: [
          {
            id: "cha3bi-music",
            title: "الموسيقى الشعبية",
            description: "تعرف على الأنماط الموسيقية التراثية في تونس مثل المزود والصالحي.",
            image: "https://cdn.pixabay.com/photo/2015/09/05/21/51/arabic-925135_1280.jpg",
          },
          {
            id: "lebsa-tounsia",
            title: "اللبسة التقليدية",
            description: "اللباس التقليدي التونسي المميز مثل الجبة والبلغة والكسوة.",
            image: "https://cdn.pixabay.com/photo/2020/11/20/09/32/tunisia-5758510_1280.jpg",
          },
          {
            id: "medina-tunis",
            title: "المدينة العتيقة",
            description: "استكشف سحر المدينة العتيقة في قلب تونس العاصمة.",
            image: "https://cdn.pixabay.com/photo/2017/09/26/13/40/sidi-bou-said-2786396_1280.jpg",
          },
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  .container {
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