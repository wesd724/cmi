import client from "./axios";

const getTradeHistory = async (username: string) => {
    const res = await client.get(`/trade?username=${username}`);
    return res.data
}

export { getTradeHistory };