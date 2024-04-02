import axios from "axios";

export interface tradeHistory {
    currencyName: string;
    order: string;
    amount: number;
    price: number;
    tradePrice: number;
    orderDate: string;
    completeDate: string;
}

const getTradeHistory = async (username: string) => {
    const res = await axios.get(`/trade?username=${username}`)
    return res.data
}

export { getTradeHistory };