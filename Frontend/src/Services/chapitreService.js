import axios from 'axios';

export default {
    getAllSuperChapitre(thematicId) {
        return axios.get(`chapitres/super/${thematicId}`);
    }
}