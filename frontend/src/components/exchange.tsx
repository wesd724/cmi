import { useState, useEffect, useRef } from "react";
import { useLocation, useSearchParams } from "react-router-dom";
import { webSocketRequest } from "../upbit/api";

const Exchange = () => {
    const location = useLocation();
    const [qs] = useSearchParams();
    const [price, setPrice] = useState<number>(location.state);
    const [trade, setTrade] = useState(location.state);
    const webSocket = useRef<WebSocket | null>(null);
    const market = qs.get('market') as string;
    const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTrade(e.target.value);
    }
    useEffect(() => {
        webSocket.current = new WebSocket('wss://api.upbit.com/websocket/v1');
        webSocket.current.onopen = () => {
            console.log('WebSocket 연결');
            webSocket.current?.send(webSocketRequest("ticker", [market]));
        };
        webSocket.current.onclose = () => {
            console.log('WebSocket 종료');
        }
        webSocket.current.onerror = (e: Event) => {
            console.log(e);
        }
        webSocket.current.onmessage = async (event: MessageEvent) => {
            const text = await event.data.text();
            const data = JSON.parse(text);
            setPrice(data.trade_price);
        };

        return () => {
            webSocket.current?.close();
        }
    }, [market])
    return (
        <div>
            <div>
                {market}: {price}
            </div>
            구매: <input value={trade} onChange={onChange} />
        </div>
    )
}

export default Exchange;