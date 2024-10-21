import { create } from 'zustand';

interface userStoreState {
    username: string;
    setName: (name: string) => void;
    deleteName: () => void;
}

const userStore = create<userStoreState>((set) => ({
    username: sessionStorage.getItem("username") ?? "",
    setName: (name: string) => {
        sessionStorage.setItem("username", name);
        set({ username: name });
    },
    deleteName: () => {
        sessionStorage.clear();
        set({ username: "" });
    }
}))

export default userStore;