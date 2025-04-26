import axios from "axios";

export default {
    getAllSuperChapitre(thematicId) {
        return axios.get(`chapitres/super/${thematicId}`);
    },
    deleteChapitre(id) {
        return axios.delete(`chapitres/delete/${id}`);
    },
    updateChapitre(id, chapitre) {
        return axios.put(`chapitres/update/${id}`, chapitre);
    },
    createChapitre(chapitre) {
        return axios.post(`chapitres/create`, chapitre);
    },
};
