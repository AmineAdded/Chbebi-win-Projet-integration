// src/services/statisticsService.js
import axios from "axios";

export const fetchStatistics = async (period = "week") => {
  const response = await axios.get(`http://localhost:9090/api/statistics?period=${period}`);
  return response.data;
};
