import Axios from 'axios';
import {useUserStore} from '@/store/User/userStore';
export default{
    async getAllQuestions(data){
        try{
            const response = await Axios.post('Question/getQuestionsTest',data);
            return response.data;
        }
        catch(err){
            console.error(err);
            throw err;
        }
    },
    async setPersonnalite(type,id){
        try{
            const store = useUserStore();
         await Axios.post("Utilisateur/setPersonnalite", {
            type: type,
            id: id
        });
        store.setPersonnalite(type);
        }catch(err){
            console.error(err);
            throw err;
        }
    }
}