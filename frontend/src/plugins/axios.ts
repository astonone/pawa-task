import axios, { AxiosInstance } from 'axios';

export const userApi = axios.create({
  baseURL: process.env.VUE_APP_USER_API
});

export const taskApi = axios.create({
  baseURL: process.env.VUE_APP_TASK_API
});

const attachToken = (instance: AxiosInstance) => {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

attachToken(userApi);
attachToken(taskApi);
