import { Fragment } from 'react';
import { Box } from "@mui/material";
import { notificationType } from "../type/interface";
import CurrencyExchangeIcon from '@mui/icons-material/CurrencyExchange';
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemIcon from "@mui/material/ListItemIcon";
import Popover from '@mui/material/Popover';

interface popperType {
    id: string | undefined;
    open: boolean;
    anchorEl: HTMLButtonElement | null;
    notification: notificationType[]
    onClose: () => void;
}

const Notification = ({ id, open, anchorEl, notification, onClose }: popperType) => {
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
                                    <ListItemIcon>
                                        <CurrencyExchangeIcon color={v.orders === "BUY" ? "primary" : "success"} />
                                    </ListItemIcon>
                                    <ListItemText
                                        primary={`${v.orders} | ${v.currencyName} ${v.amount}개`}
                                        secondary={`${v.completeDate.replace(/T/, ' ')} 체결 완료`}
                                    />
                                </ListItem>
                                <Divider />
                            </Fragment>
                        ))
                    }
                </List>
            </Box>
        </Popover>
    )
}

export default Notification;