export interface response {
    market: string;
    [key: string]: string | number;
}


export interface tickerType {
    market: '';
    trade_price: number;
    signed_change_rate: number;
    acc_trade_price_24h: number;
}

export interface candleType {
    timestamp: number;
    opening_price: number;
    high_price: number;
    low_price: number;
    trade_price: number;
}