import axios from 'axios';

export default {
    getRandomQuote() {
        return axios.get('Quote/random');
    }
}