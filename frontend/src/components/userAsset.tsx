import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { useState, useEffect, useRef } from 'react';
import { getUserAsset } from '../api/userAsset';
import { toKR } from '../lib/api';
import { currencyTicker, userAssetType } from '../type/interface';
import { webSocketRequest } from "../upbit/api";
import "./css/userAsset.css";

const UserAsset = () => {
    const [asset, setAsset] = useState<userAssetType>();
    const [currencyPrice, setCurrecyPrice] = useState<currencyTicker[]>([]);
    const username = localStorage.getItem("username") as string;
    const webSocket = useRef<WebSocket | null>(null);

    useEffect(() => {
        console.log(1)
        const market = asset?.currencyAssetResponseList.map(v => v.market) as string[];
        if (market) {
            setCurrecyPrice(market.map(v => ({ market: v, trade_price: 0 })));
            webSocket.current = new WebSocket(process.env.REACT_APP_WS_UPBIT_URL as string);
            webSocket.current.onopen = () => {
                console.log('WebSocket 연결');
                webSocket.current?.send(webSocketRequest("ticker", market));
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
                setCurrecyPrice(currencyPrice => currencyPrice.map(v =>
                    (v.market === data.code) ?
                        {
                            market: data.code,
                            trade_price: data.trade_price
                        } : v
                ))
            };
        }

        return () => {
            webSocket.current?.close();
        }
    }, [asset]);

    useEffect(() => {
        (async () => {
            const data = await getUserAsset(username);
            setAsset(data);
        })();

    }, [username]);

    return (
        <>
            보유자산: <h1>{asset?.balance}</h1>
            <TableContainer>
            <Table className="userAsset" size="small">
                <TableHead>
                    <TableRow>
                        <TableCell>보유자산</TableCell>
                        <TableCell>보유수량</TableCell>
                        <TableCell>매수평균가</TableCell>
                        <TableCell>매수금액</TableCell>
                        <TableCell>평가금액</TableCell>
                        <TableCell>평가손익(%)</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {asset?.currencyAssetResponseList.map(v => (
                        <TableRow key={v.amount * v.averageCurrencyBuyPrice}>
                            <TableCell>{v.market}</TableCell>
                            <TableCell>{toKR(v.amount)}</TableCell>
                            <TableCell>{toKR(v.averageCurrencyBuyPrice)}</TableCell>
                            <TableCell>{toKR(v.buyPrice) + " KRW"}</TableCell>
                            <TableCell>{0}</TableCell>
                            <TableCell>{0}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
        </>
    )
}

export default UserAsset;