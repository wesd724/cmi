import axios from "axios"

interface comment {
    username: string;
    currencyId: number;
    content: string;
}

const saveComment = async (data: comment) => {
    const res = await axios.post("/comment", data);
    return res.data;
}

const getComments = async (market: string) => {
    const res = await axios.get(`/comment?market=${market}`);
    return res.data;
}

const updateComment = async (id: number, content: string) => {
    await axios.put("/comment", { id, content });
}

const deleteComment = async (id: number) => {
    await axios.delete(`/comment?id=${id}`)
}

export { saveComment, getComments, updateComment, deleteComment };