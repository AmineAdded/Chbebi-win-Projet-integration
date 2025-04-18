import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", () => {
    const accessToken = ref(localStorage.getItem("accessToken") || null);
    const user = ref(JSON.parse(localStorage.getItem("user")) ?? null);

    const getToken = computed(() => accessToken.value);
    const getUser = computed(() => user.value);

    function login(u){
        user.value = u;
        accessToken.value = u.accessToken;

        localStorage.setItem("user", JSON.stringify(u));
        localStorage.setItem("accessToken", u.accessToken);
        
    }

    return {
        accessToken,
        user,
        getToken,
        getUser,
        login
    };
});