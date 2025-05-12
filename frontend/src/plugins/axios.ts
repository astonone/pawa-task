import axios, { AxiosInstance } from 'axios'

export const userApi = axios.create({
    baseURL: 'http://localhost:8081/api'
})

export const taskApi = axios.create({
    baseURL: 'http://localhost:8082/api'
})

const attachToken = (instance: AxiosInstance) => {
    instance.interceptors.request.use(config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    })
}

attachToken(userApi)
attachToken(taskApi)