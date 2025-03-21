import { UseMenuButtonParameters } from "@mui/base";
import { status, statusType } from "../data/constant";

export interface errorResponse {
    message: string;
}

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

export interface InputFocusType {
    trade: boolean;
    amount: boolean;
    total: boolean;
}

export interface tradeHistoryType {
    id: number;
    currencyName: string;
    order: string;
    amount: number;
    price: number;
    tradePrice: number;
    orderDate: string;
    completeDate: string;
    status: statusType;
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

export interface notificationType {
    id: number;
    currencyName: string;
    orders: string;
    amount: number;
    price: number;
    status: statusType;
    completeDate: string
}

export interface recommendationType {
    market: string;
    comparedPreviousDay: number;
}

export interface orderBookType {
    price: number;
    orders: string;
    activeAmount: number;
}

export interface realOrderBookUnitType {
    ask_price: number;
    bid_price: number;
    ask_size: number;
    bid_size: number;
}

export interface activeOrderType {
    id: number;
    market: string;
    currencyName: string;
    orders: string;
    originalAmount: number;
    activeAmount: number;
    price: number;
    createdDate: string;
}
