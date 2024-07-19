import { Fragment } from 'react';
import { Box, Button } from "@mui/material";
import { notificationType } from "../type/interface";
import CurrencyExchangeIcon from '@mui/icons-material/CurrencyExchange';
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemIcon from "@mui/material/ListItemIcon";
import Popover from '@mui/material/Popover';
import ListItemButton from '@mui/material/ListItemButton';
import { checkAllNotification, checkNotification } from '../api/notification';
import userStore from '../store/userStore';

interface popperProps {
    id: string | undefined;
    open: boolean;
    anchorEl: HTMLButtonElement | null;
    notification: notificationType[];
    setNotification: React.Dispatch<React.SetStateAction<notificationType[]>>;
    onClose: () => void;
}

const Notification = ({ id, open, anchorEl, notification, setNotification, onClose }: popperProps) => {
    const { username } = userStore();

    const deleteOneNotification = async (id: number) => {
        await checkNotification(id);
        setNotification(notification => notification.filter(n => id !== n.id))
    }

    const deleteAllNotification = async () => {
        await checkAllNotification(username);
        setNotification([]);
    }

    return (
        <Popover
            id={id}
            open={open}
            anchorEl={anchorEl}
            onClose={onClose}
            anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'center',
            }}
        >
            <Box sx={{ border: 1, p: "10px", maxHeight: "300px", maxWidth: "300px", overflow: "auto", bgcolor: 'background.paper' }}>
                <List disablePadding>
                    {
                        notification.map(v => (
                            <Fragment key={v.id}>
                                <ListItem>
                                    <ListItemButton onClick={() => deleteOneNotification(v.id)}>
                                        <ListItemIcon>
                                            <CurrencyExchangeIcon color={v.orders === "BUY" ? "primary" : "success"} />
                                        </ListItemIcon>
                                        <ListItemText
                                            primary={`${v.orders} | ${v.currencyName} ${v.amount}개`}
                                            secondary={<span>{v.completeDate.replace(/T/, ' ')}<br />체결 완료</span>}
                                        />
                                    </ListItemButton>
                                </ListItem>
                                <Divider />
                            </Fragment>
                        ))
                    }
                    <Button sx={{ left: 170, m: "5px", height: "30px" }} onClick={() => deleteAllNotification()} variant="contained">모두 확인</Button>
                </List>
            </Box>
        </Popover>
    )
}

export default Notification;