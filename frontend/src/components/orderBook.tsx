import { useEffect } from "react";
import useSSE from "../store/useSSE";

const OrderBook = ({ id }: { id: number }) => {
    const { closeEventSource, addEventListener, removeEventListener } = useSSE();
    useEffect(() => {
        const eventName = `orderBook ${id}`;

        const callback = (e: MessageEvent) => {
            const data = JSON.parse(e.data);
            console.log(data);
        }

        addEventListener(eventName, callback);

        return () => {
            removeEventListener(eventName, callback);
        }
    }, [id, closeEventSource, addEventListener, removeEventListener])

    return (
        <div>currencyID: {id}</div>
    )
}

export default OrderBook;
