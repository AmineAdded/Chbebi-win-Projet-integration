<template>
    <div>
      <h2>💬 الاقتباسات</h2>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>الاقتباس</th>
            <th>المؤلف</th>
            <th>إجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(quote, index) in quotes" :key="quote.id">
            <td>{{ index + 1 }}</td>
            <td>{{ quote.text }}</td>
            <td>{{ quote.author }}</td>
            <td>
              <button @click="editQuote(quote)">✏️ تعديل</button>
              <button @click="deleteQuote(quote.id)">🗑 حذف</button>
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
        quotes: [
          { id: 1, text: "العقل السليم في الجسم السليم", author: "مثل عالمي" },
          { id: 2, text: "النجاح يأتي بالمثابرة", author: "شخص مجهول" }
        ]
      };
    },
    methods: {
      editQuote(quote) {
        console.log("تعديل:", quote);
      },
      deleteQuote(id) {
        this.quotes = this.quotes.filter(q => q.id !== id);
      }
    }
  };
  </script>
  