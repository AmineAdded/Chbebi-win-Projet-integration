import Axios from 'axios';

const API_URL = 'http://localhost:9090/api/Test';


export default {
    async getAllTests() {
        try {
            const response = await Axios.get(`${API_URL}/getAll`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },

    async getTestById(id) {
        try {
            const response = await Axios.get(`${API_URL}/get/${id}`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },

    async getTestUtilisable() {
        try {
            const response = await Axios.get(`${API_URL}/getTestUtilisable`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },

    async createTest(test) {
        try {
            const response = await Axios.post(`${API_URL}/create`, test);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },

    async updateTest(id, test) {
        try {
            console.log(`Sending PUT request to ${API_URL}/update/${id} with data:`, test);
            const response = await Axios.put(`${API_URL}/update/${id}`, test);
            console.log("Update response:", response.data);
            return response.data;
        } catch (err) {
            console.error("Error in updateTest:", err);
            console.error("Error response:", err.response?.data);
            throw err;
        }
    },

    async deleteTest(id) {
        try {
            const response = await Axios.delete(`${API_URL}/delete/${id}`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },

    async deleteAllTests() {
        try {
            const response = await Axios.delete(`${API_URL}/deleteAll`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },
    
    async getTestsByType(type) {
        try {
            const allTests = await this.getAllTests();
            return allTests.filter(test => test.typeTest === type || test.type_test === type);
        } catch (err) {
            console.error(err);
            throw err;
        }
    }
}