export const MARKET = [
    "KRW-BTC",
    "KRW-ETH",
    "KRW-SOL",
    "KRW-XRP",
    "KRW-BCH",
    "KRW-AAVE",
    "KRW-ETC",
    "KRW-NEO",
    "KRW-XLM",
    "KRW-STX",
    "KRW-DOGE",
    "KRW-BTG",
    "KRW-ARK",
    "KRW-LINK",
    "KRW-AVAX",
    "KRW-QTUM",
    "KRW-ZETA",
    "KRW-SUI",
    "KRW-MASK",
    "KRW-NEAR",
]
export const MARKET_NAME = [
    "비트코인",
    "이더리움",
    "솔라나",
    "리플",
    "비트코인캐시",
    "에이브",
    "이더리움클래식",
    "네오",
    "스텔라루멘",
    "스택스",
    "도지코인",
    "비트코인골드",
    "아크",
    "체인링크",
    "아발란체",
    "퀀텀",
    "제타체인",
    "수이",
    "마스크네트워크",
    "니어프로토콜",
]

export const MARKET_MAPPER = Object.fromEntries(
    MARKET.map((v, i) => [v, MARKET_NAME[i]])
)

// different way
// MARKET
//     .map((v, i) => ({ [v]: MARKET_NAME[i] }))
//     .reduce((acc, cur) => ({ ...acc, ...cur }), {});

// MARKET
//     .reduce<{ [key: string]: string }>((acc, cur, i) => {
//         acc[cur] = MARKET_NAME[i]
//         return acc;
//     }, {})

export const ASSET_UPDATE_COLOR = "#0152b7";

export const status = {
    ACTIVE: "미체결",
    COMPLETE: "체결 완료",
    PARTIAL: "부분 체결"
};

export type statusType = keyof typeof status;