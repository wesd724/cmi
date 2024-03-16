import { useCallback, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MARKET, MARKET_NAME } from "../data/constant";
import { response, tickerType } from "../type/interface";
import { tickers, webSocketRequest } from "../upbit/api";
import "./css/main.css";

const Main = () => {
    const [info, setInfo] = useState<tickerType[]>([])
    const webSocket = useRef<WebSocket | null>(null);
    const navigate = useNavigate();

    const ticker = useCallback(async (markets: string[]) => {
        const response = await tickers(markets);
        const data = response.data.map((v: response) => (
            {
                market: v.market,
                trade_price: v.trade_price,
                signed_change_rate: v.signed_change_rate,
                acc_trade_price_24h: v.acc_trade_price_24h
            }
        ))
        setInfo(data)
    }, []);

    useEffect(() => {
        ticker(MARKET);
        webSocket.current = new WebSocket('wss://api.upbit.com/websocket/v1');
        webSocket.current.onopen = () => {
            console.log('WebSocket 연결');
            webSocket.current?.send(webSocketRequest("ticker", MARKET));
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
            setInfo(info => info.map(v =>
                (v.market === data.code) ? {
                    market: data.code,
                    trade_price: data.trade_price,
                    signed_change_rate: data.signed_change_rate,
                    acc_trade_price_24h: data.acc_trade_price_24h
                } : v
            ));
        };

        return () => {
            webSocket.current?.close();
        }
    }, [ticker])

    const onClick = useCallback(() => console.log(1), []);

    return (
        <div>
            <table className="ts">
                <thead>
                    <tr>
                        <th>한글명</th>
                        <th>현재가</th>
                        <th>전일대비</th>
                        <th>거래대금</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {info.map((v, i) =>
                        <tr key={v.signed_change_rate * v.trade_price}>
                            <td onClick={onClick}>{`${MARKET_NAME[i]}(${v.market})`}</td>
                            <td>{v.trade_price.toLocaleString('ko-KR')}</td>
                            <td>{`${(v.signed_change_rate * 100).toFixed(2)}%`}</td>
                            <td>{`${Math.round(v.acc_trade_price_24h / 1000000).toLocaleString('ko-KR')}백만`}</td>
                            <td><button onClick={
                                () => navigate(`/exchange/${i + 1}`, { state: v.trade_price })
                            } style={{ width: "100%" }}>구매</button></td>

                        </tr>
                    )}
                </tbody>

            </table>
            <button onClick={() => ticker(MARKET)}>get ticker</button>

        </div>
    )
}

export default Main;