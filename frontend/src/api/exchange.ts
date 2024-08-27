import client from "./axios";

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
    const res = await client.get("/asset/cash-currency", {
        params: data
    });
    return res.data
}

const buy = async (data: order) => {
    await client.post("/order/buy", data);
}

const sell = async (data: order) => {
    await client.post("/order/sell", data);
}

export { buy, sell, getCashAndCurrency };