<template>
      <v-card class="quote-card" elevation="2">
        <h3 class="quote-title">إقتباسات</h3>
        <p class="quote-text">"اللي يحب يصنع التغيير، يبدأ من نفسه."</p>
      </v-card>
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
  </style>
  