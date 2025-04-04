<template>
  <nav class="sidebar">
    <h2>📌 شبابي وين</h2>
    <br>
    <ul>
      <a href="#"><li>🏠 الرئيسية</li></a>
      
      <li>👥 المستخدمين</li>
      <li>📖 الفصول</li>
      <li>📝 الاستبيانات</li>
      <li>🧪 الاختبارات</li>
      <li>💬 الاقتباسات</li>
      <li>📊 التحليلات</li>
      <li>⚙️ الإعدادات</li>
      <li>🔒 تسجيل الخروج</li>
    </ul>
  </nav>
</template>

<style scoped>
.sidebar {
  width: 250px;
  background-color: #91b5c9;
  color: black;
  padding: 20px;
  min-height: 100vh;
  right: 0;
  position: fixed;
text-align: right;
  box-shadow: 0px 9px 9px rgba(0, 0, 0, 0.1);
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  margin: 15px 0;
}

.sidebar a {
  text-decoration: none;
  color: rgb(62, 62, 62);
}
.sidebar a:hover {
  color: #007bff;
  text-decoration: underline;
}
.sidebar h2 {
  font-size: 1.8em;
  margin-bottom: 20px;
}
</style>
