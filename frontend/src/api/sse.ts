import axios from "axios"

const closeSSE = async (username: string) => {
    try {
        await axios.delete(`/close?username=${username}`);
    } catch(err) {
        console.log(`closeSSE ERROR: ${err}`);
    }
}

export { closeSSE }