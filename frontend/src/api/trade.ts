import axios from "axios";

const getTradeHistory = async (username: string) => {
    const res = await axios.get(`/trade?username=${username}`);
    return res.data
}

const cancelTrade = async(id: number) => {
    const res = await axios.delete(`/trade/cancel?id=${id}`);
    return res.data
}

export { getTradeHistory, cancelTrade };