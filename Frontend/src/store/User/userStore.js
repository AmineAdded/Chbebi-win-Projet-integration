import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", () => {
    const accessToken = ref(localStorage.getItem("accessToken") || null);
    const role = ref(localStorage.getItem("role") || null);
    const nom = ref(localStorage.getItem("nom") || null);
    const user = ref(JSON.parse(localStorage.getItem("user")) ?? null);
    

    const getToken = computed(() => accessToken.value);
    const getRole = computed(() => role.value);
    const getUser = computed(() => user.value);
    const getNom = computed(() => nom.value);

    function login(u){
        user.value = u;
        accessToken.value = u.accessToken;
        role.value = u.role;
        nom.value = u.nom;

        localStorage.setItem("user", JSON.stringify(u));
        localStorage.setItem("accessToken", u.accessToken);
        localStorage.setItem("role", u.role);
        localStorage.setItem("nom", u.nom);
        
    }
    function logout(){
        user.value = null;
        accessToken.value = null;
        role.value = null;
        nom.value = null;

        localStorage.removeItem("user");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("role");
        localStorage.removeItem("nom");
    }

    function SignUp(u) {
        accessToken.value = u.accessToken;
        user.value = u;
        role.value = u.role;
        nom.value = u.nom;

        localStorage.setItem("user", JSON.stringify(u));
        localStorage.setItem("accessToken", u.accessToken);
        localStorage.setItem("role", u.role);
        localStorage.setItem("nom", u.nom);
    }
    function udpate(name){
        nom.value = name;
    }
    function setPersonnalite(type){
        user.value.personnalite_id = type;
        localStorage.setItem("user", JSON.stringify(user.value));
    }

    return {
        accessToken,
        user,
        role,
        nom,
        getToken,
        getUser,
        getRole,
        getNom,
        login,
        logout,
        SignUp,
        udpate,
        setPersonnalite
    };
});