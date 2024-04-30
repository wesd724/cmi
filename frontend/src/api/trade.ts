import axios from "axios";

export interface tradeHistory {
    id: number;
    currencyName: string;
    order: string;
    amount: number;
    price: number;
    tradePrice: number;
    orderDate: string;
    completeDate: string;
    complete: boolean;
}

const getTradeHistory = async (username: string) => {
    const res = await axios.get(`/trade?username=${username}`);
    return res.data
}

const cancelTrade = async(id: number) => {
    const res = await axios.delete(`/trade/cancel?id=${id}`);
    return res.data
}

export { getTradeHistory, cancelTrade };