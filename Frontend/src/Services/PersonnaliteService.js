import Axios from 'axios';
export default{
    async getAllCriteres(id){
        try{
            const response = await Axios.get("Personnalite/get/"+id);
            return response.data;
        }catch(err){
            console.log(err);
            throw err;
        }
    }
}