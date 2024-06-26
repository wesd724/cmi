import axios from "axios";

const checkNotification = async (id: number) => {
    await axios.delete(`/check?id=${id}`); 
}

const checkAllNotification = async (username: string) => {
    await axios.delete(`/check-all?username=${username}`); 
}

export { checkNotification, checkAllNotification };