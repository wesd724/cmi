import axios from "axios"

export const getTickers = (markets: string[]) => {
    const str = markets.join(",")
    return axios.get(import.meta.env.VITE_UPBIT_URL + "/ticker", {
        params: {
            markets : str
        }
    })
}

export const getCandles = (market: string, time: string, count: number) => {
    return axios.get(import.meta.env.VITE_UPBIT_URL + `/candles/${time}`, {
        params: {
            market, count
        }
    })
}

export const webSocketRequest = (type: string, codes: string[]) => `
[
    {
        "ticket": "user"
    },
    {
        "type": ${type},
        "codes": [${codes}],
        "isOnlyRealtime": true
    },
    {
        "format": "DEFAULT"
    }
]   
`