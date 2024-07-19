import client from "./axios";

const checkNotification = async (id: number) => {
    await client.delete(`/check?id=${id}`); 
}

const checkAllNotification = async (username: string) => {
    await client.delete(`/check-all?username=${username}`); 
}

export { checkNotification, checkAllNotification };