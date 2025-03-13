<template>
    <div class="chat-container">
        <div class="chat-header">
            <h3>Assistant virtuel</h3>
        </div>
        
        <div class="chat-box">
            <div v-for="(msg, index) in messages" :key="index"
                :class="['message', msg.type === 'bot' ? 'bot' : 'user']">
                <div class="message-content">
                    <p>{{ msg.text }}</p>
                </div>
                <div class="message-time" v-if="msg.time">{{ msg.time }}</div>
            </div>
        </div>
        
        <div class="input-container">
            <input v-model="userMessage" type="text" placeholder="Votre message..." @keyup.enter="sendMessage" />
            <button @click="sendMessage">
                <v-icon color="white" size="20">mdi-send</v-icon>
            </button>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            userMessage: "",
            messages: [],
        };
    },
    methods: {
        async sendMessage() {
            if (!this.userMessage.trim()) return;

            // Ajout du message utilisateur avec timestamp
            const now = new Date();
            const timeString = now.getHours() + ':' + (now.getMinutes() < 10 ? '0' : '') + now.getMinutes();
            
            this.messages.push({ 
                text: this.userMessage, 
                type: "user",
                time: timeString
            });

            try {
                // Ajouter un indicateur de chargement
                const loadingIndex = this.messages.length;
                this.messages.push({ 
                    text: "En cours de réponse...", 
                    type: "bot",
                    loading: true
                });

                const response = await axios.post("http://127.0.0.1:5000/chat", {
                    message: this.userMessage,
                });

                // Remplacer l'indicateur de chargement par la réponse
                this.messages.splice(loadingIndex, 1);
                this.messages.push({ 
                    text: response.data.response, 
                    type: "bot",
                    time: timeString
                });
            } catch (error) {
                console.error("Erreur:", error);
                this.messages.push({
                    text: "Désolé, je rencontre un problème de connexion. Veuillez réessayer.",
                    type: "bot",
                    time: timeString
                });
            }

            this.userMessage = ""; // Effacer la saisie utilisateur
            
            // Scroll to bottom
            this.$nextTick(() => {
                const chatBox = document.querySelector('.chat-box');
                chatBox.scrollTop = chatBox.scrollHeight;
            });
        },
    },
};
</script>

<style scoped>
.chat-container {
    width: 380px;
    max-width: 95%;
    margin: auto;
    background: white;
    border-radius: 12px;
    box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    display: flex;
    flex-direction: column;
    height: 600px;
}

.chat-header {
    background: #2c3e50;
    color: white;
    padding: 15px 20px;
    text-align: center;
    font-weight: 500;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.chat-header h3 {
    margin: 0;
    font-size: 18px;
    letter-spacing: 0.5px;
}

.chat-box {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
    background-color: #f8f9fa;
    display: flex;
    flex-direction: column;
}

.message {
    margin-bottom: 16px;
    display: flex;
    flex-direction: column;
    max-width: 80%;
}

.message-content {
    padding: 12px 16px;
    border-radius: 18px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    position: relative;
}

.message-time {
    font-size: 11px;
    color: #999;
    margin-top: 4px;
    padding: 0 8px;
}

.user {
    align-self: flex-end;
}

.user .message-content {
    background: #3498db;
    color: white;
    border-bottom-right-radius: 4px;
}

.user .message-time {
    align-self: flex-end;
}

.bot {
    align-self: flex-start;
}

.bot .message-content {
    background: white;
    color: #333;
    border-bottom-left-radius: 4px;
    border: 1px solid #e6e6e6;
}

.bot .message-time {
    align-self: flex-start;
}

.input-container {
    display: flex;
    padding: 15px;
    background: white;
    border-top: 1px solid #eaeaea;
}

input {
    flex: 1;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 24px;
    font-size: 14px;
    transition: all 0.3s;
}

input:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

button {
    margin-left: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: #3498db;
    color: white;
    border: none;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background 0.3s;
}

button:hover {
    background: #2980b9;
}

/* Animation pour le chargement */
.message.bot .message-content.loading:after {
    content: '';
    animation: dots 1.5s infinite;
}

@keyframes dots {
    0%, 20% { content: '.'; }
    40% { content: '..'; }
    60%, 100% { content: '...'; }
}

/* Style pour mobile */
@media (max-width: 480px) {
    .chat-container {
        width: 100%;
        height: 100vh;
        border-radius: 0;
    }
    
    .message {
        max-width: 90%;
    }
}
</style>