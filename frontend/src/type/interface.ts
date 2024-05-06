export interface response {
    market: string;
    [key: string]: string | number;
}

export interface tickerType {
    market: string;
    trade_price: number;
    signed_change_rate: number;
    acc_trade_price_24h: number;
}

export interface exchangeStatus {
    balance: number;
    currencyAmount: number;
}

export interface candleType {
    time: string;
    marketName: string;
    width: string;
    height: string;
}

export interface currencyAssetType {
    market: string;
    currencyName: string;
    amount: number;
    averageCurrencyBuyPrice: number;
    buyPrice: number;
}

export interface userAssetType {
    balance: number;
    currencyAssetResponseList: currencyAssetType[];
}

export interface valuationType {
    market: string;
    price: number;
}

export interface pieChartType {
    id: number;
    value: number;
    label: string;
}