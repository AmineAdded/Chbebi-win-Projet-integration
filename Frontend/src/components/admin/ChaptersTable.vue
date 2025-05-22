<template>
  <div class="table-card">
    <div class="card-header">
      <h2><i class="icon">📖</i> الفصول</h2>
      <div class="card-actions">
        <button class="add-btn thematic-btn" @click="showAddThematicModal">
          <i class="icon">➕</i> إضافة مجال جديد
        </button>
        <button class="add-btn" @click="addNewChapter(null)">
          <i class="icon">➕</i> إضافة فصل جديد
        </button>
        <button class="filter-btn" @click="toggleSearch">
          <i class="icon">🔍</i>
        </button>
        <div class="thematic-selector">
          <select v-if="thematics.length > 0" v-model="selectedThematicId" class="thematic-select"
            @change="loadChaptersByThematic">
            <option value="" disabled>-- اختر مجال --</option>
            <option v-for="thematic in thematics" :key="thematic.id" :value="thematic.id">
              {{ thematic.nom }}
            </option>
          </select>
          <div v-if="selectedThematicId" class="thematic-actions">
            <button class="edit-thematic-btn" @click="editThematic(selectedThematicId)">
              <i class="icon">✏️</i>
            </button>
            <button class="delete-thematic-btn" @click="deleteThematic(selectedThematicId)">
              <i class="icon">🗑</i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Search bar -->
    <div v-if="showSearch" class="search-container">
      <div class="search-input-wrapper">
        <input type="text" v-model="searchTerm" class="search-input" placeholder="ابحث عن فصل..." @input="performSearch"
          dir="rtl" />
        <button v-if="searchTerm" class="clear-search" @click="clearSearch">
          ✕
        </button>
      </div>
      <div class="search-info" v-if="searchTerm">
        {{ searchResults.length }} نتيجة بحث
      </div>
    </div>

    <!-- Loading indicator -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>جاري تحميل البيانات...</p>
    </div>

    <div v-else class="table-responsive">
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
          <tr v-for="item in displayedChapters" :key="item.id" class="table-row" :class="{
            'child-row': item.level === 1,
            'grandchild-row': item.level === 2,
            'third-level-row': item.level >= 3,
            'search-highlight': isSearchMatch(item),
          }">
            <td class="id-column">{{ item.numbering }}</td>
            <td class="title-column">
              <span class="title-content" :style="{ paddingRight: item.level * 20 + 'px' }">
                {{ item.title }}
                <span v-if="item.pourcentage > 0" class="progress-indicator">
                </span>
              </span>
            </td>
            <td class="actions-column">
              <div class="action-buttons">
                <button v-if="hasChildren(item)" class="view-btn" @click="toggleExpand(item.id)">
                  <i class="icon">{{ isExpanded(item.id) ? "👁️" : "👁️" }}</i>
                </button>
                <button class="add-btn" v-if="hasChildren(item) || getChapterType(item) === 'main'"
                  @click="addNewChapter(item.id)">
                  <i class="icon">➕</i>
                </button>
                <button class="edit-btn" @click="editChapter(item)">
                  <i class="icon">✏️</i>
                </button>
                <button class="delete-btn" @click="deleteChapter(item)">
                  <i class="icon">🗑</i>
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="displayedChapters.length === 0">
            <td colspan="3" class="no-results">لا توجد نتائج</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="table-footer">
      <div class="pagination">
        <button class="page-btn" :disabled="currentPage <= 1" @click="changePage(-1)">
          <i class="icon">⬅️</i>
        </button>
        <span class="page-info">صفحة {{ currentPage }} من {{ totalPages }}</span>
        <button class="page-btn" :disabled="currentPage >= totalPages" @click="changePage(1)">
          <i class="icon">➡️</i>
        </button>
      </div>
      <div class="entries-info">إجمالي: {{ totalChapters }} فصل</div>
    </div>

    <!-- Modal for adding/editing thematic -->
    <div class="modal" v-if="showThematicModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEditingThematic ? "تعديل المجال" : "إضافة مجال جديد" }}</h3>
          <button class="close-btn" @click="closeThematicModal">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="thematicName">اسم المجال</label>
            <input type="text" id="thematicName" v-model="newThematic.nom" class="form-control" />
          </div>
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="closeThematicModal">إلغاء</button>
          <button class="save-btn" @click="saveThematic">حفظ</button>
        </div>
      </div>
    </div>

    <!-- Modal for adding/editing chapter -->
    <div class="modal" v-if="showModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ isEditing ? "تعديل الفصل" : "إضافة فصل جديد" }}</h3>
          <button class="close-btn" @click="closeModal">✕</button>
        </div>
        <div class="modal-body">
          <!-- Standard fields for both chapters and sub-chapters -->
          <div class="form-group">
            <label for="chapterTitle">عنوان الفصل</label>
            <input type="text" id="chapterTitle" v-model="currentChapter.title" class="form-control" />
          </div>
          <div class="form-group">
            <label for="chapterDescription">وصف الفصل</label>
            <textarea id="chapterDescription" v-model="currentChapter.description" class="form-control"></textarea>
          </div>
          <div class="form-group">
            <label for="chapterImage">الصورة</label>
            <input type="file" id="chapterImage" @change="handleFileUpload" class="form-control"
              placeholder="رابط الصورة" />
          </div>

          <!-- Additional fields for sous-chapters only -->
          <div v-if="parentId" class="form-group">
            <label for="videoLink">رابط الفيديو</label>
            <input type="text" id="videoLink" v-model="currentChapter.lien_video" class="form-control" />
          </div>
          <div v-if="parentId" class="form-group">
            <label for="pdfLink">ملف PDF</label>
            <input type="file" id="pdfLink" @change="handlePdfUpload" class="form-control" />
          </div>

          <!-- Thematic selection for main chapters (when adding new) -->
          <div v-if="!parentId && !isEditing" class="form-group">
            <label for="thematicSelect">المجال</label>
            <select id="thematicSelect" v-model="currentChapter.thematicId" class="form-control">
              <option value="" disabled>-- اختر مجال --</option>
              <option v-for="thematic in thematics" :key="thematic.id" :value="thematic.id">
                {{ thematic.nom }}
              </option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="cancel-btn" @click="closeModal">إلغاء</button>
          <button class="save-btn" @click="saveChapter">حفظ</button>
        </div>
      </div>
    </div>

    <!-- Error Alert -->
    <div class="error-alert" v-if="error">
      <div class="error-content">
        <span class="error-icon">⚠️</span>
        <span class="error-message">{{ error }}</span>
        <button class="error-close" @click="error = null">×</button>
      </div>
    </div>
  </div>
  <!-- Modal de confirmation de suppression -->
  <div class="modal" v-if="showDeleteConfirmation">
    <div class="modal-content">
      <div class="modal-header">
        <h3>تأكيد الحذف</h3>
        <button class="close-btn" @click="showDeleteConfirmation = false">
          ✕
        </button>
      </div>
      <div class="modal-body">
        <p>{{ deleteConfirmationMessage }}</p>
      </div>
      <div class="modal-footer">
        <button class="cancel-btn" @click="showDeleteConfirmation = false">
          إلغاء
        </button>
        <button class="confirm-delete-btn" @click="confirmDeleteAction">
          تأكيد الحذف
        </button>
      </div>
    </div>
  </div>
</template>


<script>
import ChapterService from "@/Services/chapitreService";
import SousChapterService from "@/Services/sousChapitreService";
import ThematicService from "@/Services/thematicService";
import axios from "axios";

export default {
  data() {
    return {
      chapters: [],
      thematics: [],
      expandedItems: new Set(),
      showModal: false,
      showThematicModal: false,
      isEditing: false,
      isEditingThematic: false,
      currentChapter: {
        id: null,
        title: "",
        description: "",
        image: "",
        thematicId: "",
        lien_video: "",
        pdf: "",
        pourcentage: 0,
      },
      newThematic: {
        id: null,
        nom: "",
        description: "",
      },
      parentId: null,
      showSearch: false,
      searchTerm: "",
      searchResults: [],
      itemsPerPage: 5,
      currentPage: 1,
      loading: false,
      error: null,
      selectedThematicId: "",
      showDeleteConfirmation: false,
      deleteConfirmationMessage: "",
      itemToDelete: null,
      deleteType: "", // 'chapter' ou 'thematic'
    };
  },
  computed: {
    flattenedChapters() {
      const result = [];

      const processChapter = (chapter, parentIndex = "", level = 0, parentId = null) => {
        const numbering = parentIndex
          ? `${parentIndex}`
          : `${result.filter((item) => item.level === 0).length + 1}`;

        // Always add the current chapter to results
        result.push({
          id: chapter.id,
          title: chapter.title,
          description: chapter.description,
          image: chapter.image,
          numbering: numbering,
          level: level,
          parentId: parentId, // Track the parent ID
          pourcentage: chapter.pourcentage || 0,
          hasChildren: chapter.sousChapitres && chapter.sousChapitres.length > 0,
        });

        // Process children only if this chapter is expanded
        if (
          this.isExpanded(chapter.id) &&
          chapter.sousChapitres &&
          chapter.sousChapitres.length > 0
        ) {
          chapter.sousChapitres.forEach((child, childIndex) => {
            const childNumbering = `${numbering}.${childIndex + 1}`;
            processChapter(child, childNumbering, level + 1, chapter.id);
          });
        }
      };

      // Start with top-level chapters
      this.chapters.forEach((chapter) => {
        processChapter(chapter);
      });

      return result;
    },
    displayedChapters() {
      // If searching, return search results
      if (this.searchTerm) {
        return this.searchResults;
      }

      // Otherwise return paginated flattened chapters
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.flattenedChapters.slice(start, end);
    },
    totalChapters() {
      return this.flattenedChapters.length;
    },
    totalPages() {
      if (this.searchTerm) {
        return Math.max(
          1,
          Math.ceil(this.searchResults.length / this.itemsPerPage)
        );
      }
      return Math.max(
        1,
        Math.ceil(this.flattenedChapters.length / this.itemsPerPage)
      );
    },
  },
  async created() {
    try {
      this.loading = true;
      // First load thematics
      await this.loadThematics();

      // If thematics were loaded successfully, automatically load the first one
      if (this.thematics.length > 0) {
        this.selectedThematicId = this.thematics[0].id;
        await this.loadChaptersByThematic();
      }
    } catch (error) {
      this.error = "حدث خطأ أثناء تحميل البيانات";
      console.error("Error loading data:", error);
    } finally {
      this.loading = false;
    }
  },
  methods: {
    async handleFileUpload(event) {
      const file = event.target.files[0];
      if (!file) return;
      const formData = new FormData();
      formData.append("file", file);

      try {
        const res = await axios.post("http://localhost:9090/api/upload", formData);
        this.currentChapter.image = res.data; // exemple: "/files/images/uuid_nom.jpg"
      } catch (err) {
        console.error("Erreur upload image", err);
      }
    },

    async handlePdfUpload(event) {
      const file = event.target.files[0];
      if (!file) return;

      const formData = new FormData();
      formData.append("file", file);

      try {
        const response = await axios.post("http://localhost:9090/api/upload", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        // Exemple : /files/xyz.pdf
        this.currentChapter.pdf = response.data;
      } catch (error) {
        console.error("Erreur upload PDF", error);
        this.error = "Échec du téléchargement du fichier PDF.";
      }
    },

    getChapterType(item) {
      return item.level === 0 ? "main" : "sub";
    },
    async loadThematics() {
      try {
        this.thematics = await ThematicService.getAllThematics();
      } catch (error) {
        console.error("Error loading thematics:", error);
        this.error = "فشل في تحميل المجالات";
      }
    },

    showAddThematicModal() {
      this.isEditingThematic = false;
      this.newThematic = { id: null, nom: "", description: "" };
      this.showThematicModal = true;
    },

    editThematic(thematicId) {
      const thematic = this.thematics.find((t) => t.id === thematicId);
      if (!thematic) {
        this.error = "لم يتم العثور على المجال";
        return;
      }

      this.isEditingThematic = true;
      this.newThematic = {
        id: thematic.id,
        nom: thematic.nom,
        description: thematic.description,
      };
      this.showThematicModal = true;
    },

    closeThematicModal() {
      this.showThematicModal = false;
      this.newThematic = { id: null, nom: "", description: "" };
      this.isEditingThematic = false;
    },

    async saveThematic() {
      if (!this.newThematic.nom.trim()) {
        this.error = "الرجاء إدخال اسم المجال";
        return;
      }

      try {
        this.loading = true;

        if (this.isEditingThematic) {
          // Update existing thematic
          await ThematicService.updateThematic(
            this.newThematic.id,
            this.newThematic
          );
        } else {
          // Create new thematic
          await ThematicService.createThematic(this.newThematic);
        }

        // Reload thematics
        await this.loadThematics();

        // If we edited the currently selected thematic, reload chapters to reflect any name changes
        if (
          this.isEditingThematic &&
          this.selectedThematicId === this.newThematic.id
        ) {
          await this.loadChaptersByThematic();
        }

        // Close modal and reset form
        this.closeThematicModal();
      } catch (error) {
        console.error(
          this.isEditingThematic
            ? "Error updating thematic:"
            : "Error creating thematic:",
          error
        );
        this.error = this.isEditingThematic
          ? "فشل في تحديث المجال"
          : "فشل في إنشاء المجال";
      } finally {
        this.loading = false;
      }
    },

    async loadChaptersByThematic() {
      try {
        this.loading = true;
        this.error = null;

        console.log(
          `Attempting to load chapters for thematic ID: ${this.selectedThematicId}`
        );

        if (!this.selectedThematicId) {
          console.log("No thematic selected, skipping chapter load");
          this.chapters = [];
          return;
        }

        // Add explicit log before API call
        console.log(
          `Making API request to: /chapitres/get/${this.selectedThematicId}`
        );

        const chapters = await ChapterService.getChaptersByThematic(
          this.selectedThematicId
        );

        console.log("API response received:", chapters);

        // Transform data to match our component structure
        this.chapters = chapters.map((chapter) => ({
          id: chapter.id,
          title: chapter.title,
          description: chapter.description,
          image: chapter.image,
          pourcentage: chapter.pourcentage,
          sousChapitres: chapter.sousChapitres || [],
        }));

        console.log("Transformed chapters:", this.chapters);

        // Reset pagination
        this.currentPage = 1;
        // Clear search
        this.clearSearch();
      } catch (error) {
        console.error("Error fetching chapters:", error);
        // More detailed error message
        if (error.response) {
          // The request was made and the server responded with a status code
          // that falls out of the range of 2xx
          console.error("Response data:", error.response.data);
          console.error("Response status:", error.response.status);
          this.error = `فشل في تحميل الفصول - خطأ ${error.response.status}`;
        } else if (error.request) {
          // The request was made but no response was received
          console.error("No response received:", error.request);
          this.error = "فشل في تحميل الفصول - لم يتم استلام رد من الخادم";
        } else {
          // Something happened in setting up the request that triggered an Error
          console.error("Error setting up request:", error.message);
          this.error = `فشل في تحميل الفصول - ${error.message}`;
        }
      } finally {
        this.loading = false;
      }
    },

    // isParentExpanded(childId) {
    //   // For sub-chapters, check if the parent chapter is expanded
    //   const findParentChapterId = (childId) => {
    //     for (const chapter of this.chapters) {
    //       if (chapter.sousChapitres) {
    //         for (const sousChapter of chapter.sousChapitres) {
    //           if (sousChapter.id === childId) {
    //             return chapter.id;
    //           }

    //           // Check deeper levels if necessary - adjust as needed for your structure
    //           if (sousChapter.sousChapitres) {
    //             for (const subSousChapter of sousChapter.sousChapitres) {
    //               if (subSousChapter.id === childId) {
    //                 return sousChapter.id;
    //               }
    //             }
    //           }
    //         }
    //       }
    //     }
    //     return null;
    //   };

    //   const parentId = findParentChapterId(childId);
    //   if (!parentId) return true;

    //   // Also check if parent's parent is expanded
    //   const grandParentId = findParentChapterId(parentId);

    //   return (
    //     this.expandedItems.has(parentId) &&
    //     (!grandParentId || this.expandedItems.has(grandParentId))
    //   );
    // },

    hasChildren(chapter) {
      return chapter.hasChildren;
    },

    isExpanded(id) {
      return this.expandedItems.has(id);
    },

    toggleExpand(id) {
      console.log(`Toggling expansion for chapter ID: ${id}`);

      if (this.expandedItems.has(id)) {
        this.expandedItems.delete(id);
        console.log(`Collapsed chapter ${id}`);
      } else {
        this.expandedItems.add(id);
        console.log(`Expanded chapter ${id}`);
      }

      // Force reactivity update
      this.$forceUpdate();
    },

    findChapter(id) {
      return this.chapters.find((c) => c.id === id);
    },
    findSousChapter(id) {
      for (const chapter of this.chapters) {
        if (chapter.sousChapitres) {
          const found = chapter.sousChapitres.find((sc) => sc.id === id);
          if (found) return { sousChapter: found, parentId: chapter.id };

          for (const sousChapter of chapter.sousChapitres) {
            if (sousChapter.sousChapitres) {
              const subFound = sousChapter.sousChapitres.find(
                (ssc) => ssc.id === id
              );
              if (subFound)
                return { sousChapter: subFound, parentId: sousChapter.id };
            }
          }
        }
      }
      return null;
    },

    async addNewChapter(parentId) {
      this.isEditing = false;
      this.parentId = parentId;
      this.showValidationErrors = false;

      if (parentId) {
        const parentChapter = this.findChapter(parentId);
        if (!parentChapter) {
          const result = this.findSousChapter(parentId);
          if (!result) {
            this.error = "الفصل الأصلي غير موجود";
            return;
          }
        }

        this.currentChapter = {
          title: "",
          description: "",
          image: "",
          chapitreId: parentId,
          lien_video: "",
          pdf: "",
          pourcentage: 0,
        };
      } else {
        this.currentChapter = {
          title: "",
          description: "",
          image: "",
          thematicId: this.selectedThematicId || "",
          pourcentage: 0,
        };
      }

      this.showModal = true;
    },

    async editChapter(chapter) {
      this.isEditing = true;
      this.showValidationErrors = false;

      if (chapter.level === 0) {
        const fullChapter = this.findChapter(chapter.id);
        if (fullChapter) {
          const thematicId = this.selectedThematicId;

          this.currentChapter = {
            id: chapter.id,
            title: fullChapter.title,
            description: fullChapter.description,
            image: fullChapter.image,
            thematicId: thematicId,
            pourcentage: fullChapter.pourcentage,
          };

          this.parentId = null;
        } else {
          this.error = "لم يتم العثور على الفصل";
          return;
        }
      } else {
        // Editing a sous-chapter
        const result = this.findSousChapter(chapter.id);
        if (result) {
          this.currentChapter = {
            id: chapter.id,
            title: result.sousChapter.title,
            description: result.sousChapter.description,
            image: result.sousChapter.image,
            lien_video: result.sousChapter.lienVideo || "",
            pdf: result.sousChapter.pdf || "",
            chapitreId: result.parentId,
            pourcentage: result.sousChapter.pourcentage,
          };

          this.parentId = result.parentId;
        } else {
          this.error = "لم يتم العثور على الفصل الفرعي";
          return;
        }
      }

      this.showModal = true;
    },

    async saveChapter() {
      if (!this.currentChapter.title.trim()) {
        this.error = "الرجاء إدخال عنوان الفصل";
        return;
      }

      try {
        this.loading = true;

        if (this.isEditing) {
          if (this.parentId) {
            const sousChapterData = {
              id: this.currentChapter.id,
              title: this.currentChapter.title,
              description: this.currentChapter.description,
              image: this.currentChapter.image,
              lienVideo: this.currentChapter.lien_video,
              pdf: this.currentChapter.pdf,
              chapitreId: this.parentId,
            };
            console.log("Updating sous-chapter with data:", sousChapterData);
            await SousChapterService.updateSousChapter(
              this.currentChapter.id,
              sousChapterData
            );
          } else {
            const chapterData = {
              id: this.currentChapter.id,
              title: this.currentChapter.title,
              description: this.currentChapter.description,
              image: this.currentChapter.image,
              thematicId: this.selectedThematicId,
            };

            await ChapterService.updateChapter(
              this.currentChapter.id,
              chapterData
            );
          }
        } else {
          // Create new chapter or sous-chapter
          if (this.parentId) {
            // Create a sous-chapter
            const sousChapterData = {
              title: this.currentChapter.title,
              description: this.currentChapter.description,
              image: this.currentChapter.image,
              lienVideo: this.currentChapter.lien_video,
              pdf: this.currentChapter.pdf,
              chapitreId: this.parentId,
            };

            await SousChapterService.createSousChapter(sousChapterData);
          } else {
            // Create a main chapter
            if (!this.currentChapter.thematicId) {
              this.error = "الرجاء اختيار مجال";
              this.loading = false;
              return;
            }

            const chapterData = {
              title: this.currentChapter.title,
              description: this.currentChapter.description,
              image: this.currentChapter.image,
              thematicId: this.currentChapter.thematicId,
            };
            console.log("Creating chapter with data:", chapterData);
            await ChapterService.createChapter(chapterData);
          }
        }

        // Reload data
        await this.loadChaptersByThematic();
        this.closeModal();
      } catch (error) {
        console.error("Error saving chapter:", error);
        this.error = "فشل في حفظ الفصل";
      } finally {
        this.loading = false;
      }
    },

    closeModal() {
      this.showModal = false;
      this.currentChapter = {
        id: null,
        title: "",
        description: "",
        image: "",
        thematicId: "",
        lien_video: "",
        pdf: "",
        pourcentage: 0,
      };
      this.parentId = null;
      this.isEditing = false;
    },

    toggleSearch() {
      this.showSearch = !this.showSearch;
      if (!this.showSearch) {
        this.clearSearch();
      } else {
        this.$nextTick(() => {
          // Focus on search input when it appears
          const searchInput = document.querySelector(".search-input");
          if (searchInput) {
            searchInput.focus();
          }
        });
      }
    },

    performSearch() {
      if (!this.searchTerm.trim()) {
        this.searchResults = [];
        this.currentPage = 1;
        return;
      }

      const searchTermLower = this.searchTerm.toLowerCase();

      // Generate complete flattened hierarchy for searching (including non-expanded items)
      const allItems = [];

      const flattenForSearch = (chapters, parentIndex = "", level = 0, parentId = null) => {
        if (!chapters || !chapters.length) return;

        chapters.forEach((chapter, index) => {
          const numbering = parentIndex
            ? `${parentIndex}.${index + 1}`
            : `${index + 1}`;

          allItems.push({
            id: chapter.id,
            title: chapter.title,
            description: chapter.description || "",
            numbering: numbering,
            level: level,
            parentId: parentId,
            pourcentage: chapter.pourcentage || 0,
            hasChildren: chapter.sousChapitres && chapter.sousChapitres.length > 0,
          });

          // Always include children in search, regardless of expansion state
          if (chapter.sousChapitres && chapter.sousChapitres.length > 0) {
            flattenForSearch(chapter.sousChapitres, numbering, level + 1, chapter.id);
          }
        });
      };

      flattenForSearch(this.chapters);

      // Filter based on title or description
      this.searchResults = allItems.filter(
        (item) =>
          item.title.toLowerCase().includes(searchTermLower) ||
          item.description.toLowerCase().includes(searchTermLower)
      );

      // Reset pagination when search changes
      this.currentPage = 1;

      // Expand all parents of search results to ensure visibility
      this.searchResults.forEach((item) => {
        this.expandParentsOfItem(item);
      });
    },
    expandParentsOfItem(item) {
      if (!item.parentId) return;

      // Expand the direct parent
      this.expandedItems.add(item.parentId);

      // Find the parent item and recursively expand its parents
      const parentItem = this.findItemById(item.parentId);
      if (parentItem) {
        this.expandParentsOfItem(parentItem);
      }
    },
    findItemById(id) {
      const findInChapters = (chapters, parentId = null) => {
        for (const chapter of chapters) {
          if (chapter.id === id) {
            return { ...chapter, parentId };
          }

          if (chapter.sousChapitres && chapter.sousChapitres.length > 0) {
            const found = findInChapters(chapter.sousChapitres, chapter.id);
            if (found) return found;
          }
        }
        return null;
      };

      return findInChapters(this.chapters);
    },

    findParentByChildId(childId) {
      const findParent = (chapters) => {
        for (const chapter of chapters) {
          if (chapter.sousChapitres && chapter.sousChapitres.length > 0) {
            // Check if childId is a direct child
            if (chapter.sousChapitres.some(child => child.id === childId)) {
              return chapter;
            }

            // Recursively check in sub-chapters
            const found = findParent(chapter.sousChapitres, chapter);
            if (found) return found;
          }
        }
        return null;
      };

      return findParent(this.chapters);
    },

    clearSearch() {
      this.searchTerm = "";
      this.searchResults = [];
      this.currentPage = 1;
    },

    isSearchMatch(item) {
      if (!this.searchTerm || !this.searchResults.length) return false;
      return this.searchResults.some((result) => result.id === item.id);
    },

    changePage(direction) {
      const newPage = this.currentPage + direction;
      if (newPage >= 1 && newPage <= this.totalPages) {
        this.currentPage = newPage;
      }
    },

    deleteChapter(to_delete) {
      this.deleteType = "chapter";
       const chapterToDelete = this.flattenedChapters.find(item => item.id === to_delete.id);

      if (!chapterToDelete) {
        this.error = "لم يتم العثور على الفصل";
        return;
      }
      // Determine if this is a main chapter or sous-chapter based on its level
      const isMainChapter = to_delete.level === 0;

      this.itemToDelete = {
        id: to_delete.id,
        isMainChapter: isMainChapter,
        title: to_delete.title || "هذا الفصل"
      };
      console.log(this.itemToDelete);

      // Trouver le chapitre pour afficher son titre dans le message

      this.deleteConfirmationMessage = `هل أنت متأكد من حذف ${this.itemToDelete.title}؟ ${isMainChapter ? "سيتم حذف جميع الفصول الفرعية المرتبطة به أيضًا." : ""}`;
      this.showDeleteConfirmation = true;
    },

    // Modifier la méthode deleteThematic
    deleteThematic(thematicId) {
      this.itemToDelete = thematicId;
      this.deleteType = "thematic";

      const thematic = this.thematics.find((t) => t.id === thematicId);
      const thematicName = thematic?.nom || "هذا المجال";

      this.deleteConfirmationMessage = `هل أنت متأكد من حذف ${thematicName}؟ سيتم حذف جميع الفصول المرتبطة به أيضًا.`;
      this.showDeleteConfirmation = true;
    },

    // Nouvelle méthode pour confirmer la suppression
    async confirmDeleteAction() {
      try {
        this.loading = true;
        this.showDeleteConfirmation = false;

        if (this.deleteType === "chapter") {
          const { id, isMainChapter } = this.itemToDelete;

         if (isMainChapter) {
            // Delete main chapter using ChapterService
            await ChapterService.deleteChapter(id);
          } else {
            await SousChapterService.deleteSousChapter(id);
          }

          await this.loadChaptersByThematic();
        } else if (this.deleteType === "thematic") {
          const thematicId = this.itemToDelete;
          await ThematicService.deleteThematic(thematicId);

          await this.loadThematics();

          if (this.selectedThematicId === thematicId) {
            this.selectedThematicId =
              this.thematics.length > 0 ? this.thematics[0].id : "";
            if (this.selectedThematicId) {
              await this.loadChaptersByThematic();
            } else {
              this.chapters = [];
            }
          }
        }

        this.successMessage = "تم الحذف بنجاح";
        setTimeout(() => {
          this.successMessage = null;
        }, 3000);
      } catch (error) {
        console.error("Error deleting:", error);
        this.error =
          this.deleteType === "chapter"
            ? "فشل في حذف الفصل. قد يكون هناك فصول فرعية مرتبطة به."
            : "فشل في حذف المجال. قد يكون هناك فصول مرتبطة به.";
      } finally {
        this.loading = false;
        this.itemToDelete = null;
        this.deleteType = "";
      }
    },
  },
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

/* Search styling */
.search-container {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.search-input-wrapper {
  position: relative;
  width: 100%;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e1e8f0;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.search-input:focus {
  border-color: #4361ee;
  outline: none;
  box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.15);
}

.clear-search {
  position: absolute;
  top: 50%;
  left: 16px;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: #888;
  font-size: 0.9rem;
}

.search-info {
  margin-top: 8px;
  font-size: 0.85rem;
  color: #666;
  text-align: right;
}

.search-highlight {
  background-color: #fff9e5 !important;
}

.no-results {
  text-align: center;
  padding: 32px 0;
  color: #666;
  font-style: italic;
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

th,
td {
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

.view-btn,
.edit-btn,
.delete-btn {
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

.page-btn:hover:not(:disabled) {
  background-color: #f0f4f8;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

/* Ajouter ces styles pour le modal de confirmation */
.confirm-delete-btn {
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-delete-btn:hover {
  background-color: #c0392b;
}

.modal-body p {
  margin: 1rem 0;
  font-size: 1.1rem;
  color: #333;
}
</style>