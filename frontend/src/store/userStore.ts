import { create } from 'zustand';

interface userStoreState {
    username: string;
    setName: (name: string) => void;
    deleteName: () => void;
}

const userStore = create<userStoreState>((set) => ({
    username: localStorage.getItem("username") ?? "",
    setName: (name: string) => {
        localStorage.setItem("username", name);
        set({ username: name });
    },
    deleteName: () => {
        localStorage.clear();
        set({ username: "" });
    }
}))

export default userStore;