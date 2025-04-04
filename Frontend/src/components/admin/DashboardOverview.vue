<template>
  <section class="overview">
    <h2>لوحة القيادة</h2><br>
    <div class="cards-container">
      <div class="card">👥 المستخدمين <p>250 نشط</p>
      </div>
      <div class="card">📖 الفصول <p>12 منشورة</p>
      </div>
      <div class="card">📝 الاختبارات <p>45 متاحة</p>
      </div>
      <div class="card">🧪 الاختبارات النهائية <p>30 مكتملة</p>
      </div>
      <div class="card">💬 الاقتباسات <p>100 مشاركة</p>
      </div>
    </div>
  </section><br>
</template>

<style scoped>
h2 {
  text-align: center;
  font-size: 2em;
  color: #333;
  margin-bottom: 20px;
}
.overview {
  background-color: #91b5c9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
.cards-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}
.card {
  background-color: #ffffff;
  border-radius: 10px;
  border: 3px solid black;
  padding: 20px;
  margin: 10px;
  width: calc(33% - 40px);
  text-align: center;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}
.card p {
  margin: 0;
  font-size: 1.2em;
  color: #555;
}
.card:hover {
  transform: scale(1.05);
  transition: transform 0.2s;
}
.card h2 {
  font-size: 1.5em;
  margin-bottom: 10px;
}
.card:hover p {
  color: #007bff;
}
@media (max-width: 700px) {
  .card {
    width: calc(100% - 40px);
  }
}
</style>