import Axios from 'axios';

const API_URL = 'http://localhost:9090/api/Reponse';

export default {
    async getAllReponses() {
        try {
            const response = await Axios.get(`${API_URL}/getAll`);
            return response.data;
        } catch (err) {
            console.error('Error getting all responses:', err);
            throw err;
        }
    },

    async getReponseById(id) {
        try {
            const response = await Axios.get(`${API_URL}/get/${id}`);
            return response.data;
        } catch (err) {
            console.error(`Error getting response with id ${id}:`, err);
            throw err;
        }
    },

    async createReponse(reponseRequest) {
        try {
            const response = await Axios.post(`${API_URL}/create`, reponseRequest);
            return response.data;
        } catch (err) {
            console.error('Error creating response:', err);
            throw err;
        }
    },

    async updateReponse(id, reponseRequest) {
        try {
            const response = await Axios.put(`${API_URL}/update/${id}`, reponseRequest);
            return response.data;
        } catch (err) {
            console.error(`Error updating response with id ${id}:`, err);
            throw err;
        }
    },

    async deleteReponse(id) {
        try {
            const response = await Axios.delete(`${API_URL}/delete/${id}`);
            return response.data;
        } catch (err) {
            console.error(`Error deleting response with id ${id}:`, err);
            throw err;
        }
    },

    async deleteAllReponses() {
        try {
            const response = await Axios.delete(`${API_URL}/deleteAll`);
            return response.data;
        } catch (err) {
            console.error('Error deleting all responses:', err);
            throw err;
        }
    }
}