import axios, { AxiosInstance } from "axios";

const client: AxiosInstance = axios.create({
    baseURL: "/api",
})

export default client;