import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { useState, useEffect } from "react";
import { getTradeHistory, tradeHistory } from "../api/trade";
import "./css/trade.css";

const Trade = () => {
    const [trades, setTrades] = useState<tradeHistory[]>([]);
    useEffect(() => {
        const get = async () => {
            const data = await getTradeHistory(localStorage.getItem("username") as string);
            setTrades(data);
        }
        get();
    }, [])

    return (
        <TableContainer sx={{height: 800 }}>
            <Table className="trade" size="small">
                <TableHead>
                    <TableRow sx={{ backgroundColor: "#f3f3f3" }}>
                        <TableCell>코인</TableCell>
                        <TableCell>주문</TableCell>
                        <TableCell>거래수량</TableCell>
                        <TableCell>거래단가</TableCell>
                        <TableCell>거래금액</TableCell>
                        <TableCell>주문시간</TableCell>
                        <TableCell>체결시간</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {trades.map((v) => (
                        <TableRow key={v.orderDate}>
                            <TableCell>{v.currencyName}</TableCell>
                            <TableCell>{v.order}</TableCell>
                            <TableCell>{v.amount}</TableCell>
                            <TableCell>{v.price.toLocaleString('ko-KR') + " KRW"}</TableCell>
                            <TableCell>{v.tradePrice.toLocaleString('ko-KR') + " KRW"}</TableCell>
                            <TableCell>{v.orderDate.replace(/T/, ' ')}</TableCell>
                            <TableCell>{v.completeDate?.replace(/T/, ' ')}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

export default Trade;