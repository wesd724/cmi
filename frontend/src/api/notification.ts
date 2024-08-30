import client from "./axios";

const checkNotification = async (id: number) => {
    await client.delete(`notification/check?id=${id}`); 
}

const checkAllNotification = async (username: string) => {
    await client.delete(`notification/check-all?username=${username}`); 
}

export { checkNotification, checkAllNotification };