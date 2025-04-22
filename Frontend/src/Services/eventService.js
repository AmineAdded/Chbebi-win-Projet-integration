import axios from 'axios';

const API_URL = 'http://localhost:9090/api/WorkshopEvents';

export const getAllEvents = () => {
    return axios.get(`${API_URL}/getAll`);
};
export const deleteExpiredEvents = () => {
    return axios.delete(`${API_URL}/deleteExpired`);
};  
