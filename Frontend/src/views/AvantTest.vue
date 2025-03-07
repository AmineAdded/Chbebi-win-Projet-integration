<template>
    <div v-if="isLoading" class="loader-container">
        <div class="loader-content">
            <v-progress-circular
                indeterminate
                color="primary"
                size="64"
            ></v-progress-circular>
            <span class="loader-text">...مرحبا بك</span>
        </div>
    </div>
    <div v-else>
    <v-container fluid class="about-container">
      <v-row justify="center" align="center">
        <v-col cols="20" md="8" lg="7">
          <!-- Logo and Title Section with improved layout -->
          <div class="header-section">
            <div class="title-container">
              <h1 class="main-title">CHBEBIWIN<br /><span class="arabic-title">شبابي وين</span></h1>
            </div>
            <img :src="logo" alt="logo" class="logo-img" />
          </div>
          
          <!-- Content Section with improved typography and spacing -->
          <div class="content-section">
            <h2 class="intro-text">قبل كل شيء</h2>
            
            <div class="description-text">
              <p>نحبك تجاوب على شويا أسئلة بش تعرف روحك أكثر وتعاونا إنو نقدمولك إلي يفيدك</p>
              <p>الأسئلة هاذم معمولين خصيصا بش نفهمو الجوهر متاعك ونحاولو من خلالهم نحددولك شخصيتك</p>
              <p>فكانك مستعد إنزل على بدء ويلا</p>
              <div class="important-note">
                <v-icon color="red" class="note-icon">mdi-alert-circle</v-icon>
                <p>ملاحظة هامة برشا: حاول تجاوب بصدق</p>
              </div>
            </div>
            
            <div class="button-container">
              <v-btn 
                elevation="2" 
                class="start-button" 
                rounded 
                x-large
                @click.prevent="$emit('openSignUp')"
              >
                <v-icon left>mdi-play-circle</v-icon>
                <span class="button-text">بدء</span>
              </v-btn>
            </div>
          </div>
        </v-col>
      </v-row>
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
        isLoading:true,
      };
    },
    created() {
        setTimeout(() => {
            this.isLoading = false;
        }, 1000);
    },
  };
  </script>
    
  <style scoped>
  /* Main container with improved gradient background */
  .about-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #a6cce0 0%, #91b5c9 50%, #7da9c0 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
  }
  
  /* Header section with improved spacing and alignment */
  .header-section {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 50px;
    flex-wrap: wrap;
  }
  
  .title-container {
    flex: 1;
    min-width: 220px;
    position: relative;
    left:50px;
  }
  
  .main-title {
    color: #152538;
    font-size: 3.2rem;
    font-weight: 900;
    text-align: center;
    line-height: 1.2;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
    margin: 0;
  }
  
  .arabic-title {
    font-family: 'Amiri', 'Tajawal', sans-serif;
    font-size: 3rem;
    display: block;
    margin-top: 5px;
  }
  
  .logo-img {
    width: 220px;
    height: 180px;
    filter: drop-shadow(0 8px 16px rgba(0, 0, 0, 0.15));
    transition: transform 0.4s ease, filter 0.4s ease;
    position: relative;
    right:50px;
  }
  
  .logo-img:hover {
    transform: scale(1.05) rotate(2deg);
    filter: drop-shadow(0 12px 20px rgba(0, 0, 0, 0.2));
  }
  
  /* Content section with improved typography */
  .content-section {
    text-align: center;
    direction: rtl;
  }
  
  .intro-text {
    font-size: 2.4rem;
    font-weight: 700;
    color: #152538;
    margin-bottom: 35px;
    position: relative;
    display: inline-block;
  }
  
  .intro-text:after {
    content: "";
    position: absolute;
    bottom: -12px;
    left: 50%;
    transform: translateX(-50%);
    width: 100px;
    height: 4px;
    background: linear-gradient(90deg, #152538 0%, #2a4a6d 100%);
    border-radius: 4px;
  }
  
  .description-text {
    margin: 40px 0;
    max-width: 90%;
    margin-left: auto;
    margin-right: auto;
  }
  
  .description-text p {
    font-family: 'Tajawal', 'Segoe UI', sans-serif;
    font-size: 1.6rem;
    line-height: 1.8;
    color: #152538;
    margin-bottom: 22px;
    font-weight: 500;
  }
  
  .important-note {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 30px;
    background-color: rgba(255, 0, 0, 0.08);
    padding: 15px;
    border-radius: 12px;
    border-right: 4px solid #ff3333;
  }
  
  .note-icon {
    margin-left: 10px;
    font-size: 24px;
  }
  
  .important-note p {
    color: #ff3333;
    font-weight: 700;
    margin: 0;
  }
  
  /* Button styles with improved animation and design */
  .button-container {
    margin-top: 50px;
    display: flex;
    justify-content: center;
  }
  
  .start-button {
    background: linear-gradient(135deg, #152538 0%, #2a4a6d 100%) !important;
    color: white !important;
    font-size: 1.8rem !important;
    padding: 12px 50px !important;
    border-radius: 50px !important;
    transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) !important;
    position: relative;
    overflow: hidden;
    font-family: 'Tajawal', 'Segoe UI', sans-serif !important;
    font-weight: 700 !important;
    letter-spacing: 1px;
    min-width: 180px;
    height: 65px;
    box-shadow: 0 10px 20px rgba(21, 37, 56, 0.25) !important;
  }
  
  .start-button::before {
    content: "";
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    transition: 0.6s;
  }
  
  .start-button:hover {
    transform: translateY(-7px);
    box-shadow: 0 15px 30px rgba(21, 37, 56, 0.35) !important;
    background: linear-gradient(135deg, #1e3754 0%, #365f8d 100%) !important;
  }
  
  .start-button:active {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(21, 37, 56, 0.35) !important;
  }
  
  .start-button:hover::before {
    left: 100%;
  }
  
  .button-text {
    position: relative;
    z-index: 1;
    margin-right: 8px;
  }
  
  /* Responsive adjustments */
  @media (max-width: 960px) {
    .header-section {
      flex-direction: column;
      text-align: center;
      justify-content: center;
    }
    
    .title-container {
      width: 100%;
    }
    
    .logo-img {
      margin: 0 auto;
    }
  }
  
  @media (max-width: 768px) {
    .content-wrapper {
      padding: 30px 20px;
    }
    
    .main-title {
      font-size: 2.4rem;
    }
    
    .arabic-title {
      font-size: 2.2rem;
    }
    
    .logo-img {
      width: 180px;
      height: 150px;
    }
    
    .intro-text {
      font-size: 2rem;
    }
    
    .description-text p {
      font-size: 1.3rem;
    }
    
    .start-button {
      font-size: 1.5rem !important;
      padding: 10px 40px !important;
      height: 60px;
    }
  }
  
  @media (max-width: 480px) {
    .main-title {
      font-size: 2rem;
    }
    
    .arabic-title {
      font-size: 1.8rem;
    }
    
    .intro-text {
      font-size: 1.7rem;
    }
    
    .description-text p {
      font-size: 1.1rem;
      line-height: 1.6;
    }
    
    .start-button {
      font-size: 1.3rem !important;
      padding: 8px 30px !important;
      height: 55px;
    }
  }

  .loader-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    background: rgba(25, 118, 210, 0.03);
    z-index: 1001;
    backdrop-filter: blur(8px);
}
.loader-text {
    color: #1976d2;
    font-size: 2em;
    font-weight: 500;
    animation: pulse 2s infinite;
    padding-left: 14px;
    padding-top: 6px;
}
  </style>