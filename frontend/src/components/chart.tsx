import ApexChart from 'react-apexcharts';
import React, { useState, useEffect, useRef, useMemo } from 'react';
import { response, candleType } from '../type/interface';
import { getCandles } from '../upbit/api';

const Chart = ({ marketName } : { marketName: string }) => {
    const [candles, setCandles] = useState<candleType[]>([]);
    const interval = useRef<ReturnType<typeof setInterval> | null>(null);

    const candle = useMemo(() => async (market: string, time: string, count: number) => {
        const res = await getCandles(market, time, count);
        const data = res.data.map((v: response) => (
            {
                timestamp: v.timestamp,
                opening_price: v.opening_price,
                high_price: v.high_price,
                low_price: v.low_price,
                trade_price: v.trade_price,
            }
        ))
        setCandles(data);
    }, []);

    useEffect(() => {
        candle(marketName, "days", 200);
        interval.current = setInterval(() => {
            console.log(1);
        }, 10000)

        return () => {
            if (interval.current) {
                clearInterval(interval.current);
                interval.current = null;
            }
        }
    }, [candle, marketName]);

    const endInterval = () => {
        if (interval.current) {
            console.log("END!", interval.current);
            clearInterval(interval.current);
            interval.current = null;
        }
    }

    return (
        <div>
            <ApexChart
                type="candlestick"
                height="500px"
                series={[{
                    data: candles.map(v => [
                        v.timestamp,
                        v.opening_price,
                        v.high_price,
                        v.low_price,
                        v.trade_price,
                    ]).reverse()
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
                    stroke: { curve: "smooth", width: 2 },
                    xaxis: {
                        type: 'datetime'
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