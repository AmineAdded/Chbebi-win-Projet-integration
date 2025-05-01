import Axios from "axios";
import axios from "axios";
import { useUserStore } from "@/store/User/userStore";

export default {
  async login(email, password) {
    try {
      const store = useUserStore();
      const response = await Axios.post("Utilisateur/login", {
        email: email,
        password: password,
      });
      const {
        id,
        nom,
        email: mail,
        accessToken,
        role,
        personnalite_id,
      } = response.data;
      const filteredUser = {
        id,
        nom,
        email: mail,
        accessToken,
        role,
        personnalite_id,
      };

      localStorage.setItem("email", mail);
      store.login(filteredUser);
      

      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },
  async logSuperAdmin(email, password) {
    try {
      const response = await axios.post("Utilisateur/login", {
        email: email,
        password: password,
      });

      return response.data;
    } catch (error) {
      let errorMessage = "حدث خطأ أثناء التحقق";
      if (error.response?.data?.message) {
        const serverMessage = error.response.data.message.toLowerCase();
        if (serverMessage.includes("password")) {
          errorMessage = "كلمة المرور غير صحيحة";
        }
      }
      throw new Error(errorMessage);
    }
  },
  async signUp(nom, email, password, confirmPassword) {
    try {
      const store = useUserStore();
      const response = await Axios.post("Utilisateur/register", {
        nom: nom,
        email: email,
        password: password,
        confirmPassword: confirmPassword,
      });
      const accessToken = response.data.accessToken;
      const role = response.data.role;
      const id = response.data.id;
      const personnalite_id = response.data.personnalite_id || null;

      store.SignUp({
        id: id,
        nom: nom,
        email: email,
        accessToken: accessToken,
        role: role,
        personnalite_id: personnalite_id,
      });
      return response.data;
    } catch (error) {
      console.error(error.response.data.message);
      throw error;
    }
  },
  updateUserProfile(data) {
    const store = useUserStore();
    store.udpate(data.nom);
    const userLocal = localStorage.getItem("user");
    const parsedUser = JSON.parse(userLocal);
    const userId = parsedUser.id;
    console.log("Data envoyée :", data);
    return axios.put(`/Utilisateur/update-profile/${userId}`, data);
  },
  async getAllUsers() {
    return axios.get("Utilisateur/getAll");
  },
  deleteUser(id) {
    return axios.delete(`Utilisateur/delete/${id}`);
  },
  updateUser(id, data) {
    return axios.put(`/Utilisateur/update-profile/${id}`, data);
  }
};
