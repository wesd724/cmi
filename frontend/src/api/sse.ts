import client from "./axios";

const closeSSE = async (username: string) => {
    try {
        await client.delete(`sse/close?username=${username}`);
    } catch(err) {
        console.log(`closeSSE ERROR: ${err}`);
    }
}

export { closeSSE }