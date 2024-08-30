import { create } from 'zustand';

interface useSseState {
    eventSource: EventSource | null;
    setEventSource: (url: string) => void;
    closeEventSource: () => void;
    addEventListener: (eventName: string, callback: (event: MessageEvent) => void) => void,
    removeEventListener: (eventName: string, callback: (event: MessageEvent) => void) => void
}

const useSSE = create<useSseState>((set, get) => ({
    eventSource: null,
    setEventSource: (url: string) => set({
        eventSource: new EventSource(url)
    }),
    closeEventSource: () => {
        const { eventSource } = get();
        eventSource?.close();
    },
    addEventListener: (eventName: string, callback: (event: MessageEvent) => void) => {
        const { eventSource } = get();
        eventSource?.addEventListener(eventName, callback);
    },
    removeEventListener: (eventName: string, callback: (event: MessageEvent) => void) => {
        const { eventSource } = get();
        eventSource?.removeEventListener(eventName, callback);
    }
}))

export default useSSE;