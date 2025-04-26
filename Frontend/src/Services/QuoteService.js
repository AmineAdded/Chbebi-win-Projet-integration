import axios from 'axios';

export default {
    getRandomQuote() {
        return axios.get('Quote/random');
    },
    getAllQuotes() {
        return axios.get('Quote/getAll');
    },
    deleteQuote(id) {
        return axios.delete(`Quote/delete/${id}`);
    },
    createQuote(quote) {
        return axios.post(`Quote/create`, quote);
    },
    
    updateQuote(id, quote) {
        return axios.put(`Quote/update/${id}`, quote);
    },
    
    deleteAllQuotes() {
        return axios.delete(`Quote/deleteAll`);
    },
    
    getQuoteById(id) {
        return axios.get(`Quote/get/${id}`);
    }
}