import Axios from 'axios';

export default{
    async addFedback(data){
        console.log("Data",data);
        try{
            const response = await Axios.post('Feedback/create',data);
            return response.data;
        }
        catch (error) {
            console.error(error);
            throw error;
        }
    },
    async getAllFeedback(){
        try{
            const response = await Axios.get('Feedback/getAll');
            return response.data;
        }
        catch(err){
            console.error(err);
            throw err;
        }
    },
       
}