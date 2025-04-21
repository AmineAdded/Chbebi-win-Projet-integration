
from flask import Flask, request, jsonify
import google.generativeai as genai
from flask_cors import CORS  # Pour autoriser les requêtes cross-origin
from langdetect import detect_langs, DetectorFactory

# Assure que la détection de langue est stable (évite les résultats aléatoires)
DetectorFactory.seed = 0

# Liste des langues acceptées
ALLOWED_LANGUAGES = {'en', 'fr', 'ar'}

# Configuration de l'API Google Gemini
genai.configure(api_key="AIzaSyCWIoKMGRhJW1veIwvgIIWtEHfMIrThwLs")  # Remplace par ta clé API

# Création de l'application Flask
app = Flask(__name__)
CORS(app)  # Autorise toutes les origines à accéder à l'API Flask

def detect_language(text):
    """
    Détecte la langue du texte en s'assurant qu'elle est fiable.
    Retourne "und" si aucune langue valide n'est détectée.
    """
    try:
        lang_probabilities = detect_langs(text)
        for lang in lang_probabilities:
            if lang.lang in ALLOWED_LANGUAGES and lang.prob > 0.8:  # Vérifie si la langue est fiable
                return lang.lang
        return "und"  # Retourne "und" si aucune langue fiable n'est trouvée
    except:
        return "und"  # En cas d'erreur, retourne "und"

def chat_with_gemini(prompt, lang):
    """
    Génère une réponse avec l'API Gemini en tenant compte de la langue détectée.
    """
    model = genai.GenerativeModel(model_name="gemini-1.5-flash-latest")  # Sélection du modèle

    # Construction du prompt avec la langue détectée
    if lang == "ar":
        lang_instruction = "Réponds exclusivement en dialecte tunisien (Darja), sans utiliser d'autres dialectes comme l'algérien ou le marocain. Utilise uniquement des mots et expressions typiquement tunisiens"
    elif lang == "fr":
        lang_instruction = "Réponds en français de manière concise et claire."
    elif lang == "en":
        lang_instruction = "Réponds en anglais de manière concise et claire."
    else:
        lang_instruction = "Réponds de manière concise et claire dans la langue que tu juges appropriée."

    response = model.generate_content(f"{lang_instruction} {prompt}")
    return response.text.strip()  # Nettoie la réponse


@app.route('/chat', methods=['POST'])
def chat():
    """
    Route principale pour recevoir un message, détecter sa langue et générer une réponse.
    """
    # Récupération du message utilisateur
    user_message = request.json.get('message')
    if not user_message:
        return jsonify({'error': 'No message provided'}), 400

    # Détection de la langue du message
    detected_lang = detect_language(user_message)

    # Appel à l'API Gemini pour obtenir une réponse
    try:
        bot_response = chat_with_gemini(user_message, detected_lang)
        return jsonify({'response': bot_response, 'detected_language': detected_lang})
    except Exception as e:
        return jsonify({'error': str(e)}), 500  # Gestion des erreurs

if __name__ == "__main__":
    app.run(debug=True)  # Démarre le serveur Flask
