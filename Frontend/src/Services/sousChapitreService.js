import axios from 'axios';

export default {
    getAllSousChapitre(chapitreId) {
        return axios.get(`chapitres/sous/${chapitreId}`);
    }
}