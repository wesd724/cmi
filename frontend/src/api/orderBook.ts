import client from "./axios"

const getOrderBook = async (id: number) => {
    const res = await client.get(`/order/list?currencyId=${id}`)
    return res.data;
}

export { getOrderBook };