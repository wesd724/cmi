import axios from "axios";

interface order {
    username: string;
    currencyId: number;
    amount: number;
    price: number;
}

const buy = async (data: order) => {
    const res = await axios.post("/trade", data);
}

export { buy };