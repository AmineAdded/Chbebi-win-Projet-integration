<template>
    <div class="chapters-container">
      <Navbar />
  
      <v-container fluid>
        <h2 class="section-title">الفصول الخاصة بموضوع: {{ topicTitle }}</h2>
        <div class="cards-wrapper">
          <div
            v-for="(chapter, index) in chapters"
            :key="index"
            class="card"
            @click="openModal(chapter)"
          >
            <img :src="chapter.image" alt="Chapter" class="card-img" />
            <div class="card-body">
              <h3 class="card-title">{{ chapter.title }}</h3>
            </div>
          </div>
        </div>
      </v-container>
  
      <v-dialog v-model="showModal" max-width="800">
        <div class="popup-content">
          <button class="close-btn" @click="showModal = false">X</button>
          <iframe
            class="video-frame"
            :src="selectedChapter.video"
            frameborder="0"
            allowfullscreen
          ></iframe>
          <div class="description">
            <p>{{ selectedChapter.description }}</p>
          </div>
          <a
            class="download-btn"
            :href="selectedChapter.pdf"
            target="_blank"
            download
          >
            تحميل الملف PDF
          </a>
        </div>
      </v-dialog>
  
      <Footer />
    </div>
  </template>
  
  <script>
  import Navbar from "@/components/Navbar.vue";
  import Footer from "@/components/Footer.vue";
  
  export default {
    name: "TopicChaptersView",
    components: { Navbar, Footer },
    data() {
      return {
        topicId: this.$route.params.topicId,
        topicTitle: "",
        showModal: false,
        selectedChapter: {},
        allChapters: {
          "arkan-islam": [
            {
              title: "الشهادة",
              video: "https://www.youtube.com/embed/VIDEO_ID_1",
              description: "شرح مفصل عن الشهادة وأهميتها في الإسلام.",
              pdf: "/pdfs/shahada.pdf",
              image: "https://cdn.pixabay.com/photo/2017/01/20/00/30/mosque-1998433_1280.jpg",
            },
            {
              title: "الصلاة",
              video: "https://www.youtube.com/embed/VIDEO_ID_2",
              description: "كل ما تحتاج معرفته عن الصلاة.",
              pdf: "/pdfs/salat.pdf",
              image: "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_1280.jpg",
            },
          ],
          "kisas-anbiya": [
            {
              title: "قصة سيدنا يوسف",
              video: "https://www.youtube.com/embed/VIDEO_ID_3",
              description: "قصة سيدنا يوسف عليه السلام مع إخوته.",
              pdf: "/pdfs/yusuf.pdf",
              image: "https://cdn.pixabay.com/photo/2016/11/29/06/18/fantasy-1867596_1280.jpg",
            },
          ],
          // Add more topics with chapters
        },
      };
    },
    computed: {
      chapters() {
        return this.allChapters[this.topicId] || [];
      },
    },
    mounted() {
      const topic = {
        "arkan-islam": "أركان الإسلام",
        "kisas-anbiya": "قصص الأنبياء",
        "akhlaq-muslim": "أخلاق المسلم",
        "koran-karim": "القرآن الكريم",
      };
      this.topicTitle = topic[this.topicId] || "غير معروف";
    },
    methods: {
      openModal(chapter) {
        this.selectedChapter = chapter;
        this.showModal = true;
      },
    },
  };
  </script>
  
  <style scoped>
  .chapters-container {
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
    width: 280px;
    background-color: white;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    transition: transform 0.3s ease;
    cursor: pointer;
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
  
  /* Modal Styling */
  .popup-content {
    background: #fff;
    padding: 20px;
    border-radius: 12px;
    position: relative;
    max-height: 90vh;
    overflow-y: auto;
  }
  
  .close-btn {
    position: absolute;
    top: 10px;
    left: 10px;
    background-color: #c62828;
    color: white;
    border: none;
    border-radius: 8px;
    padding: 5px 12px;
    cursor: pointer;
  }
  
  .video-frame {
    width: 100%;
    height: 400px;
    border-radius: 12px;
    margin-bottom: 20px;
  }
  
  .description {
    font-size: 1rem;
    color: #333;
    line-height: 1.6;
    margin-bottom: 20px;
  }
  
  .download-btn {
    display: inline-block;
    padding: 10px 20px;
    background-color: #0d47a1;
    color: white;
    border-radius: 8px;
    text-decoration: none;
    font-weight: bold;
    transition: background-color 0.3s ease;
  }
  
  .download-btn:hover {
    background-color: #1565c0;
  }
  
  /* Responsive */
  @media (max-width: 768px) {
    .video-frame {
      height: 250px;
    }
  }
  </style>
  