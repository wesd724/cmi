import client from "./axios";

const getRecommendation = async () => {
    const res = await client.get("/recommendation");
    return res.data;
}

export { getRecommendation };