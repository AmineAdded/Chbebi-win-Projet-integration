<template>
    <v-container fluid class="how-to-use-container">
      <h2 class="section-title">كيفاش نستعمل تطبيقنا</h2>
  <br>
      <v-carousel 
        hide-delimiters 
        show-arrows="hover" 
        cycle
        class="carousel"
        :continuous="true"
        height="400"
      >
        <v-carousel-item v-for="(chunk, index) in chunkedItems" :key="index">
          <div class="carousel-slide">
            <v-card 
              v-for="(item, i) in chunk" 
              :key="i" 
              class="slide-card animate-slide"
            >
              <v-img :src="item.image" class="slide-image"></v-img>
              <v-card-title class="slide-title">
                {{ item.title }}
                <span class="step-number">{{ item.step }}</span>
              </v-card-title>
              <v-card-text class="slide-text">
                {{ item.text }}
              </v-card-text>
            </v-card>
          </div>
        </v-carousel-item>
      </v-carousel>
    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        items: [
          {
            step: "②",
            title: "التعلّم والتثقف",
            text: "مواضيع متنوّعة على الدين، تاريخ تونس، قضايا معاصرة، الكلّ بطريقة مبسّطة وتفاعلية.",
            image: require("@/assets/symbols-come-out-bulb-top-book.jpg"),
          },
          {
            step: "①",
            title: "اكتشاف ذاتك",
            text: "تلقى أسئلة وأدوات تعاونك تفهم روحك خير وتحدّد شنوة تحب في حياتك",
            image: require("@/assets/still-life-illustrating-ethics-concept.jpg"),
          },
          {
            step: "④",
            title: "Chatbot المحادثة مع",
            text: "تنجّم تتفاعل مع نظام ذكي يجاوبك على أسئلتك، يعطيك نصائح، ويعاونك في التنظيم والتخطيط بطريقة تفاعلية",
            image: require("@/assets/Chatbot Conversation.jpg"),
          },
          {
            step: "③",
            title: "إدارة الوقت والأهداف",
            text: "تنجّم تضبط أهدافك وتحطّ خطّة توصلهم بطريقة منظّمة وسهلة",
            image: require("@/assets/AdobeStock_580251118_Preview.jpeg"),
          }
        ],
      };
    },
    computed: {
      chunkedItems() {
        const chunkSize = 2;
        let result = [];
        for (let i = 0; i < this.items.length; i += chunkSize) {
          result.push(this.items.slice(i, i + chunkSize));
        }
        return result;
      },
    },
  };
  </script>
  
  <style scoped>
  .how-to-use-container {
    background-color: #91B5C9;
    text-align: center;
    padding: 40px 0;
    
  }
  
  .section-title {
    font-size: 2.3rem;
    font-weight: bold;
    color: #152538;
    font-family: 'Segoe UI';
    margin-bottom: 5%;
  }
  
  .carousel {
    max-width: 1000px;
    margin: auto;
  }
  
  .carousel-slide {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-bottom: 2px;
    padding-right: 20px; 
    padding-left: 20px;
  }
  
  .slide-card {
    width: 350px;
    border-radius: 16px;
    transition: transform 0.5s ease-in-out;
  }
  
  .slide-card:hover {
    transform: scale(1.01);
  }
  
  .slide-image {
    height: 220px;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    margin-top: 7px;
  }
  
  .slide-title {
    font-size: 1.2rem;
    font-weight: bold;
    color: #152538;
    display: flex;
    justify-content: center;
    align-items: center;
    font-family: 'Segoe UI';
  }
  
  .step-number {
    background-color: #27c4f5;
    color: white;
    font-size: 1.2rem;
    font-weight: bold;
    padding: 5px 12px;
    border-radius: 50%;
    margin-left: 10px;
    font-family: 'Segoe UI';
  }
  
  .slide-text {
    font-size: 1rem;
    color: #333;
    text-align: center;
    padding: 15px;
    font-family: 'Segoe UI';
  }
  
  /* Animation fluide de droite à gauche */
  .animate-slide {
    animation: slideIn 1s ease-in-out;
  }
  
  @keyframes slideIn {
    from {
      opacity: 0;
      transform: translateX(50px);
    }
    to {
      opacity: 1;
      transform: translateX(0);
    }
  }

  @media (max-width: 768px) {
    .carousel-slide {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-bottom: 2px;
    padding-right: 10px; 
    padding-left: 10px;
  }
  
  .slide-image {
    height: 200px;
    margin-top: 9px;
    margin-left: 9px;
    margin-right: 9px;
  }
 
  .step-number {
    background-color: #27c4f5;
    color: white;
    font-size: 1rem;
    font-weight:normal;
    padding: 2px 9px;
    border-radius: 50%;
    margin-left: 2px;
    font-family: 'Segoe UI';
  }
  }
  </style>
  