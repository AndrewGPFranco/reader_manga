import axios from "axios";

const username = "andrew";
const password = "mypassword";

const token = btoa(`${username}:${password}`);

export const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        Authorization: `Basic ${token}`
    }
});