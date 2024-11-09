import { useState, useEffect, useRef, useCallback, Fragment } from "react";
import { Navigate, useLocation, useParams } from "react-router-dom";
import { getRealOrderBookUnit, webSocketRequest } from "../upbit/api";
import { MARKET, MARKET_MAPPER } from "../data/constant";
import { Button } from "@mui/material";
import Chart from "./chart";
import { buy, getCashAndCurrency, sell } from "../api/exchange";
import "./css/exchange.css";
import { errorResponse, exchangeStatus, InputFocusType, realOrderBookUnitType } from "../type/interface";
import { checkNaN, toKR } from "../lib/api";
import userStore from "../store/userStore";
import axios from "axios";
import OrderBook from "./orderBook";

const Exchange = () => {
    const { username } = userStore();
    const location = useLocation();

    type ExplicitAny = typeof location.state; // no-explicit-any 오류를 우회하기 위한 타입
    //const [qs] = useSearchParams();
    const { id } = useParams() as { id: string };
    const [price, setPrice] = useState(location.state);
    const [trade, setTrade] = useState(toKR(location.state));
    const [amount, setAmount] = useState<ExplicitAny>(0);
    const [order, setOrder] = useState<string>("BUY");
    const [status, setStatus] = useState<exchangeStatus>({
        balance: 0, currencyAmount: 0
    });
    const [orderUnit, setOrderUnit] = useState<Pick<realOrderBookUnitType, "ask_price" | "bid_price">[]>([]);
    const interval = useRef<ReturnType<typeof setInterval> | null>(null);
    const [selectRatio, setSelectRatio] = useState<number>(0);
    const [inputFocus, setInputFocus] = useState<InputFocusType>({
        trade: false,
        amount: false,
        total: false,
    });

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
        setTrade(toKR(price));
        setSelectRatio(0);
    }

    const ChangeTrade = (e: React.ChangeEvent<HTMLInputElement>) => {
        setTrade(e.target.value);
    }

    const ChangeAmount = (e: React.ChangeEvent<HTMLInputElement>) => {
        setAmount(e.target.value);
        setSelectRatio(0);
    }

    const ChangeTotal = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = checkNaN(parseInt(e.target.value.replace(/,/g, '')));
        const tradePrice = checkNaN(parseFloat(trade.toString().replace(/,/g, '')));

        setAmount(checkNaN(parseFloat((value / tradePrice).toFixed(8))));
    }

    const onFocus = (e: React.FocusEvent<HTMLInputElement>) => {
        setInputFocus({ ...inputFocus, [e.target.name]: true });
        e.target.value = e.target.value.replace(/,/g, '');
        if (e.target.name == "trade")
            setTrade(e.target.value);
        else if (e.target.name == "amount")
            setAmount(e.target.value);
    };

    const onBlur = (e: React.FocusEvent<HTMLInputElement>) => {
        setInputFocus({ ...inputFocus, [e.target.name]: false });
        const value = checkNaN(parseFloat(e.target.value));
        if (e.target.name == "trade")
            setTrade(toKR(value));
        else if (e.target.name == "amount")
            setAmount(toKR(value));
    };

    const total = () => {
        const t = trade.toString().replace(/,/g, '');
        const a = amount.toString().replace(/,/g, '');
        const result = Math.round(checkNaN(parseFloat(t)) * checkNaN(parseFloat(a)));
        return inputFocus.total == false ? toKR(result) : result;
    }

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        let isOrder = false;

        const tempTrade = parseFloat(trade.toString().replace(/,/g, ''));
        const tempAmount = parseFloat(amount.toString().replace(/,/g, ''));
        //console.log(tempTrade, tempAmount, status)

        if (tempTrade * tempAmount === 0) {
            alert("수량과 총액을 입력하세요.");
            return;
        }

        const unit = orderUnit.at(-1)!
        if (tempTrade > unit.ask_price || tempTrade < unit.bid_price) {
            alert("주문 가능 가격 범위 초과");
            return;
        }

        try {
            if (order === "BUY") {
                if (status.balance < Math.round(tempTrade * tempAmount)) {
                    alert("주문 가능 금액 초과");
                    return;
                }

                if (window.confirm("매수하겠습니까?")) {
                    await buy({ username, currencyId: Number(id), amount: tempAmount, price: tempTrade });
                    alert("주문 완료");
                    isOrder = true;
                }
            } else {
                if (status.currencyAmount < tempAmount) {
                    alert("주문 가능 수량 초과");
                    return;
                }

                if (window.confirm("매도하겠습니까?")) {
                    await sell({ username, currencyId: Number(id), amount: tempAmount, price: tempTrade });
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

    const onClick = (e: React.MouseEvent<HTMLButtonElement>, ratio: number) => {
        if (order == "BUY") {
            const tempTrade = parseFloat(trade.toString().replace(/,/g, ''));
            const orderStatus = status.balance * (ratio / 100);
            setAmount(toKR(orderStatus / tempTrade));

        } else {
            setAmount(toKR(status.currencyAmount * (ratio / 100)));
        }

        setSelectRatio(ratio);
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
                                    {order === "BUY" ? "매수" : "매도"} 가격 <input name="trade" onFocus={onFocus} onBlur={onBlur} value={trade} onChange={ChangeTrade} /><br />
                                    <div className="limit">주문 가능 범위 <span>{toKR(orderUnit.at(-1)?.ask_price)}~{toKR(orderUnit.at(-1)?.bid_price)}</span></div>

                                    주문 수량 <input name="amount" onFocus={onFocus} onBlur={onBlur} value={amount} onChange={ChangeAmount} /><br />
                                    {[10, 25, 50, 100].map((v, i) => (
                                        <Fragment key={i + 1}>
                                            <Button
                                                variant={selectRatio == v ? "contained" : "outlined"}
                                                onClick={(e) => onClick(e, v)}
                                                color="secondary"
                                                sx={{
                                                    left: 80,
                                                    height: 25,
                                                    ml: "10px",
                                                    width: 70,
                                                    color: "black",
                                                    borderColor: "rgb(184, 176, 176)",
                                                    fontSize: "12px",
                                                }}>{v}%</Button>
                                        </Fragment>
                                    ))}<br />
                                    주문 총액 <input name="total" onFocus={onFocus} onBlur={onBlur} value={total()} onChange={ChangeTotal} /><br />
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
                                <span>{MARKET_MAPPER[market]}({market}) 현재가</span>
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