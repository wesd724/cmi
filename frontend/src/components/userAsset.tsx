import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import { PieChart } from '@mui/x-charts';
import { useState, useEffect, useRef, useCallback } from 'react';
import { getUserAsset } from '../api/userAsset';
import { ASSET_UPDATE_COLOR } from '../data/constant';
import { toKR } from '../lib/api';
import userStore from '../store/userStore';
import { currencyAssetType, pieChartType, response, userAssetType, valuationType } from '../type/interface';
import { getTickers, webSocketRequest } from "../upbit/api";
import "./css/userAsset.css";
import Loading from './loading';

const UserAsset = () => {
    const [asset, setAsset] = useState<userAssetType>({ balance: 0, currencyAssetResponseList: [] });
    const [valuation, setValuation] = useState<valuationType[]>([]);
    const [totalPrice, setTotalPrice] = useState<number>(0);
    const [totalValuation, setTotalValuation] = useState<number>(0);
    const [pieChartData, setPieChartData] = useState<pieChartType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const { username } = userStore();
    const webSocket = useRef<WebSocket | null>(null);

    const ticker = useCallback(async (markets: string[], currencyAssets: currencyAssetType[]) => {
        const res = await getTickers(markets);
        const data: valuationType[] = currencyAssets.map((v, i) => (
            {
                market: v.market,
                price: Math.round(v.amount * res.data[i].trade_price)
            }
        ))

        const updateTotalValuation = data.reduce((a, b) => a + b.price, 0);

        setValuation(data);

        setTotalValuation(updateTotalValuation);

        setPieChartData(data.map((v, i) => (
            {
                id: i,
                value: v.price / updateTotalValuation * 100,
                label: `${v.market.replace("KRW-", "")}`
            }
        )));

    }, []);

    const updateValuationAndTotalValuation = (valuation: valuationType[], data: response, currencyAssets: currencyAssetType[]) => {
        const updateValuation = valuation.map((v, i) =>
            (v.market === data.code) ?
                {
                    market: data.code,
                    price: Math.round(currencyAssets[i].amount * (data.trade_price as number))
                } : v
        );

        const updateTotalValuation = updateValuation.reduce((a, b) => a + b.price, 0);

        return { updateValuation, updateTotalValuation };
    }

    useEffect(() => {
        const market = asset.currencyAssetResponseList.map(v => v.market) as string[];

        if (market.length) {
            ticker(market, asset.currencyAssetResponseList);
            setLoading(false);
            webSocket.current = new WebSocket(import.meta.env.VITE_WS_UPBIT_URL as string);
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

                setValuation(valuation => {
                    const { updateValuation, updateTotalValuation }
                        = updateValuationAndTotalValuation(valuation, data, asset.currencyAssetResponseList);

                    setTotalValuation(updateTotalValuation);

                    setPieChartData(updateValuation.map((v, i) => (
                        {
                            id: i,
                            value: v.price / updateTotalValuation * 100,
                            label: `${v.market.replace("KRW-", "")}`
                        }
                    )));

                    return updateValuation;
                });
            };
        }

        return () => {
            webSocket.current?.close();
        }
    }, [asset, ticker]);


    useEffect(() => {
        (async () => {
            const data: userAssetType = await getUserAsset(username);
            setAsset(data);
            if (!data.currencyAssetResponseList.length) setLoading(false);
            setTotalPrice(data.currencyAssetResponseList.reduce((a, b) => a + b.buyPrice, 0));
        })();
    }, [username]);

    return (
        <>
            {
                loading ?
                    <Loading /> :
                    <div className="container">
                        <div className="userAsset">
                            <div className="tradeState">
                                <dl>
                                    <dt>보유 KRW</dt>
                                    <dd>{toKR(asset.balance)}<span>KRW</span></dd>
                                </dl>
                                <dl>
                                    <dt >총 보유자산</dt>
                                    <dd>{toKR((asset.balance) + totalValuation)}<span>KRW</span></dd>
                                </dl>
                                <dl>
                                    <dt>총 매수</dt>
                                    <dd>{toKR(totalPrice)}<span>KRW</span></dd>
                                </dl>
                                <dl>
                                    <dt>총 평가손익</dt>
                                    <dd>{toKR(totalValuation - totalPrice)}<span>KRW</span></dd>
                                </dl>
                                <dl>
                                    <dt>총 평가</dt>
                                    <dd>{toKR(totalValuation)}<span>KRW</span></dd>
                                </dl>
                                <dl>
                                    <dt>총 평가수익률(%)</dt>
                                    <dd>{((totalValuation - totalPrice) / totalPrice * 100).toFixed(2)}<span>%</span></dd>
                                </dl>
                            </div>
                            <div className="chart">
                                <div style={{ marginLeft: 20, marginBottom: 10 }}>보유 비중(%)</div>
                                <PieChart
                                    series={[
                                        {
                                            data: pieChartData,
                                            innerRadius: 36,
                                            cx: pieChartData.length > 6 ? 110 : 170,
                                        },
                                    ]}
                                    width={400}
                                    height={200}
                                />
                            </div>
                        </div>
                        <TableContainer>
                            <Table className="currencyAsset" size="small">
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
                                    {asset.currencyAssetResponseList.map((v, i) => (
                                        <TableRow key={i + 1}>
                                            <TableCell>{v.currencyName}</TableCell>
                                            <TableCell>{toKR(v.amount)}<span>{v.market.replace("KRW-", "")}</span></TableCell>
                                            <TableCell>{toKR(v.averageCurrencyBuyPrice)}<span>KRW</span></TableCell>
                                            <TableCell>{toKR(v.buyPrice)}<span>KRW</span></TableCell>
                                            <TableCell sx={{ color: ASSET_UPDATE_COLOR }}>{toKR(valuation[i]?.price ?? 0)}<span>KRW</span></TableCell>
                                            <TableCell sx={{ color: ASSET_UPDATE_COLOR }}>{valuation[i]?.price ? ((valuation[i].price - v.buyPrice) / v.buyPrice * 100).toFixed(2) : 0}<span>%</span></TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
            }
        </>
    )
}

export default UserAsset;