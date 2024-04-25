import axios from "axios";

interface order {
    username: string;
    currencyId: number;
    amount: number;
    price: number;
}

interface info {
    username: string,
    market: string
}

const getCashAndCurrency = async (data: info) => {
    const res = await axios.get("/user/cash-currency", {
        params: data
    });
    return res.data
}

const buy = async (data: order) => {
    await axios.post("/trade/buy", data);
}

const sell = async (data: order) => {
    await axios.post("/trade/sell", data);
}

export { buy, sell, getCashAndCurrency };