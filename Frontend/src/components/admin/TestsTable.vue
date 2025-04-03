<template>
    <div>
      <h2>🧪 الاختبارات</h2>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>العنوان</th>
            <th>النتيجة</th>
            <th>إجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(test, index) in tests" :key="test.id">
            <td>{{ index + 1 }}</td>
            <td>{{ test.title }}</td>
            <td>{{ test.score }}/100</td>
            <td>
              <button @click="editTest(test)">✏️ تعديل</button>
              <button @click="deleteTest(test.id)">🗑 حذف</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <style scoped>
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
    h2 {
      text-align: right;
      margin-bottom: 20px;
    }
  </style>
  <script>
  export default {
    data() {
      return {
        tests: [
          { id: 1, title: "اختبار التحليل المنطقي", score: 85 },
          { id: 2, title: "اختبار الشخصية", score: 92 }
        ]
      };
    },
    methods: {
      editTest(test) {
        console.log("تعديل:", test);
      },
      deleteTest(id) {
        this.tests = this.tests.filter(t => t.id !== id);
      }
    }
  };
  </script>
  