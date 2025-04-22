import axios from 'axios';

export default {
    getRandomQuote() {
        return axios.get(`http://localhost:9090/api/Quote/random`);
    }
}