<template>
    <section>
      <h2>📊 الإحصائيات</h2>
      <canvas id="progressChart"></canvas>
    </section>
  </template>
  
<style scoped>
  section {
    text-align: right;
    margin: 20px 0;
  } 
</style>
  <script>
  import { onMounted } from "vue";
  import Chart from "chart.js/auto";
  
  export default {
    setup() {
      onMounted(() => {
        new Chart(document.getElementById("progressChart"), {
          type: "bar",
          data: {
            labels: ["المستخدمين", "الفصول", "الاختبارات", "الاقتباسات"],
            datasets: [{
              label: "نظرة عامة",
              data: [250, 12, 45, 100],
              backgroundColor: ["#ff6384", "#36a2eb", "#ffce56", "#4bc0c0"]
            }]
          }
        });
      });
    },
  };
  </script>
  