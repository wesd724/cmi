import client from "./axios";

const getUserAsset = async (username: string) => {
    const res = await client.get(`/asset?username=${username}`);
    return res.data;
}

export { getUserAsset };