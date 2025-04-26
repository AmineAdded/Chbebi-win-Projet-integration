<template>
  <div class="table-card">
    <div class="card-header">
      <h2><i class="icon">📖</i> الفصول</h2>
      <div class="card-actions">
        <button class="add-btn"><i class="icon">➕</i> إضافة فصل جديد</button>
        <button class="filter-btn"><i class="icon">🔍</i></button>
      </div>
    </div>

    <div class="table-responsive">
      <table>
        <thead>
          <tr>
            <th class="id-column">#</th>
            <th>العنوان</th>
            <th>الوصف</th>
            <th class="actions-column">إجراءات</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(chapter, index) in chapters" :key="chapter.id" class="table-row">
            <td class="id-column">{{ index + 1 }}</td>
            <td class="title-column">{{ chapter.title }}</td>
            <td>{{ chapter.description }}</td>
            <td class="actions-column">
              <div class="action-buttons">
                <button class="edit-btn" @click="editChapter(chapter)">
                  <i class="icon">✏️</i>
                </button>
                <button class="delete-btn" @click="deleteChapter(chapter.id)">
                  <i class="icon">🗑</i>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="table-footer">
      <div class="pagination">
        <button class="page-btn"><i class="icon">⬅️</i></button>
        <span class="page-info">صفحة 1 من 1</span>
        <button class="page-btn"><i class="icon">➡️</i></button>
      </div>
      <div class="entries-info">إجمالي: {{ chapters.length }} فصل</div>
    </div>
  </div>
</template>
  
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

<style scoped>
.table-card {
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 24px;
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

.table-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

h2 {
  text-align: right;
  font-size: 1.4rem;
  font-weight: 600;
  color: #333;
  margin: 0;
  display: flex;
  align-items: center;
}

.icon {
  margin-left: 8px;
  font-style: normal;
}

.card-actions {
  display: flex;
  gap: 12px;
}

.add-btn {
  background-color: #4361ee;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
}

.add-btn:hover {
  background-color: #3a56d4;
  transform: translateY(-2px);
}

.filter-btn {
  background-color: #f0f4f8;
  border: none;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-btn:hover {
  background-color: #e1e8f0;
}

.table-responsive {
  overflow-x: auto;
  border-radius: 8px;
}

table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

th, td {
  padding: 14px;
  text-align: right;
}

th {
  background-color: #f6f8fa;
  color: #555;
  font-weight: 600;
  border-bottom: 2px solid #eaeef2;
}

tr.table-row {
  border-bottom: 1px solid #eaeef2;
  transition: all 0.2s ease;
}

tr.table-row:hover {
  background-color: #f9fafc;
}

td {
  border-bottom: 1px solid #eaeef2;
}

.id-column {
  width: 60px;
  text-align: center;
}

.title-column {
  font-weight: 500;
}

.actions-column {
  width: 120px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.edit-btn, .delete-btn {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.edit-btn {
  background-color: #e3f2fd;
  color: #2196f3;
}

.edit-btn:hover {
  background-color: #bbdefb;
}

.delete-btn {
  background-color: #ffebee;
  color: #f44336;
}

.delete-btn:hover {
  background-color: #ffcdd2;
}

.table-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 12px;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
}

.page-btn {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  border: 1px solid #e1e8f0;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.page-btn:hover {
  background-color: #f0f4f8;
}

.page-info {
  font-size: 0.9rem;
  color: #555;
}

.entries-info {
  font-size: 0.9rem;
  color: #555;
}
</style>