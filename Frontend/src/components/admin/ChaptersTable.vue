<template>
  <div class="table-card">
    <div class="card-header">
      <h2><i class="icon">📖</i> الفصول</h2>
      <div class="card-actions">
        <button class="add-btn" @click="addNewChapter(null)">
          <i class="icon">➕</i> إضافة فصل جديد
        </button>
        <button class="filter-btn"><i class="icon">🔍</i></button>
      </div>
    </div>

    <div class="table-responsive">
      <table>
        <thead>
          <tr>
            <th class="id-column">#</th>
            <th>العنوان</th>
            <th class="actions-column">إجراءات</th>
          </tr>
        </thead>
        <tbody>
          <!-- Render flattened hierarchy -->
          <tr v-for="(item) in flattenedChapters" :key="item.id" 
            class="table-row" 
            :class="{ 'child-row': item.level === 1, 'grandchild-row': item.level === 2, 'third-level-row': item.level >= 3 }">
            <td class="id-column">{{ item.numbering }}</td>
            <td class="title-column">
              <span class="title-content" :style="{ paddingRight: (item.level * 20) + 'px' }">
                {{ item.title }}
              </span>
            </td>
            <td class="actions-column">
              <div class="action-buttons">
                <button v-if="hasChildren(item)" class="view-btn" @click="toggleExpand(item.id)">
                  <i class="icon">{{ isExpanded(item.id) ? '👁️' : '👁️' }}</i>
                </button>
                <button class="add-btn" @click="addNewChapter(item.id)">
                  <i class="icon">➕</i>
                </button>
                <button class="edit-btn" @click="editChapter(item)">
                  <i class="icon">✏️</i>
                </button>
                <button class="delete-btn" @click="deleteChapter(item.id)">
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
      <div class="entries-info">إجمالي: {{ totalChapters }} فصل</div>
    </div>

    <!-- Modal for adding/editing chapter -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEditing ? 'تعديل الفصل' : 'إضافة فصل جديد' }}</h3>
          <button class="close-btn" @click="closeModal">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="chapterTitle">عنوان الفصل</label>
            <input type="text" id="chapterTitle" v-model="currentChapter.title" class="form-control">
          </div>
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="closeModal">إلغاء</button>
          <button class="save-btn" @click="saveChapter">حفظ</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      chapters: [

        {
          id: 1,
          title: "مقدمة",
          children: [
            {
              id: 101,
              title: "تعريف عام بالتطبيق",
              children: [
                { 
                  id: 1001, 
                  title: "الهدف من التطبيق",
                  children: [
                    { id: 10001, title: "تسهيل الوصول للمعلومات", children: [] },
                    { id: 10002, title: "تحسين تجربة المستخدم", children: [] }
                  ] 
                }
              ]
            }
          ]
        }
      ],
      expandedItems: new Set(),
      showModal: false,
      isEditing: false,
      currentChapter: { id: null, title: '', children: [] },
      parentId: null
    };
  },
  computed: {
    flattenedChapters() {
      // Create a flattened representation of the chapter hierarchy
      const result = [];
     
      const processChapter = (chapter, parentIndex = '', level = 0) => {
        // const index = parentIndex ? `${parentIndex}.${result.length - result.lastIndexOf(item => item.level === level) + 1}` : `${result.filter(item => item.level === 0).length + 1}`;
        
        // Don't include children if parent is not expanded, unless it's top level
        if (level === 0 || this.isParentExpanded(chapter.id)) {
          const numbering = parentIndex ? `${parentIndex}` : `${result.filter(item => item.level === 0).length + 1}`;
          
          result.push({
            id: chapter.id,
            title: chapter.title,
            numbering: numbering,
            level: level,
            hasChildren: chapter.children && chapter.children.length > 0
          });
          
          // Process children if parent is expanded
          if (this.isExpanded(chapter.id) && chapter.children && chapter.children.length > 0) {
            chapter.children.forEach((child, childIndex) => {
              const childNumbering = `${numbering}.${childIndex + 1}`;
              processChapter(child, childNumbering, level + 1);
            });
          }
        }
      };
      
      // Start with top-level chapters
      this.chapters.forEach(chapter => {
        processChapter(chapter);
      });
      
      return result;
    },
    totalChapters() {
      // Function to count all chapters including nested ones
      const countChapters = (items) => {
        if (!items || !items.length) return 0;
        
        let count = items.length;
        for (const item of items) {
          if (item.children && item.children.length) {
            count += countChapters(item.children);
          }
        }
        return count;
      };
      
      return countChapters(this.chapters);
    }
  },
  methods: {
    isParentExpanded(childId) {
      // Check if all parent chapters in the hierarchy are expanded
      const findParent = (chapters, childId, parentIds = []) => {
        for (const chapter of chapters) {
          if (chapter.children && chapter.children.length) {
            const childIndex = chapter.children.findIndex(c => c.id === childId);
            if (childIndex !== -1) {
              return [...parentIds, chapter.id];
            }
            
            const result = findParent(chapter.children, childId, [...parentIds, chapter.id]);
            if (result.length) return result;
          }
        }
        return [];
      };
      
      const parentIds = findParent(this.chapters, childId);
      // Check if all parents are expanded
      return parentIds.every(id => this.expandedItems.has(id));
    },
    hasChildren(chapter) {
      if (typeof chapter === 'object') {
        return chapter.hasChildren;
      }
      return false;
    },
    isExpanded(id) {
      return this.expandedItems.has(id);
    },
    toggleExpand(id) {
      if (this.expandedItems.has(id)) {
        this.expandedItems.delete(id);
      } else {
        this.expandedItems.add(id);
      }
    },
    findChapter(chapterList, id) {
      if (!chapterList || !chapterList.length) return null;
      
      for (const chapter of chapterList) {
        if (chapter.id === id) return chapter;
        if (chapter.children && chapter.children.length) {
          const found = this.findChapter(chapter.children, id);
          if (found) return found;
        }
      }
      return null;
    },
    addNewChapter(parentId) {
      this.isEditing = false;
      this.currentChapter = { id: null, title: '', children: [] };
      this.parentId = parentId;
      this.showModal = true;
    },
    editChapter(chapter) {
      // Find the actual chapter object in the data
      const actualChapter = this.findChapter(this.chapters, chapter.id);
      if (actualChapter) {
        this.isEditing = true;
        this.currentChapter = { ...actualChapter };
        this.showModal = true;
      }
    },
    saveChapter() {
      if (!this.currentChapter.title.trim()) {
        alert('الرجاء إدخال عنوان الفصل');
        return;
      }

      if (this.isEditing) {
        // Update existing chapter
        const chapter = this.findChapter(this.chapters, this.currentChapter.id);
        if (chapter) {
          chapter.title = this.currentChapter.title;
        }
      } else {
        // Add new chapter
        const newChapter = {
          id: Date.now(),
          title: this.currentChapter.title,
          children: []
        };
        
        if (this.parentId === null) {
          this.chapters.push(newChapter);
        } else {
          const parent = this.findChapter(this.chapters, this.parentId);
          if (parent) {
            if (!parent.children) {
              parent.children = [];
            }
            parent.children.push(newChapter);
            this.expandedItems.add(this.parentId);
          }
        }
      }
      
      this.closeModal();
    },
    closeModal() {
      this.showModal = false;
      this.currentChapter = { id: null, title: '', children: [] };
      this.parentId = null;
    },
    deleteChapter(chapterId) {
      if (confirm('هل أنت متأكد من حذف هذا الفصل؟')) {
        this.deleteChapterFromList(this.chapters, chapterId);
      }
    },
    deleteChapterFromList(chapterList, id) {
      if (!chapterList || !chapterList.length) return false;
      
      const index = chapterList.findIndex(c => c.id === id);
      if (index !== -1) {
        chapterList.splice(index, 1);
        return true;
      }
      
      for (const chapter of chapterList) {
        if (chapter.children && chapter.children.length) {
          if (this.deleteChapterFromList(chapter.children, id)) {
            return true;
          }
        }
      }
      
      return false;

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

.action-buttons .add-btn {
  width: 32px;
  height: 32px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
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

tr.child-row {
  background-color: #f9fafc;
}

tr.grandchild-row {
  background-color: #f2f5f8;
}

tr.third-level-row {
  background-color: #edf1f5;
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

.title-content {
  display: inline-block;
  position: relative;
}

.actions-column {
  width: 180px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.view-btn, .edit-btn, .delete-btn {
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

.view-btn {
  background-color: #e8f5e9;
  color: #4caf50;
}

.view-btn:hover {
  background-color: #c8e6c9;
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


/* Modal styling */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal-header {
  padding: 16px 20px;
  border-bottom: 1px solid #eaeef2;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #777;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
  text-align: right;
}

.form-control {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e1e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.2s;
  direction: rtl;
}

.form-control:focus {
  border-color: #4361ee;
  outline: none;
  box-shadow: 0 0 0 2px rgba(67, 97, 238, 0.1);
}

.modal-footer {
  padding: 16px 20px;
  border-top: 1px solid #eaeef2;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-btn {
  background-color: #f0f4f8;
  color: #555;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cancel-btn:hover {
  background-color: #e1e8f0;
}

.save-btn {
  background-color: #4361ee;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.save-btn:hover {
  background-color: #3a56d4;
}

/* Highlight expanded rows */
tr.expanded {
  background-color: #f8f9fa;
}

</style>