<template>
  <v-card class="quote-card" elevation="2">
    <h3 class="quote-title">إقتباسات</h3>
    <p class="quote-text" v-if="quote">"{{ quote.contenu }}"</p>
    <small v-if="quote" class="quote-author">— {{ quote.nomAuteur }} —</small>
    <v-btn @click="loadRandomQuote" class="mt-3" color="primary">إقتباس آخر</v-btn>
  </v-card>
</template>

<script>
import quoteService from '@/Services/QuoteService';

export default {
  name: "Quote",
  data() {
    return {
      quote: null,
    };
  },
  mounted() {
    this.loadRandomQuote();
  },
  methods: {
    loadRandomQuote() {
      quoteService.getRandomQuote()
        .then(response => {
          this.quote = response.data;
        })
        .catch(error => {
          console.error("Erreur lors de la récupération de la citation :", error);
        });
    }
  }
};
</script>
  
  <style scoped>
  .quote-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0;
    padding: 0;

    background-color: 7d9eb5;
  }
  
  .quote-card {
    background-color: #eaf2f8;
    border: 2px solid #7d9eb5;
    text-align: center;
    padding: 50px;
    padding-bottom: 70px;
    width: 100%;

  }
  
  .quote-title {
    color: #1e50a2;
    font-size: 2rem;
    font-weight: bold;
    text-decoration: underline;
  }
  
  .quote-text {
    font-size: 1.6rem;
    color: #000;
    margin-top: 10px;
  }
  .quote-author {
    display: block;
    margin-top: 10px;
    color: #555;
    font-size: 1.0rem;
  }
  </style>