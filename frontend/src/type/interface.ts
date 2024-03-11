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