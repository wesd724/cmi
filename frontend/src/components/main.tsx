import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import Button from "@mui/material/Button";
import { useCallback, useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MARKET, MARKET_NAME } from "../data/constant";
import { response, tickerType } from "../type/interface";
import { getTickers, webSocketRequest } from "../upbit/api";
import Chart from "./chart";
import "./css/main.css";

const Main = () => {
    const [market, setMarket] = useState<string>("KRW-BTC");
    const [tickers, setTickers] = useState<tickerType[]>([])
    const webSocket = useRef<WebSocket | null>(null);
    const navigate = useNavigate();
    const username = localStorage.getItem("username");

    const ticker = useCallback(async (markets: string[]) => {
        const res = await getTickers(markets);
        const data = res.data.map((v: response) => (
            {
                market: v.market,
                trade_price: v.trade_price,
                signed_change_rate: v.signed_change_rate,
                acc_trade_price_24h: v.acc_trade_price_24h
            }
        ))
        setTickers(data)
    }, []);

    useEffect(() => {
        ticker(MARKET);
        webSocket.current = new WebSocket(process.env.REACT_APP_WS_UPBIT_URL as string);
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
            setTickers(ticker => ticker.map(v =>
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

    const onClick = (market: string) => setMarket(market);

    const exchange = (i: number, price: number) => {
        if (username)
            navigate(`/exchange/${i + 1}`, { state: price })
        else
            alert("로그인이 필요합니다.");
    }

    return (
        <>
            <TableContainer sx={{ height: "43vh" }}>
                <Table className="ticker">
                    <TableHead>
                        <TableRow>
                            <TableCell>한글명</TableCell>
                            <TableCell>현재가</TableCell>
                            <TableCell>전일대비</TableCell>
                            <TableCell>거래대금</TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {tickers.map((v, i) => (
                            <TableRow key={v.signed_change_rate * v.trade_price}>
                                <TableCell onClick={() => onClick(v.market)}>{`${MARKET_NAME[i]}(${v.market})`}</TableCell>
                                <TableCell>{v.trade_price.toLocaleString('ko-KR')}</TableCell>
                                <TableCell>{`${(v.signed_change_rate * 100).toFixed(2)}%`}</TableCell>
                                <TableCell>{`${Math.round(v.acc_trade_price_24h / 1000000).toLocaleString('ko-KR')}백만`}</TableCell>
                                <TableCell><Button variant="outlined" onClick={() => exchange(i, v.trade_price)} style={{ left: "2vw", width: "70%" }}>구매</Button></TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Chart time="days" marketName={market} />
        </>

    )
}

export default Main;