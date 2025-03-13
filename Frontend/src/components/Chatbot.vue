<template>
    <div class="chat-container">

        <div class="chat-box">

            <div v-for="(msg, index) in messages" :key="index"
                :class="['message', msg.type === 'bot' ? 'bot' : 'user']">
                <p>{{ msg.text }}</p>
            </div>

        </div>
        <div class="input-container">
            <input v-model="userMessage" type="text" placeholder="Tapez votre message..." @keyup.enter="sendMessage" />
            <button @click="sendMessage">Envoyer</button>
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

            // Ajouter le message de l'utilisateur
            this.messages.push({ text: this.userMessage, type: "user" });

            try {
                const response = await axios.post("http://127.0.0.1:5000/chat", {
                    message: this.userMessage,
                });

                // Ajouter la réponse du bot
                this.messages.push({ text: response.data.response, type: "bot" });
            } catch (error) {
                console.error("Erreur:", error);
                this.messages.push({
                    text: "Erreur de connexion avec le bot.",
                    type: "bot",
                });
            }

            this.userMessage = ""; // Effacer la saisie utilisateur
        },
    },
};
</script>

<style>
.chat-container {
    width: 400px;
    margin: auto;
    background: white;
    padding: 15px;
    border-radius: 10px;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
}

.chat-box {
    height: 300px;
    overflow-y: auto;
    border-bottom: 1px solid #ddd;
    padding-bottom: 10px;
}

.message {
    display: flex;
    margin-bottom: 10px;
}

.message p {
    padding: 10px;
    border-radius: 10px;
    max-width: 80%;
}
.user {
    justify-content: flex-end; /* Aligner le message de l'utilisateur à droite */
    display: flex;
}

.user p {
    background: #007bff;
    color: white;
    align-self: flex-end;
    text-align: right;
}

.bot {
    justify-content: flex-start;
    /* Aligner les messages bot à droite */
}

.bot p {
    background: #eee;
    color: black;
    align-self: flex-end;
    /* Aligner le texte à droite */
    text-align: left;
}


.input-container {
    display: flex;
    margin-top: 10px;
}

input {
    flex: 1;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
}

button {
    padding: 10px;
    background: #007bff;
    color: white;
    border: none;
    cursor: pointer;
}
</style>