import { Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { useState, useEffect } from "react";
import { cancelTrade, getTradeHistory } from "../api/trade";
import { toKR } from "../lib/api";
import userStore from "../store/userStore";
import { tradeHistoryType } from "../type/interface";
import "./css/trade.css";
import Loading from "./loading";

const Trade = () => {
    const [trades, setTrades] = useState<tradeHistoryType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const username = userStore(state => state.username);
    
    useEffect(() => {
        (async () => {
            const data = await getTradeHistory(username);
            setLoading(false);
            setTrades(data);
        })();
    }, [username])

    const cancel = async (id: number) => {
        if (window.confirm("주문 취소하시겠습니까?")) {
            const res = await cancelTrade(id);
            if (res) {
                setTrades(trades => trades.filter(v => v.id !== id))
                return;
            }
            alert("이미 처리된 주문입니다.");
        }
    }
    return (
        <>
            {
                loading ?
                    <Loading /> :
                    <TableContainer sx={{ height: 800 }}>
                        <Table className="trade" size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>코인</TableCell>
                                    <TableCell>주문</TableCell>
                                    <TableCell>거래수량</TableCell>
                                    <TableCell>거래단가</TableCell>
                                    <TableCell>거래금액</TableCell>
                                    <TableCell>주문시간</TableCell>
                                    <TableCell>체결시간</TableCell>
                                    <TableCell align="center">미체결</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {trades.map(v => (
                                    <TableRow key={v.id}>
                                        <TableCell>{v.currencyName}</TableCell>
                                        <TableCell>{v.order}</TableCell>
                                        <TableCell>{v.amount}</TableCell>
                                        <TableCell>{toKR(v.price) + " KRW"}</TableCell>
                                        <TableCell>{toKR(v.tradePrice) + " KRW"}</TableCell>
                                        <TableCell>{v.orderDate.replace(/T/, ' ')}</TableCell>
                                        <TableCell>{v.completeDate?.replace(/T/, ' ')}</TableCell>
                                        <TableCell>
                                            {
                                                v.complete
                                                    ? <Button size="small" disabled>취소</Button>
                                                    : <Button
                                                        size="large"
                                                        sx={{ color: "red" }}
                                                        onClick={() => cancel(v.id)}
                                                    >취소</Button>
                                            }
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
            }
        </>
    )
}

export default Trade;