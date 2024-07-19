import client from "./axios";

const getTradeHistory = async (username: string) => {
    const res = await client.get(`/trade?username=${username}`);
    return res.data
}

const cancelTrade = async(id: number) => {
    const res = await client.delete(`/trade/cancel?id=${id}`);
    return res.data
}

export { getTradeHistory, cancelTrade };