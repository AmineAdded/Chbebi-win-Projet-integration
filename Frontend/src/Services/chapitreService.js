import axios from 'axios';

// Define the base URL for API calls
const API_URL = 'http://localhost:9090/api';

export default {
  /**
   * Get all chapters by thematic ID
   * @param {Number} thematicId - The ID of the thematic
   * @returns {Promise} Promise object representing the chapters
   */
  getChaptersByThematic(thematicId) {
    return axios.get(`${API_URL}/chapitres/get/${thematicId}`)
      .then(response => response.data) // تأكد من بنية البيانات من الخادم
      .catch(error => {
        console.error('Error fetching chapters:', error);
        throw error;
      });
  },

  /**
   * Create a new chapter
   * @param {Object} chapter - The chapter data
   * @returns {Promise} Promise object representing the created chapter
   */
  createChapter(chapter) {
    console.log('Creating chapter:', chapter); 
    return axios.post(`${API_URL}/chapitres/create`, chapter)
      .then(response => response.data) 
      .catch(error => {
        console.error('Error creating chapter:', error);
        throw error;
      });
  },

  /**
   * Update an existing chapter
   * @param {Number} id - The ID of the chapter to update
   * @param {Object} chapter - The updated chapter data
   * @returns {Promise} Promise object representing the updated chapter
   */
  updateChapter(id, chapter) {
    return axios.put(`${API_URL}/chapitres/update/${id}`, chapter)
      .then(response => response.data)
      .catch(error => {
        console.error('Error updating chapter:', error);
        throw error;
      });
  },

  /**
   * Delete a chapter
   * @param {Number} id - The ID of the chapter to delete
   * @returns {Promise} Promise object representing the result of the deletion
   */
  deleteChapter(id) {
    return axios.delete(`${API_URL}/chapitres/delete/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error deleting chapter:', error);
        throw error;
      });
  },

  /**
   * Get progress for a chapter
   * @param {Number} id - The ID of the chapter
   * @returns {Promise} Promise object representing the progress
   */
  getProgress(data) {
    return axios.post("chapitres/getProgress",data)
      .then(response => response.data)
      .catch(error => {
        console.error('Error fetching chapter progress:', error);
        throw error;
      });
  }
};