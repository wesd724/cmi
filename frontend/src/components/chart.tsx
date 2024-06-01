import ApexChart from 'react-apexcharts';
import React, { useState, useEffect, useRef, useMemo } from 'react';
import { response } from '../type/interface';
import { getCandles } from '../upbit/api';

export interface candleProps {
    time: string;
    marketName: string;
    width: string;
    height: string;
}

const intervalTime = (time: string) => time === "days" ? 1000 * 60 * 60 : 1000 * 10;

const formatBytime = (time: string, timestamp: string) => {
    const d = new Date(timestamp);
    if(time === "days") {
        const month = d.getMonth() + 1;
        const date = d.getDate();
        return `${d.getFullYear()}/${month < 10 ? "0" + month : month}/${date < 10 ? "0" + date : date}`
    }
    const m = d.getMinutes();
    return `${d.getHours()}:${m < 10 ? "0" + m : m}`;
}

const Chart = ({ width, height, time, marketName }: candleProps) => {
    const [candles, setCandles] = useState<number[][]>([]);
    const interval = useRef<ReturnType<typeof setInterval> | null>(null);

    const candle = useMemo(() => async (market: string, time: string, count: number) => {
        const res = await getCandles(market, time, count);
        const data = res.data.map((v: response) => (
            [
                v.timestamp,
                v.opening_price,
                v.high_price,
                v.low_price,
                v.trade_price
            ]
        ))
        setCandles(data);
    }, []);

    useEffect(() => {
        candle(marketName, time, 200);
        interval.current = setInterval(() => {
            candle(marketName, time, 200);
        }, intervalTime(time))

        return () => {
            if (interval.current) {
                clearInterval(interval.current);
                interval.current = null;
            }
        }
    }, [candle, marketName, time]);

    // const endInterval = () => {
    //     if (interval.current) {
    //         console.log("END!", interval.current);
    //         clearInterval(interval.current);
    //         interval.current = null;
    //     }
    // }

    return (
        <div>
            <ApexChart
                type="candlestick"
                height={height}
                width={width}
                series={[{
                    data: candles.reverse()
                }]}
                options={{
                    chart: {
                        type: "candlestick",
                        toolbar: {
                            show: false,
                        },
                        zoom: {
                            enabled: false
                        },
                        animations: {
                            enabled: true
                        }
                    },
                    tooltip: {
                        enabled: true
                    },
                    stroke: { curve: "smooth", width: 2 },
                    xaxis: {
                        type: 'datetime',
                        labels: {
                            datetimeUTC: false,
                            formatter: (timestamp) => formatBytime(time, timestamp),
                        }
                    },
                    plotOptions: {
                        candlestick: {
                            colors: {
                                upward: '#DF7D46',
                                downward: '#3C90EB'
                            }
                        }
                    },
                }}
            />
        </div>
    )
}

export default React.memo(Chart);