<template>
    <section>
      <h2>👥 إدارة المستخدمين</h2>
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>الاسم</th>
            <th>البريد الإلكتروني</th>
            <th>الإجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.email }}</td>
            <td>
              <button @click="editUser(quote)">✏️ تعديل</button>
              <button @click="deleteUser(user.id)">🗑 حذف</button></td>
          </tr>
        </tbody>
      </table>
    </section>
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
        users: [
          { id: 1, name: "أحمد", email: "ahmed@example.com" },
          { id: 2, name: "سارة", email: "sarah@example.com" },
        ],
      };
    },
    methods: {
      deleteUser(id) {
        this.users = this.users.filter(user => user.id !== id);
      },
    },
  };
  </script>
 
  