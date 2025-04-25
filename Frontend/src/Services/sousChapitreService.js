import axios from 'axios';

export default {
    getAllSousChapitre(chapitreId) {
        return axios.get('SousChapitres/getByChapitreId/' + chapitreId);
    },
    async setLastReadPage(id, currentPage) {
        try {
            const response = await axios.put(`SousChapitres/setLastPageRead/${id}`,
                { pageNumber: currentPage });
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    },
    
    async getLastReadPage(id) {
        try {
            const response = await axios.get(`SousChapitres/getLastReadPage/${id}`);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    }
}