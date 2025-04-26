import axios from "axios";

export default {
    getAllSuperChapitre(thematicId) {
        return axios.get(`chapitres/get/${thematicId}`);
    },
    async getChapitreProgress(id){
        try {
            const response = await axios.get('chapitres/getProgress/'+id);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    }
};