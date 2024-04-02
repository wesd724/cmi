import { useState, useEffect, useRef } from "react";
import { Navigate, useLocation, useParams } from "react-router-dom";
import { webSocketRequest } from "../upbit/api";
import { MARKET } from "../data/constant";
import { Button } from "@mui/material";
import Chart from "./chart";
import { buy } from "../api/exchange";
import "./css/exchange.css";

const Exchange = () => {
    const username = localStorage.getItem("username") as string;
    const location = useLocation();
    //const [qs] = useSearchParams();
    const { id } = useParams() as { id: string };
    const [price, setPrice] = useState<number>(location.state);
    const [trade, setTrade] = useState(location.state);
    const [amount, setAmount] = useState<number>(0);

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


    const ChangeTrade = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTrade(e.target.value ? parseInt(e.target.value) : 0);
    }

    const ChangeAmount = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAmount(e.target.value ? parseFloat(e.target.value) : 0);
    }

    const ChangeTotal = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value ? parseInt(e.target.value) : 0;
        setAmount(parseFloat((value / trade).toFixed(8)));
    }

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (window.confirm("매수하겠습니까?")) {
            buy({ username, currencyId: Number(id), amount, price: trade })
        }
    }

    const onClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        setTrade((v: number) => v + 1000);
    }
    return (
        <div>
            {
                username
                    ? (
                        <>
                            <div className="exchange">
                                <p>{market} 현재가: {price}</p>
                                <form onSubmit={onSubmit}>
                                    매수 가격: <input value={trade} onChange={ChangeTrade} /><br />
                                    주문 수량: <input value={amount} onChange={ChangeAmount} /><br />
                                    주문 총액: <input value={Math.round(trade * amount)} onChange={ChangeTotal} /><br />
                                    <Button sx={{ bottom: 90, left: 240, margin: "5px" }} size="small" type="submit" variant="contained">매수</Button>
                                    <Button sx={{ bottom: 55, left: 166, margin: "5px", backgroundColor: "green" }} size="small" variant="contained">매도</Button>
                                    <Button sx={{ left: -70, margin: "5px" }} size="small" onClick={onClick} variant="outlined">+1000</Button>
                                </form>
                            </div>
                            <Chart time="minutes/1" marketName={market} />
                        </>
                    ) : <Navigate to="/" replace />
            }
        </div>
    )
}

export default Exchange;