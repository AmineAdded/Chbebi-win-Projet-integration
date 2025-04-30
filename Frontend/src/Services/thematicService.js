import axios from 'axios';

// Define the base URL for API calls
const API_URL = 'http://localhost:9090/api';

export default {
  /**
   * Get all thematics
   * @returns {Promise} Promise object representing the thematics
   */
  getAllThematics() {
    return axios.get(`${API_URL}/thematics`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error fetching thematics:', error);
        throw error;
      });
  },

  /**
   * Get a thematic by ID
   * @param {Number} id - The ID of the thematic
   * @returns {Promise} Promise object representing the thematic
   */
  getThematicById(id) {
    return axios.get(`${API_URL}/thematics/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error fetching thematic:', error);
        throw error;
      });
  },

  /**
   * Create a new thematic
   * @param {Object} thematic - The thematic data
   * @returns {Promise} Promise object representing the created thematic
   */
  createThematic(thematic) {
    return axios.post(`${API_URL}/thematics`, thematic)
      .then(response => response.data)
      .catch(error => {
        console.error('Error creating thematic:', error);
        throw error;
      });
  },

  /**
   * Update an existing thematic
   * @param {Number} id - The ID of the thematic to update
   * @param {Object} thematic - The updated thematic data
   * @returns {Promise} Promise object representing the updated thematic
   */
  updateThematic(id, thematic) {
    return axios.put(`${API_URL}/thematics/${id}`, thematic)
      .then(response => response.data)
      .catch(error => {
        console.error('Error updating thematic:', error);
        throw error;
      });
  },

  /**
   * Delete a thematic
   * @param {Number} id - The ID of the thematic to delete
   * @returns {Promise} Promise indicating deletion success
   */
  deleteThematic(id) {
    return axios.delete(`${API_URL}/thematics/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error deleting thematic:', error);
        throw error;
      });
  }
};