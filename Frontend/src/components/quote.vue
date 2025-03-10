<template>
    <v-container fluid class="quote-container">
      <v-card class="quote-card" elevation="2">
        <h3 class="quote-title">إقتباسات</h3>
        <p class="quote-text">"اللي يحب يصنع التغيير، يبدأ من نفسه."</p>
      </v-card>
    </v-container>
  </template>
  
  <script>
  export default {
    name: "Quote",
  };
  </script>
  
  <style scoped>
  .quote-container {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
  }
  
  .quote-card {
    background-color: #eaf2f8;
    border: 2px solid #7d9eb5;
    text-align: center;
    padding: 20px;
    width: 100%;
    max-width: 800px;
  }
  
  .quote-title {
    color: #1e50a2;
    font-size: 1.2rem;
    font-weight: bold;
    text-decoration: underline;
  }
  
  .quote-text {
    font-size: 1rem;
    color: #000;
    margin-top: 10px;
  }
  </style>
  