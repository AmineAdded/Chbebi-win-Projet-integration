<template>
  <v-container class="note-container">
    <div class="close-icon">
      <v-btn
        rounded="pill"
        @click.prevent="$emit('closeRate')"
      >
        ❌
      </v-btn>
    </div>
    <!-- Titre avec icône alignés côte à côte -->
    <v-row justify="center" align="center">
      <v-col cols="auto" class="text-center">
        <h2 class="note-title">أعطينا رايك</h2>
      </v-col>
      <v-col cols="auto">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
          width="50"
          height="50"
          viewBox="0 0 96 96"
        >
          <image
            id="NoPath_-_Copie_8_"
            data-name="NoPath - Copie (8)"
            width="100"
            height="100"
            xlink:href="data:image/png;base64,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MjAyMy0xMS0xM1QxMjo0MjowOSswMDowMPXovUwAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMjMtMTEtMTNUMTI6NDI6MDkrMDA6MDCEtQXwAAAAIHRFWHRzb2Z0d2FyZQBodHRwczovL2ltYWdlbWFnaWNrLm9yZ7zPHZ0AAAAYdEVYdFRodW1iOjpEb2N1bWVudDo6UGFnZXMAMaf/uy8AAAAYdEVYdFRodW1iOjpJbWFnZTo6SGVpZ2h0ADUxMo+NU4EAAAAXdEVYdFRodW1iOjpJbWFnZTo6V2lkdGgANTEyHHwD3AAAABl0RVh0VGh1bWI6Ok1pbWV0eXBlAGltYWdlL3BuZz+yVk4AAAAXdEVYdFRodW1iOjpNVGltZQAxNjk5ODc5MzI54g9UEwAAABN0RVh0VGh1bWI6OlNpemUAMTEwODNCQhm3IfgAAAB5dEVYdFRodW1iOjpVUkkAZmlsZTovLy4vdXBsb2Fkcy81Ni9rekZRWTEwLzQwNzYvaW5zdGFncmFtX2xpa2VfbG92ZV9tZXNzYWdlX2ZlZWRiYWNrX2NoYXRfc3BlZWNoX2J1YmJsZV9oZWFydF9pY29uXzI1ODkzMS5wbmdE6OowAAAAAElFTkSuQmCC"
          />
        </svg>
      </v-col>
    </v-row>

    <!-- Zone de texte avec icône -->
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-textarea
          v-model="feedback"
          label="...اكتب تعليقك هنا"
          variant="outlined"
          rows="3"
          prepend-inner-icon="mdi-pencil"
          class="custom-textarea"
        ></v-textarea>
      </v-col>
    </v-row>

    <!-- Barre d'évaluation avec étoile qui se déplace -->
    <v-row justify="center">
      <v-col cols="12" md="8" class="text-center">
        <v-rating
          v-model="rating"
          color="gold darken-3"
          background-color="grey lighten-2"
          dense
          hover
        ></v-rating>
      </v-col>
    </v-row>

    <!-- Bouton d'envoi -->
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <v-btn color="primary" @click="submitFeedback" large class="bottom-icon">
          <v-icon left>mdi-send</v-icon> أبعث
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>
  
<script>
export default {
  data() {
    return {
      feedback: "",
      rating: 3, // Valeur initiale de l'évaluation
    };
  },
  methods: {
    submitFeedback() {
      if (!this.feedback.trim()) {
        alert("الرجاء كتابة تعليق قبل الإرسال.");
        return;
      }

      // Simuler l'envoi des données
      console.log("Feedback envoyé :", {
        feedback: this.feedback,
        rating: this.rating,
      });

      // Réinitialisation des valeurs après l'envoi
      this.feedback = "";
      this.rating = 3;
      alert("شكرا على تعليقك!");
    },
  },
};
</script>
  
  <style scoped>
.note-container {
  background-color: #eaedf1;
  padding: 20px;
  border: 2px solid #0277bd;
  border-radius: 12px;
  box-shadow: 0px 10px 20px rgba(0, 0, 0, 0.336);
  max-width: 600px;
  margin: auto;
  margin-top: 20px;
  margin-bottom: 20px;
  font-family: "Segoe UI";
}

.note-title {
  font-size: 28px;
  font-weight: bold;
  color: #152538;
  margin-top: 1px;
  display: inline-block; /* Pour aligner le titre avec l'icône */
}

.custom-textarea {
  background: white;
  border-radius: 28px;
  text-align: right; /* Alignement du texte à droite */
}

.bottom-icon {
  color: #0277bd;
  cursor: pointer;
  transition: transform 0.3s ease-in-out;
}

.bottom-icon:hover {
  transform: scale(1.2);
}

.close-icon {
  text-align: right;
  margin-bottom: 10px;
}
</style>