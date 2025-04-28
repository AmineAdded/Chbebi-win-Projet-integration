import axios from "axios"

export default{
    getAllThematics(){
        return axios.get("thematics");
    }
}