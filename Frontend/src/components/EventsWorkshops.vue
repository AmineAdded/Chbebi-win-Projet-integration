<template>
  <v-card class="events-card pa-6">
    <h2 class="section-title mb-4">الفعاليات وورش العمل القراب</h2>
    
    <div class="event-list">
      <div v-for="(event, index) in events" :key="index" class="event-item">
        <div class="event-card">
          <div class="event-content-wrapper">
            <div class="event-image mt-2">
              <img :src="event.image" alt="Event image" class="rounded-lg" />
            </div>
            <div class="event-info">
              <div class="d-flex justify-space-between align-center">
                <div class="event-title font-weight-bold">{{ event.title }}</div>
                <div class="event-date primary--text">{{ event.date }}</div>
              </div>
              <div class="event-description mt-2 text-right">{{ event.description }}</div>
              <div class="mt-3 text-left">
                <v-btn color="success" class="learn-more white--text px-2">
                  تعرف أكثر <v-icon small class="mr-1">mdi-chevron-left</v-icon>
                </v-btn>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  name: 'EventsWorkshops',
  data() {
    return {
      events: [
        {
          title: '🔥 Code It Up 5.0 – Naruto Edition 🔥',
          date: '26-27 أبريل 2025',
          image: require('@/assets/481352016_627745519986335_674803419184381753_n.jpg'),
          description: 'كل ماهو متعلق بهذا ال event هو أنه يكون 24 hour hackathon من تنظيم IEEE CS Chapter ISET Bizerte hackathon يجمع كل فرق من الطلبة و ‫#‏الشباب‬ المشاركين يتحداون في حل مشاكل برمجية وذلك خلال تقنية في وقت محدود.'
        },
        {
          title: 'Bizerte Tcodi',
          date: '06 أبريل 2025',
          image: require('@/assets/434396316_1769285730240893_6600856576318328781_n.jpg'),
          description: 'أول دورة لحدث hackathon كان Bizerte Tcodi من تنظيم IEEE CS Chapter ISET Bizerte بالشراكة مع IEEE CS ISSATM و IEEE CS ENIB. حدث 6-hour problem-solving hackathon 17 ال النسخة الأولى كانت 🔥 مشاركين من مختلف المؤسسات. الهدف منها كان تمرين مهارات problem-solving و algorithmic thinking للطلبة وتحضيرهم لمسابقات عالمية كيف.'
        }
      ]
    };
  },
  methods: {
    showDetails(event) {
      console.log('Show details for:', event.title);
    }
  }
}
</script>

<style scoped>
.events-card {
  background: white !important;

}

.section-title {
  color: #333;
  font-size: 2.5rem;
  text-align: right;
  font-weight: bold;
  position: relative;
  padding-bottom: 10px;
  border-bottom: 2px solid #e0e0e0;
  width: 100%;
  margin-bottom: 20px;
  text-align: center;
}

.event-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.event-item {
  overflow: hidden;
}

.event-card {
  background: white;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.event-content-wrapper {
  display: flex;
  flex-direction: row;
}

.event-image {
  width: 40%;
  padding-right: 100px;
}

.event-image img {
  margin-top: 20px;
  width: 70%;
  height: 70%;
  object-fit: cover;
}

.event-info {
  width: 60%;
  padding: 20px;
  padding-left: 100px;
  padding-top: 50px;
}

.event-date {
  color: #4F46E5;
  font-weight: bold;
  font-size: 0.9rem;
}

.event-title {
  font-size: 1.1rem;
  font-weight: bold;
  color: #333;
}

.event-description {
  color: #666;
  font-size: 1rem;
  margin-top: 1px;
  font-weight: 600;
}

.learn-more {
  font-weight: 500;
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .event-content-wrapper {
    flex-direction: column;
  }
  
 .event-info {
    width: 100%;
    padding: 20px;
  }
 
  .event-image{
    display: none;
  }
}
</style>