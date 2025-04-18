import Axios from 'axios';
import {useUserStore} from '@/store/User/userStore';

export default{
    async login(email, password){
        try{
            const store = useUserStore();
            const response = await Axios.post('Utilisateur/login', {
                email: email,
                password: password
            });
            const { id, nom, email: mail, accessToken, role } = response.data;
            const filteredUser = { id, nom, email: mail, accessToken, role };

            store.login(filteredUser);
            
            return response.data;
        }
        catch (error) {
            console.error(error);
            throw error;
        }
            
        },
        async signUp(nom,email,password){
        try{
            const store = useUserStore();
            const response = await Axios.post('Utilisateur/signup', {
                nom: nom,
                email: email,
                password: password,
            });
            const { email: mail, accessToken } = response.data;
            const filteredUser = { nom, email: mail, accessToken };

            store.SignUp(filteredUser);
            
            return response.data;
        }
        catch (error) {
            console.error(error);
            throw error;
        }
    }
};