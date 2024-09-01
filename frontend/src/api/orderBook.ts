import client from "./axios"

const getOrderBook = async (id: number) => {
    const res = await client.get(`/order/list?currencyId=${id}`);
    return res.data;
}

const getActiveOrder = async (username: string) => {
    const res = await client.get(`/order/active?username=${username}`);
    return res.data;
}

const cancelOrder = async (id: number) => {
    await client.delete(`/order?id=${id}`);
}

export { getOrderBook, getActiveOrder, cancelOrder };