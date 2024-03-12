import axios from "axios"

export const tickers = (markets: string[]) => {
    const str = markets.join(",")
    return axios.get(process.env.REACT_APP_UPBIT_URL + "/ticker", {
        params: {
            markets : str
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
        "codes": [${codes}]
    },
    {
        "format": "DEFAULT"
    }
]   
`