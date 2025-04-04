<template>
  <div>
    <h2>📝 الكويزات</h2>
    <table>
      <thead>
        <tr>
          <th>#</th>
          <th>العنوان</th>
          <th>عدد الأسئلة</th>
          <th>إجراءات</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(quiz, index) in quizzes" :key="quiz.id">
          <td>{{ index + 1 }}</td>
          <td>{{ quiz.title }}</td>
          <td>{{ quiz.questionsCount }}</td>
          <td>
            <button @click="editQuiz(quiz)">✏️ تعديل</button>
            <button @click="deleteQuiz(quiz.id)">🗑 حذف</button>
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

th,
td {
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
      quizzes: [
        { id: 1, title: "اختبار الشخصية", questionsCount: 10 },
        { id: 2, title: "اختبار الذكاء", questionsCount: 15 }
      ]
    };
  },
  methods: {
    editQuiz(quiz) {
      console.log("تعديل:", quiz);
    },
    deleteQuiz(id) {
      this.quizzes = this.quizzes.filter(q => q.id !== id);
    }
  }
};
</script>