import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", () => {
    const accessToken = ref(localStorage.getItem("accessToken") || null);
    const user = ref(JSON.parse(localStorage.getItem("user")) ?? null);
    // const nom = ref(localStorage.getItem("nom") || null);

    const getToken = computed(() => accessToken.value);
    const getUser = computed(() => user.value);
    // const getNom = computed(() => nom.value);

    function login(u){
        user.value = u;
        accessToken.value = u.accessToken;

        localStorage.setItem("user", JSON.stringify(u));
        localStorage.setItem("accessToken", u.accessToken);
        
    }
    function logout(){
        user.value = null;
        accessToken.value = null;

        localStorage.removeItem("user");
        localStorage.removeItem("accessToken");
    }

    function SignUp(u) {
        accessToken.value = u.accessToken;
        user.value = u;
        // nom.value = userName;

        localStorage.setItem("user", JSON.stringify(u));
        localStorage.setItem("accessToken", u.accessToken);
    }

    return {
        accessToken,
        user,
        // nom,
        getToken,
        getUser,
        // getNom,
        login,
        logout,
        SignUp
    };
});