<template>
    <div class="agenda-container">

        <Navbar @openUpdateAccount="showUpdateAccount = true" />

        <v-dialog v-model="showUpdateAccount" persistent width="auto">
            <UpdateAccount @closeUpdateAccount="showUpdateAccount = false" />
        </v-dialog>

        <h2>أجندة</h2>
        <div class="journal">
            <div id="pageContainer" class="journal-page">
                <div class="half-page">
                    <textarea v-model="pages[currentPage].right" placeholder="اكتب هنا..."></textarea>
                    <div class="page-number right">{{ currentPage * 2 + 1 }}</div>
                </div>
                <div class="half-page">
                    <textarea v-model="pages[currentPage].left" placeholder="اكتب هنا..."></textarea>
                    <div class="page-number left">{{ currentPage * 2 + 2 }}</div>
                </div>
                <div class="page-divider"></div>
            </div>
            <div class="buttons">

                <button class="btn btn-circle btn-next" @click="prevPage">⬅</button>
                <button class="btn btn-circle btn-prev" @click="nextPage">➡</button>
                <button class="btn btn-circle btn-delete" @click="deleteNote">✕</button>
                <button class="btn btn-circle btn-save" @click="saveNotes">💾</button>
            </div>
        </div>
        <Footer />
    </div>
</template>

<script>
import Navbar from "../components/Navbar.vue";
import Footer from "../components/Footer.vue";
import UpdateAccount from "@/components/UpdateAccount.vue";

export default {
  name: "AgendaView",
  components: { Navbar, Footer, UpdateAccount },

  data() {
    return {
      showUpdateAccount: false,
      pages: [{ left: '', right: '' }],
      currentPage: 0,
    };
  },

  mounted() {
    const saved = localStorage.getItem('journalPages');
    if (saved) {
      this.pages = JSON.parse(saved);
    }
  },

  watch: {
    pages: {
      handler(newVal) {
        localStorage.setItem('journalPages', JSON.stringify(newVal));
      },
      deep: true,
    },
  },

  methods: {
    saveNotes() {
      alert("تم الحفظ بنجاح!");
    },
    nextPage() {
      this.currentPage++;
      if (!this.pages[this.currentPage]) {
        this.pages.push({ left: '', right: '' });
      }
    },
    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
      }
    },
    deleteNote() {
      this.pages[this.currentPage] = { left: '', right: '' };
    },
  },
};
</script>


<style scoped>
body {
    background-color: #1faaea;
    font-family: 'Arial', sans-serif;
    direction: rtl;
    text-align: center;
}

h2 {
    text-align: center;
    font-size: 30px;
    color: #0d47a1;
    font-family: "Segoe UI", sans-serif;
    margin-bottom: 10px;
}

.agenda-container {
    max-width: 1200px;
    margin: 40px auto;
    padding: 20px;
    position: relative;
}

.journal {
    position: relative;
}

.journal-page {
    width: 100%;
    height: 500px;
    background: white;
    border: 10px solid #217CA3;
    border-radius: 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: stretch;
    overflow: hidden;
    position: relative;
}

.half-page {
    width: 50%;
    padding: 20px;
    box-sizing: border-box;
    position: relative;
}

.half-page textarea {
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
    resize: none;
    font-size: 18px;
    line-height: 1.6;
    background: transparent;
}

.page-divider {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 50%;
    width: 1px;
    background-color: #aaa;
    z-index: 10;
}

.page-number {
    position: absolute;
    bottom: 10px;
    font-size: 14px;
    color: #666;
}

.page-number.right {
    left: 20px;
}

.page-number.left {
    left: 20px;
}

.buttons {
    position: absolute;
    top: 50%;
    right: -70px;
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.btn-circle {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
    border: none;
    color: white;
    font-size: 20px;
}

.btn-next {
    background-color: #22c55e;
}

.btn-delete {
    background-color: #ef4444;
}

.btn-prev {
    background-color: #f59e0b;
}

.btn-save {
    background-color: #0ea5e9;
}

textarea {
    text-align: right;
}

@media (max-width: 768px) {
    .buttons {
        position: relative;
        flex-direction: row;
        justify-content: center;
        left: 0;
        margin-top: 20px;
    }
}
</style>