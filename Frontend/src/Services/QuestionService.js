import Axios from 'axios';
import {useUserStore} from '@/store/User/userStore';

const API_URL = 'http://localhost:9090/api/Question';

export default {
    async getAllQuestions(test) {
        try {
            const testData = {
                id: test.id,
                nomTest: test.nomTest || test.title,
                typeTest: test.typeTest || test.type_test,
                utilisable: test.utilisable === true ? 1 : 0
            };
            
            console.log('Sending test data:', testData);
            const response = await Axios.post(`${API_URL}/getQuestionsTest`, testData);
            return response.data;
        } catch (err) {
            console.error('Error getting questions for test:', err);
            throw err;
        }
    },
    
    async setPersonnalite(type, id) {
        try {
            const store = useUserStore();
            await Axios.post("api/Utilisateur/setPersonnalite", {
                type: type,
                id: id
            });
            store.setPersonnalite(type);
        } catch (err) {
            console.error('Error setting personnalite:', err);
            throw err;
        }
    },
    
    async createQuestion(questionRequest) {
        try {
            console.log('Creating question with request:', questionRequest);
            const response = await Axios.post(`${API_URL}/create`, questionRequest);
            return response.data;
        } catch (err) {
            console.error('Error creating question:', err);
            throw err;
        }
    },

    async getQuestionById(id) {
        try {
            const response = await Axios.get(`${API_URL}/get/${id}`);
            return response.data;
        } catch (err) {
            console.error(`Error getting question with id ${id}:`, err);
            throw err;
        }
    },

    async updateQuestion(id, questionRequest) {
        try {
            const response = await Axios.put(`${API_URL}/update/${id}`, questionRequest);
            return response.data;
        } catch (err) {
            console.error(`Error updating question with id ${id}:`, err);
            throw err;
        }
    },

    async deleteQuestion(id) {
        try {
            const response = await Axios.delete(`${API_URL}/delete/${id}`);
            return response.data;
        } catch (err) {
            console.error(`Error deleting question with id ${id}:`, err);
            throw err;
        }
    },

    async deleteAllQuestions() {
        try {
            const response = await Axios.delete(`${API_URL}/deleteAll`);
            return response.data;
        } catch (err) {
            console.error('Error deleting all questions:', err);
            throw err;
        }
    }
}