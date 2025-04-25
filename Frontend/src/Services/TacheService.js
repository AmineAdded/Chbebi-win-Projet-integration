import Axios from 'axios';

export default {
  async getTachesByUserandDate(id, date) {
    try {
      const response = await Axios.post(`Tache/get/${id}`, `"${date}"`, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': '*/*'
        }
      });
      return response.data;
    } catch (err) {
      console.error(err);
      throw err;
    }
  },

  async updateTache(id,done){
    try{
        const res = await Axios.put(`Tache/put/${id}`,{
            id:id,
            done: Number(done)
        });
        return res.data;
    }
    catch(err){
      console.error(err);
      throw err;
    }
  },
  async addTache(nomTache, idUser,date) {
    try{
        const res = await Axios.post("Tache/create",{
            nomTache:nomTache,
            userId:idUser,
            date:date
        });
        return res.data;
    }
    catch(err){
        console.error(err);
        throw err;
    }
  }
};
