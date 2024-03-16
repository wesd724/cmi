import { useState, useEffect, useRef } from "react";
import { useLocation, useParams } from "react-router-dom";
import { webSocketRequest } from "../upbit/api";
import { MARKET } from "../data/constant";
import { Button } from "@mui/material";

const Exchange = () => {
    const location = useLocation();
    //const [qs] = useSearchParams();
    const { id } = useParams() as { id: string };
    const [price, setPrice] = useState<number>(location.state);
    const [trade, setTrade] = useState(location.state);
    const webSocket = useRef<WebSocket | null>(null);
    const market = MARKET[Number(id) - 1];
    //const market = qs.get('market') as string;

    useEffect(() => {
        webSocket.current = new WebSocket(process.env.REACT_APP_WS_UPBIT_URL as string);
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

    
    const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTrade(Number(e.target.value));
    }

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log(1);
    }

    const onClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        setTrade((v: number) => v + 1);
    }
    return (
        <div>
            <div>
                {market} 현재가: {price}
            </div>
            <form onSubmit={onSubmit}>
                구매 가격: <input value={trade} onChange={onChange} />
                <Button size="small" type="submit" variant="contained">매수</Button>
                <Button size="small" onClick={onClick} variant="outlined">+1</Button>
            </form>
            
        </div>
    )
}

export default Exchange;