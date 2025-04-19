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
        async signUp(nom,email,password,confirmPassword){
        try{
            const store = useUserStore();
            const response = await Axios.post('Utilisateur/register', {
                nom: nom,
                email: email,
                password: password,
                confirmPassword: confirmPassword
            });
            const accessToken  = response.data.accessToken;

            store.SignUp({nom:nom,email:email,accessToken:accessToken});
            return response.data;
        }
        catch (error) {
            console.error(error.response.data.message);
            throw error;
        }
    }
};