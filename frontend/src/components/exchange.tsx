import { useState, useEffect, useRef, useCallback } from "react";
import { Navigate, useLocation, useParams } from "react-router-dom";
import { getRealOrderBookUnit, webSocketRequest } from "../upbit/api";
import { MARKET } from "../data/constant";
import { Button } from "@mui/material";
import Chart from "./chart";
import { buy, getCashAndCurrency, sell } from "../api/exchange";
import "./css/exchange.css";
import { errorResponse, exchangeStatus, realOrderBookUnitType } from "../type/interface";
import { toKR } from "../lib/api";
import userStore from "../store/userStore";
import axios from "axios";
import OrderBook from "./orderBook";


const Exchange = () => {
    const { username } = userStore();
    const location = useLocation();
    //const [qs] = useSearchParams();
    const { id } = useParams() as { id: string };
    const [price, setPrice] = useState<number>(location.state);
    const [trade, setTrade] = useState(location.state);
    const [amount, setAmount] = useState<number>(0);
    const [order, setOrder] = useState<string>("BUY");
    const [status, setStatus] = useState<exchangeStatus>({
        balance: 0, currencyAmount: 0
    });
    const [orderUnit, setOrderUnit] = useState<Pick<realOrderBookUnitType, "ask_price" | "bid_price">[]>([]);
    const interval = useRef<ReturnType<typeof setInterval> | null>(null);

    const delta = (price < 1000) ? 1 : (price < 10000) ? 10 : (price < 100000) ? 100 : 1000

    const webSocket = useRef<WebSocket | null>(null);
    const market = MARKET[Number(id) - 1];

    //const market = qs.get('market') as string;

    const realOrderUnit = useCallback(async (market: string) => {
        const res = await getRealOrderBookUnit([market]);
        const data = res.data[0].orderbook_units.map((v: realOrderBookUnitType) => (
            {
                ask_price: v.ask_price,
                bid_price: v.bid_price
            }
        ))
        setOrderUnit(data);
    }, []);

    useEffect(() => {
        webSocket.current = new WebSocket(`ws://${window.location.host}/websocket/v1`);
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

    useEffect(() => {
        (async () => {
            const data = await getCashAndCurrency({ username, market });
            setStatus(data);
        })();

        realOrderUnit(market);

        interval.current = setInterval(() => {
            realOrderUnit(market);
        }, 5000);

        return () => {
            if (interval.current) {
                clearInterval(interval.current);
                interval.current = null;
            }
        }
    }, [username, market, realOrderUnit])

    const orders = (type: string) => {
        setOrder(type);
        setAmount(0);
        setTrade(price);
    }

    const ChangeTrade = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.target.value = e.target.value.replace(/,/g, '');
        setTrade(e.target.value ? parseFloat(e.target.value) : 0);
    }

    const ChangeAmount = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAmount(e.target.value ? parseFloat(e.target.value) : 0);
    }

    const ChangeTotal = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.target.value = e.target.value.replace(/,/g, '');
        const value = e.target.value ? parseInt(e.target.value) : 0;
        setAmount(parseFloat((value / trade).toFixed(8)));
    }

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let isOrder = false;

        if (trade * amount === 0) {
            alert("수량과 총액을 입력하세요.");
            return;
        }

        const unit = orderUnit.at(-1)!
        if (trade > unit.ask_price || trade < unit.bid_price) {
            alert("주문 가능 가격 범위 초과");
            return;
        }

        try {
            if (order === "BUY") {
                if (status.balance < Math.round(trade * amount)) {
                    alert("주문 가능 금액 초과");
                    return;
                }

                if (window.confirm("매수하겠습니까?")) {
                    await buy({ username, currencyId: Number(id), amount, price: trade });
                    alert("주문 완료");
                    isOrder = true;
                }
            } else {
                if (status.currencyAmount < amount) {
                    alert("주문 가능 수량 초과");
                    return;
                }

                if (window.confirm("매도하겠습니까?")) {
                    await sell({ username, currencyId: Number(id), amount, price: trade });
                    alert("주문 완료");
                    isOrder = true;
                }
            }
        } catch (error) {
            if (axios.isAxiosError<errorResponse>(error))
                alert(error.response?.data.message);
        }


        if (isOrder) {
            const data = await getCashAndCurrency({ username, market });
            setStatus(data);
        }
    }

    const onClick = (e: React.MouseEvent<HTMLButtonElement>, delta: number) => {
        setTrade((v: number) => v + delta);
    }
    return (
        <div>
            {
                username
                    ? (
                        <>
                            <div className="exchange">
                                <Button sx={{ width: 210, marginBottom: 1 }} onClick={() => orders("BUY")} variant="outlined">매수</Button>
                                <Button sx={{ width: 210, marginBottom: 1 }} onClick={() => orders("SELL")} variant="outlined">매도</Button>
                                <form onSubmit={onSubmit}>

                                    주문가능 &nbsp;&nbsp;&nbsp; {order === "BUY" ? `${toKR(status.balance)} KRW` : `${toKR(status.currencyAmount)} ${market.slice(4)}`} <br />
                                    {order === "BUY" ? "매수" : "매도"} 가격 <input value={toKR(trade)} onChange={ChangeTrade} /><br />
                                    <div className="limit">주문 가능 범위 <span>{toKR(orderUnit.at(-1)?.ask_price)}~{toKR(orderUnit.at(-1)?.bid_price)}</span></div>
                                    <Button sx={{ left: 290, height: 25, color: "red", borderColor: "red" }} onClick={(e) => onClick(e, delta)}>+{delta}</Button>
                                    <Button sx={{ left: 290, height: 25, }} onClick={(e) => onClick(e, -delta)}>-{delta}</Button><br />

                                    주문 수량 <input value={amount} onChange={ChangeAmount} /><br />
                                    주문 총액 <input value={toKR(Math.round(trade * amount))} onChange={ChangeTotal} /><br />
                                    {
                                        order === "BUY"
                                            ? <Button sx={{ width: 420, right: 20, margin: 2, fontSize: 15, backgroundColor: "#C62E2E", "&:hover": { backgroundColor: "#D91656" } }} size="small" type="submit" variant="contained">매수</Button>
                                            : <Button sx={{ width: 420, right: 20, margin: 2, fontSize: 15, backgroundColor: "#0D0CB5", "&:hover": { backgroundColor: "#4C3BCF" } }} size="small" type="submit" variant="contained">매도</Button>

                                    }
                                </form>
                                <div className="range">
                                    <div className="real-orderbook-unit">
                                        <span>{toKR(orderUnit.at(-1)?.ask_price)} ~ {toKR(orderUnit.at(0)?.ask_price)}</span>
                                        <span>{toKR(orderUnit.at(0)?.bid_price)} ~ {toKR(orderUnit.at(-1)?.bid_price)}</span>
                                    </div>
                                    <div>실제 호가</div>
                                </div>
                            </div>
                            <OrderBook id={Number(id)} setTrade={setTrade} />
                            <p className="price">
                                <span>{market} 현재가</span>
                                <span>{toKR(price)}</span>
                            </p>
                            <Chart width="95%" height="400px" time="minutes/1" marketName={market} />
                        </>
                    ) : <Navigate to="/" replace />
            }
        </div>
    )
}

export default Exchange;