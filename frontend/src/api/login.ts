import client from "./axios";

interface user {
    username: string,
    password: string,
}

const login = async (data: user) => {
    const res = await client.post("/user/login", data);
    return res.data
}

const signUp = async (data: user) => {
    const res = await client.post("/user/sign-up", data);
    return res.data
}

export { login, signUp }