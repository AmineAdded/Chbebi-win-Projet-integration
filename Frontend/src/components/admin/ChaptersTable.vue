<template>
    <div>
      <h2>📖 الفصول</h2>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>العنوان</th>
            <th>الوصف</th>
            <th>إجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(chapter, index) in chapters" :key="chapter.id">
            <td>{{ index + 1 }}</td>
            <td>{{ chapter.title }}</td>
            <td>{{ chapter.description }}</td>
            <td>
              <button @click="editChapter(chapter)">✏️ تعديل</button>
              <button @click="deleteChapter(chapter.id)">🗑 حذف</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
<style scoped>
  h2 {
    text-align: right;
    margin-bottom: 20px;
  }
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  }
  th, td {
    padding: 10px;
    text-align: right;
    border-bottom: 1px solid #ddd;
  }
  th {
    background-color: #91b5c9;
  }
  button {
    margin-right: 5px;
    padding: 5px 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  button:hover {
    background-color: #0056b3;
  }
</style>
  <script>
  export default {
    data() {
      return {
        chapters: [
          { id: 1, title: "مقدمة", description: "تعريف عام بالتطبيق" },
          { id: 2, title: "التنظيم", description: "كيف تنظم وقتك بشكل فعال" }
        ]
      };
    },
    methods: {
      editChapter(chapter) {
        console.log("تعديل:", chapter);
      },
      deleteChapter(id) {
        this.chapters = this.chapters.filter(chap => chap.id !== id);
      }
    }
  };
  </script>
  