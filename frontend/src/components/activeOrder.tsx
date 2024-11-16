import { useEffect, useState } from "react";
import { cancelOrder, getActiveOrder } from "../api/orderBook";
import { activeOrderType, errorResponse } from "../type/interface";
import userStore from "../store/userStore";
import Loading from "./loading";
import { Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { toKR } from "../lib/api";
import axios from "axios";
import "./css/activeOrder.css";

const ActiveOrder = () => {
    const [orderList, setOrderList] = useState<activeOrderType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const username = userStore(state => state.username);

    useEffect(() => {
        (async () => {
            const data: activeOrderType[] = await getActiveOrder(username);
            setLoading(false);
            setOrderList(data);
        })();
    }, [username]);

    const cancel = async (id: number) => {
        if (window.confirm("주문 취소하시겠습니까?")) {
            try {
                await cancelOrder(id);
                setOrderList(o => o.filter(v => v.id !== id))
            } catch (error) {
                if (axios.isAxiosError<errorResponse>(error)) {
                    alert(error.response?.data.message)
                }
            }
        }
    }

    return (
        <>
            {
                loading ?
                    <Loading /> :
                    <TableContainer sx={{ height: 800 }}>
                        <Table className="active" size="small">
                            <TableHead>
                                <TableRow>
                                    <TableCell>주문시간</TableCell>
                                    <TableCell>코인</TableCell>
                                    <TableCell>주문</TableCell>
                                    <TableCell>주문가격</TableCell>
                                    <TableCell>주문수량</TableCell>
                                    <TableCell>미체결량</TableCell>
                                    <TableCell align="center">주문취소</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {orderList.map(v => (
                                    <TableRow key={v.id}>
                                        <TableCell>{v.createdDate.replace(/T/, ' ')}</TableCell>
                                        <TableCell>{v.currencyName}</TableCell>
                                        <TableCell sx={{ color: v.orders == "SELL" ? "#0D0CB5" : "#C62E2E" }}>{v.orders}</TableCell>
                                        <TableCell>{toKR(v.price) + " KRW"}</TableCell>
                                        <TableCell>{`${toKR(v.originalAmount)} ${v.market.replace("KRW-", "")}`}</TableCell>
                                        <TableCell>{`${toKR(v.activeAmount)} ${v.market.replace("KRW-", "")}`}</TableCell>
                                        <TableCell align="center"> <Button
                                            size="large"
                                            sx={{ color: "red" }}
                                            onClick={() => cancel(v.id)}
                                        >취소</Button>
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

export default ActiveOrder;