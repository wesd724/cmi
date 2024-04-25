import axios from "axios"

const closeSSE = async (username: string) => {
    await axios.delete(`/close?username=${username}`);
}

export { closeSSE }