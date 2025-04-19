<template>
    <div v-if="isLoading" class="loader-container">
        <div class="loader-content">
            <div class="custom-loader">
                <div class="pulse-ring"></div>
            </div>
        </div>
    </div>
    <div v-else class="entrance-container main-container">
        <div class="bg-decorations">
            <div class="circle circle-1"></div>
            <div class="circle circle-2"></div>
            <div class="circle circle-3"></div>
        </div>

        <div class="content-wrapper">
            <div class="logo-container">
                <div class="logo-glow"></div>
                <img :src="brainLogo" alt="Logo" class="brain-logo" />
            </div>

            <div class="welcome-text">
                <p class="arabic-subtitle">مرحبا بيك في</p>
                <h1 class="arabic-title">شبابي وين</h1>
                <div class="underline-decoration"></div>
            </div>

            <div class="navigation-buttons">
                <button class="custom-button home-button" @click="navigateTo('privatehome')">
                    <span class="button-icon home-icon"></span>
                    <span class="button-text">الصفحة الرئيسية</span>
                </button>

                <button class="custom-button culture-button" @click="navigateTo('learn')">
                    <span class="button-icon culture-icon"></span>
                    <span class="button-text">تعرف على بلدك وثقافتك</span>
                </button>

                <button class="custom-button religion-button" @click="navigateTo('religion')">
                    <span class="button-icon religion-icon"></span>
                    <span class="button-text">تعرف على دينك</span>
                </button>

                <button class="custom-button personality-button" @click="navigateTo('personality')">
                    <span class="button-icon personality-icon"></span>
                    <span class="button-text">تعرف على شخصيتك أكثر</span>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import brainLogo from '../assets/logo.png';

export default {
    name: "EntranceView",
    data() {
        return {
            isLoading: true,
            brainLogo: brainLogo,
        };
    },
    created() {
        setTimeout(() => {
            this.isLoading = false;
        }, 1500);
    },
    methods: {
        navigateTo(route) {
            const routes = {
                privatehome: '/privatehome',
                learn: '/islamic/:topicId',
                religion: '/islam',
                personality: '/DetailsPersonnality',
            };

            const buttons = document.querySelectorAll('.custom-button');
            buttons.forEach(button => {
                button.classList.add('fade-out');
            });

            setTimeout(() => {
                this.$router.push(routes[route]);
            }, 300);
        }
    }
};
</script>

<style scoped>
/* Importation des polices */
@import url('https://fonts.googleapis.com/css2?family=Tajawal:wght@300;400;500;700&display=swap');

/* Animation du loader */
@keyframes spin {
    to { transform: rotate(360deg); }
}

@keyframes pulse {
    0% { transform: scale(0.8); opacity: 0.3; }
    50% { transform: scale(1.2); opacity: 0.6; }
    100% { transform: scale(0.8); opacity: 0.3; }
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
}

@keyframes float {
    0% { transform: translateY(0px); }
    50% { transform: translateY(-10px); }
    100% { transform: translateY(0px); }
}

@keyframes glow {
    0% { box-shadow: 0 0 10px rgba(76, 170, 229, 0.5); }
    50% { box-shadow: 0 0 30px rgba(76, 170, 229, 0.8); }
    100% { box-shadow: 0 0 10px rgba(76, 170, 229, 0.5); }
}

@keyframes shimmer {
    0% { background-position: -200% 0; }
    100% { background-position: 200% 0; }
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
    background: linear-gradient(135deg, #b1d1e6, #8bb7d2);
    z-index: 1001;
}

.custom-loader {
    position: relative;
    width: 100px;
    height: 100px;
}

.pulse-ring {
    position: absolute;
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: rgba(26, 58, 95, 0.2);
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    animation: pulse 2s ease-out infinite;
}

.entrance-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #b1d1e6, #8bb7d2);
    padding: 20px;
    position: relative;
    overflow: hidden;
}

.bg-decorations {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 0;
}

.circle {
    position: absolute;
    border-radius: 50%;
    opacity: 0.2;
}

.circle-1 {
    width: 300px;
    height: 300px;
    background-color: #4caae5;
    top: -150px;
    right: -100px;
}

.circle-2 {
    width: 500px;
    height: 500px;
    background-color: #2271b3;
    bottom: -250px;
    left: -200px;
}

.circle-3 {
    width: 200px;
    height: 200px;
    background-color: #1a3a5f;
    top: 20%;
    right: 5%;
}

.content-wrapper {
    max-width: 700px;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 40px;
    z-index: 1;
    animation: fadeIn 1s ease-out;
}

.logo-container {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 10px;
    position: relative;
    animation: float 6s ease-in-out infinite;
}

.logo-glow {
    position: absolute;
    width: 160px;
    height: 160px;
    border-radius: 50%;
    background-color: rgba(255, 255, 255, 0.2);
    animation: glow 3s infinite;
}

.brain-logo {
    width: 200px;
    height: auto;
    position: relative;
    z-index: 2;
    filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
}

.welcome-text {
    text-align: center;
    margin-bottom: 30px;
    position: relative;
}

.arabic-title {
    font-size: 3.2rem;
    font-weight: 700;
    color: #1a3a5f;
    margin-bottom: 5px;
    font-family: 'Tajawal', sans-serif;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.arabic-subtitle {
    font-size: 1.7rem;
    color: #1a3a5f;
    font-family: 'Tajawal', sans-serif;
    font-weight: 300;
    margin-bottom: 10px;
}

.underline-decoration {
    width: 100px;
    height: 4px;
    background: linear-gradient(90deg, transparent, #2271b3, transparent);
    margin: 15px auto 0;
    border-radius: 2px;
}

.navigation-buttons {
    display: flex;
    flex-direction: column;
    gap: 20px;
    width: 100%;
}

/* Style commun pour tous les boutons */
.custom-button {
    font-family: 'Tajawal', sans-serif;
    font-size: 1.2rem;
    font-weight: 500;
    padding: 16px 25px;
    width: 100%;
    text-align: center;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
    color: #1a3a5f;
    border: 3px solid;
    position: relative;
    overflow: hidden;
    box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
}

.custom-button:hover {
    transform: translateY(-5px) scale(1.02);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.custom-button:active {
    transform: translateY(2px);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.button-text {
    position: relative;
    z-index: 2;
}

.button-icon {
    width: 24px;
    height: 24px;
    margin-left: 15px;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
}

/* Animation sur les boutons */
.custom-button::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(
        90deg,
        rgba(255, 255, 255, 0) 0%,
        rgba(255, 255, 255, 0.2) 50%,
        rgba(255, 255, 255, 0) 100%
    );
    transition: all 0.6s;
}

.custom-button:hover::before {
    left: 100%;
}

/* Style spécifique pour chaque bouton */
.home-button {
    background-color: rgba(177, 209, 230, 0.9);
    border-color: #1a3a5f;
    backdrop-filter: blur(5px);
}

.culture-button {
    background-color: #2271b3;
    color: white;
    border-color: #185888;
}

.religion-button {
    background-color: #4caae5;
    color: white;
    border-color: #3a8fc2;
}

.personality-button {
    background-color: rgba(255, 255, 255, 0.9);
    border-color: #1a3a5f;
    backdrop-filter: blur(5px);
}

/* Animation de sortie */
.fade-out {
    opacity: 0;
    transform: translateY(20px);
    transition: opacity 0.3s, transform 0.3s;
}

/* Responsive */
@media (max-width: 768px) {
    .arabic-title {
        font-size: 2.5rem;
    }

    .arabic-subtitle {
        font-size: 1.4rem;
    }
    
    .brain-logo {
        width: 160px;
    }
    
    .logo-glow {
        width: 140px;
        height: 140px;
    }
    
    .custom-button {
        font-size: 1.1rem;
        padding: 14px 20px;
    }
}

@media (max-width: 480px) {
    .arabic-title {
        font-size: 2rem;
    }

    .arabic-subtitle {
        font-size: 1.2rem;
    }
    
    .brain-logo {
        width: 130px;
    }
    
    .logo-glow {
        width: 120px;
        height: 120px;
    }
    
    .custom-button {
        font-size: 1rem;
        padding: 12px 15px;
    }
    
    .navigation-buttons {
        gap: 15px;
        width: 100%;
    }
}
</style>