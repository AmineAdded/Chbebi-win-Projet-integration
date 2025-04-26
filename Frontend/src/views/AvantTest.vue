<template>
  <div v-if="isLoading" class="loader-container">
    <div class="loader-content">
      <div class="custom-loader">
        <div class="pulse-dot"></div>
      </div>
      <div class="mt-3 text-center loading-text">...مرحبا بك</div>
    </div>
  </div>
  <div v-else class="main-container">
    <v-container fluid class="about-container">
      <v-row justify="center" align="center">
        <v-col cols="12" md="9" lg="7">
          <div class="glass-card">
            <div class="header-section">
              <div class="title-container">
                <h1 class="main-title">CHBEBIWIN<span class="arabic-title">شبابي وين</span></h1>
              </div>
              <div class="logo-wrapper">
                <img :src="logo" alt="logo" class="logo-img" />
              </div>
            </div>
            
            <div class="content-section">
              <h2 class="intro-text">قبل كل شيء</h2>
              
              <div class="description-text">
                <p>نحبك تجاوب على شويا أسئلة بش تعرف روحك أكثر وتعاونا إنو نقدمولك إلي يفيدك</p>
                <p>الأسئلة هاذم معمولين خصيصا بش نفهمو الجوهر متاعك ونحاولو من خلالهم نحددولك شخصيتك</p>
                <p>فكانك مستعد إنزل على بدء ويلا</p>
                <div class="important-note">
                  <v-icon color="#eb4a4a" size="20">mdi-alert-circle</v-icon>
                  <p>ملاحظة هامة برشا: حاول تجاوب بصدق</p>
                </div>
              </div>
              
              <div class="button-container">
                <v-btn 
                  elevation="2" 
                  class="start-button" 
                  rounded 
                  @click="handleClick"
                >
                  <v-icon left size="20">mdi-play-circle</v-icon>
                  <span class="button-text">بدء</span>
                </v-btn>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>
      <div class="floating-shape shape1"></div>
      <div class="floating-shape shape2"></div>
    </v-container>
  </div>
</template>
  
<script>
import logo from "../assets/logo.png";

export default {
  name: "AvantTest",
  data() {
    return {
      logo: logo,
      isLoading: true,
    };
  },
  created() {
    setTimeout(() => {
      this.isLoading = false;
    }, 1500);
  },
  methods:{
    handleClick(){
      this.$router.push('/Test');
    }
  }
};
</script>
  
<style scoped>
.about-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f5ff 0%, #e4edff 40%, #d8e6ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  font-family: 'Tajawal', sans-serif;
  position: relative;
  overflow: hidden;
}

.floating-shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.3;
  filter: blur(30px);
  z-index: 0;
}

.shape1 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #b8d3ff 0%, transparent 70%);
  top: -50px;
  right: -50px;
  animation: float 15s infinite alternate ease-in-out;
}

.shape2 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, #6c9aff 0%, transparent 70%);
  bottom: -30px;
  left: -50px;
  animation: float 12s infinite alternate-reverse ease-in-out;
}

@keyframes float {
  0% { transform: translate(0, 0); }
  100% { transform: translate(15px, 15px); }
}

.glass-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 8px 30px rgba(128, 155, 255, 0.15);
  padding: 30px;
  transition: all 0.5s cubic-bezier(0.165, 0.84, 0.44, 1);
  position: relative;
  z-index: 10;
}

.glass-card:hover {
  box-shadow: 0 12px 40px rgba(128, 155, 255, 0.2);
  transform: translateY(-5px);
}

.header-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.title-container {
  flex: 1;
  min-width: 200px;
}

.main-title {
  font-size: 2.8rem;
  font-weight: 800;
  line-height: 1;
  margin: 0;
  background: linear-gradient(90deg, #4275e9, #6c9aff);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  position: relative;
}

.arabic-title {
  font-family: 'Amiri', 'Tajawal', sans-serif;
  font-size: 2.5rem;
  display: block;
  margin-top: 8px;
  background: linear-gradient(90deg, #4275e9, #6c9aff);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: 700;
}

.logo-img {
  width: 180px;
  height: 150px;
  filter: drop-shadow(0 10px 20px rgba(107, 146, 255, 0.25));
  transition: transform 0.5s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.logo-wrapper:hover .logo-img {
  transform: scale(1.05);
}

.content-section {
  text-align: center;
  direction: rtl;
}

.intro-text {
  font-size: 2.2rem;
  font-weight: 700;
  color: #2c4ca2;
  margin-bottom: 30px;
  position: relative;
  display: inline-block;
}

.intro-text:after {
  content: "";
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 3px;
  background: linear-gradient(90deg, #4275e9, #6c9aff);
  border-radius: 3px;
}

.description-text {
  margin: 30px 0;
  max-width: 90%;
  margin-left: auto;
  margin-right: auto;
}

.description-text p {
  font-size: 1.4rem;
  line-height: 1.7;
  color: #3a4a6b;
  margin-bottom: 20px;
  font-weight: 500;
}

.important-note {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 25px;
  background-color: rgba(235, 74, 74, 0.08);
  padding: 15px;
  border-radius: 16px;
  border-right: 3px solid #eb4a4a;
  box-shadow: 0 5px 15px rgba(235, 74, 74, 0.08);
  transition: all 0.3s ease;
}

.important-note:hover {
  background-color: rgba(235, 74, 74, 0.12);
  transform: translateY(-2px);
}

.note-icon {
  margin-left: 12px;
}

.important-note p {
  color: #eb4a4a;
  font-weight: 700;
  margin: 0;
  font-size: 1.3rem;
}

.button-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

.start-button {
  background: linear-gradient(135deg, #4275e9 0%, #6c9aff 100%) !important;
  color: white !important;
  font-size: 1.6rem !important;
  padding: 10px 40px !important;
  border-radius: 50px !important;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) !important;
  position: relative;
  overflow: hidden;
  font-weight: 700 !important;
  letter-spacing: 0.5px;
  min-width: 180px;
  height: 55px;
  box-shadow: 0 10px 20px rgba(107, 146, 255, 0.3) !important;
}

.start-button:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 25px rgba(107, 146, 255, 0.4) !important;
  background: linear-gradient(135deg, #5585ea 0%, #80a9ff 100%) !important;
}

.button-text {
  margin-right: 8px;
}

.loader-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f0f5ff 0%, #e4edff 40%, #d8e6ff 100%);
  z-index: 1001;
}

.loading-text {
  color: #4275e9;
  font-size: 1.5rem;
  font-weight: 500;
  margin-top: 20px;
  animation: fadeInOut 1.5s infinite;
}

.custom-loader {
  position: relative;
  width: 80px;
  height: 80px;
}

@keyframes fadeInOut {
  0% { opacity: 0.3; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}

.pulse-dot {
  position: absolute;
  width: 25px;
  height: 25px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4275e9 0%, #6c9aff 100%);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 0 20px rgba(107, 146, 255, 0.7);
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { transform: translate(-50%, -50%) scale(0.8); opacity: 0.5; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 1; }
  100% { transform: translate(-50%, -50%) scale(0.8); opacity: 0.5; }
}

@media (max-width: 768px) {
  .main-title {
    font-size: 2.4rem;
  }
  
  .arabic-title {
    font-size: 2.1rem;
  }
  
  .intro-text {
    font-size: 1.9rem;
  }
  
  .description-text p {
    font-size: 1.2rem;
  }
  
  .start-button {
    font-size: 1.4rem !important;
    padding: 8px 35px !important;
    height: 50px;
  }
  
  .logo-img {
    width: 140px;
    height: 120px;
  }
}

@media (max-width: 480px) {
  .glass-card {
    padding: 25px 20px;
  }
  
  .header-section {
    flex-direction: column;
    text-align: center;
  }
  
  .title-container {
    margin-bottom: 20px;
  }
  
  .main-title {
    font-size: 2.1rem;
  }
  
  .arabic-title {
    font-size: 1.8rem;
  }
  
  .logo-img {
    width: 120px;
    height: 100px;
  }
  
  .intro-text {
    font-size: 1.7rem;
  }
  
  .description-text p {
    font-size: 1.1rem;
    line-height: 1.6;
  }
  
  .important-note {
    padding: 12px;
  }
  
  .important-note p {
    font-size: 1.1rem;
  }
  
  .start-button {
    font-size: 1.3rem !important;
    padding: 6px 30px !important;
    height: 45px;
  }
}
</style>