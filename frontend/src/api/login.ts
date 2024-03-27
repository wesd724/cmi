import axios from "axios";

interface user {
    username: string,
    password: string,
}

const login = async (data: user) => {
    const res = await axios.post("/user/login", data);
    return res.data
}

export { login }