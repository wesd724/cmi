import axios from "axios";

const getRecommendation = async () => {
    const res = await axios.get("/recommendation");
    return res.data;
}

export { getRecommendation };