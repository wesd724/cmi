import axios from "axios";

const getUserAsset = async (username: string) => {
    const res = await axios(`/asset?username=${username}`);
    return res.data;
}

export { getUserAsset };