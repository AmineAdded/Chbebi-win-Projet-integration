import axios from "axios";

export default {
  getAllEvents() {
    return axios.get("WorkshopEvents/getAll");
  },

  getEventById(id) {
    return axios.get(`WorkshopEvents/get/${id}`);
  },

  createEvent(eventData) {
    return axios.post("WorkshopEvents/create", eventData);
  },

  updateEvent(id, eventData) {
    return axios.put(`WorkshopEvents/update/${id}`, eventData);
  },

  deleteEvent(id) {
    return axios.delete(`WorkshopEvents/delete/${id}`);
  },

  deleteAllEvents() {
    return axios.delete("WorkshopEvents/deleteAll");
  },

  deleteExpiredEvents() {
    return axios.delete("WorkshopEvents/deleteExpired");
  },
};
