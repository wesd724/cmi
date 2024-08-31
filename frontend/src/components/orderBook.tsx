import { useEffect, useState, useRef } from "react";
import { TableBody } from "@mui/material";
import Table from "@mui/material/Table";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import { getOrderBook } from "../api/orderBook";
import useSSE from "../store/useSSE";
import { orderBookType } from "../type/interface";
import "./css/orderBook.css";
import { toKR } from "../lib/api";

interface orderBookProps {
    id: number;
    setTrade: React.Dispatch<React.SetStateAction<number>>;
}

const OrderBook = ({ id, setTrade }: orderBookProps) => {
    const { closeEventSource, addEventListener, removeEventListener } = useSSE();
    const [orderList, setOrderList] = useState<orderBookType[]>([]);
    const [maxAmount, setMaxAmount] = useState<number>(0);
    const element = useRef<HTMLTableElement>(null);

    useEffect(() => {
        (async () => {
            const data: orderBookType[] = await getOrderBook(id);
            setMaxAmount(Math.max(1, ...data.map(v => v.activeAmount)))
            setOrderList(data);
        })();

        const eventName = `orderBook ${id}`;

        const callback = (e: MessageEvent) => {
            const data: orderBookType[] = JSON.parse(e.data);
            setMaxAmount(Math.max(1, ...data.map(v => v.activeAmount)))
            setOrderList(data);
            console.log("GET");
        }

        addEventListener(eventName, callback);

        return () => {
            removeEventListener(eventName, callback);
        }
    }, [id, closeEventSource, addEventListener, removeEventListener])

    useEffect(() => {
        console.log("MOVE");
        if (element.current) {
            let index;
            index = orderList.findLastIndex(v => v.orders === "SELL")
            if (index === -1) {
                index = orderList.findIndex(v => v.orders === "BUY")
            }
            element.current.scrollTop = (27 * (index - 2));
        }
    }, [orderList]);

    return (
        <div>
            <TableContainer className="orderBook-container" sx={{ width: "280px" }}>
                <Table className="orderBook">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center" colSpan={2}>호가</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableHead>
                        <TableRow>
                            <TableCell align="center">가격</TableCell>
                            <TableCell align="center">수량</TableCell>
                        </TableRow>
                    </TableHead>
                </Table>
                <TableContainer ref={element} className="orderBook-body" sx={{ height: 250 }}>
                    <Table className="orderBook">
                        <TableBody>
                            {orderList.map((v, i) => {
                                const backgroundColor = v.orders == "SELL" ? "#4880ee4d" : "#e152414d";
                                const amountStyle = {
                                    width: `${128 * (v.activeAmount / maxAmount)}px`,
                                    backgroundColor: v.orders == "SELL" ? "#0000ff3d" : "#ff00003d"
                                }
                                return (
                                    <TableRow key={i + 1}>
                                        <TableCell sx={{ backgroundColor }}
                                            onClick={() => setTrade(v.price)}
                                            align="center">
                                            {toKR(v.price)}
                                        </TableCell>
                                        <TableCell sx={{ backgroundColor }} align="center">
                                            <div style={{ position: "relative" }}>
                                                <div
                                                    style={amountStyle}
                                                    className="amount"></div>
                                                {toKR(v.activeAmount)}
                                            </div>
                                        </TableCell>
                                    </TableRow>
                                )
                            })}
                        </TableBody>
                    </Table>
                </TableContainer>
            </TableContainer>
        </div >
    )
}

export default OrderBook;
