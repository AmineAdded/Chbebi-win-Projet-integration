<template>
  <v-container fluid class="personality-container">
    <v-card class="personality-card" elevation="0">
      <div class="personality-header">
        تعرف على شخصيتك أكثر
      </div>
      <div class="content">
        <div class="text-section">
          <div class="personality-type"><br>
            <span class="label">النوع الشخصي  </span>
            <span class="thinker">{{ Personnalite.nomEnglish }}</span>
          </div>
          <p class="description">
            {{ Personnalite.contenu }}
          </p>
          <a href="DetailsPersonnality" class="more-link">أعرف أكثر</a>
        </div>
        <div class="image-section">
          <img v-if="imageUrl" :src="imageUrl" alt="Thinker Icon" class="thinker-icon" />
          <img v-else src="@/assets/thinker-icon.png" alt="Default Icon" class="thinker-icon" />
        </div>
      </div>
    </v-card>
  </v-container>
</template>

<script>
import PersonnaliteService from "@/Services/PersonnaliteService";
import { useUserStore } from '../store/User/userStore.js';

export default {
  name: "Personality",
  data() {
    return {
      Personnalite: {},
      imageUrl: null
    }
  },
  mounted() {
    this.getPersonnalite();
  },
  methods: {
    async getPersonnalite() {
      try {
        const store = useUserStore();
        const personnalite_id = store.user.personnalite_id;
        
        this.Personnalite = await PersonnaliteService.getAllCriteres(personnalite_id);
      
        if (this.Personnalite && this.Personnalite.image) {
          try {
            this.imageUrl = require(`@/assets/${this.Personnalite.image}`);
          } catch (error) {
            console.error("Image introuvable:", error);
          }
        }
      } catch (err) {
        console.log("Erreur lors du chargement de la personnalité:", err);
      }
    }
  }
};
</script>

<style scoped>
.personality-container {
  padding: 0;
  
}

.personality-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.personality-header {
    background-color: #5285a2; /* Match the blue from the image */
    color: #000000;
    text-align: center;
    padding: 8px 15px;
    font-weight: bold;
    font-size: 1.9rem;
    border-bottom: 1px solid #e0e0e0;
}

.thinker {
    color: blue; /* Make sure Thinker text is blue */
    font-weight: bold;
    font-size: 1.2rem;
}

.more-link {
    color: red; /* Make link red as in the image */
    font-weight: bold;
    font-size: 0.9rem;
}

/* Add styling for the thinker icon */
.thinker-icon {
    width: 60px;
    height: auto;
}

.content {
  display: flex;
  padding: 10px;
  gap: 10px;
  flex: 1;
}


.text-section {
  flex: 3;
  display: flex;
  flex-direction: column;
  text-align: right;
}

.personality-type {
  margin-bottom: 10px;
}

.label {
  font-weight: bold;
  color: #333;
  margin-left: 5px;
  font-size: 1.1rem;
}



.description {
  margin-bottom: 15px;
  line-height: 1.5;
  font-size: 0.9rem;
}



.more-link:hover {
  text-decoration: underline;
}

.image-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}


</style>
