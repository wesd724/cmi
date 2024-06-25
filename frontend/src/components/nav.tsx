import { memo, useEffect, useRef, useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';
import { closeSSE } from '../api/sse';
import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone';
import { IconButton, Badge } from '@mui/material';
import Notification from './notification';
import { notificationType } from '../type/interface';
import PersonIcon from '@mui/icons-material/Person';
import userStore from '../store/userStore';

const Nav = () => {
    const navigate = useNavigate();
    const { username, deleteName } = userStore();
    const eventSource = useRef<EventSource | null>(null);

    const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null);
    const [notification, setNotification] = useState<notificationType[]>([]);

    useEffect(() => {
        if (username) {
            eventSource.current = new EventSource(process.env.REACT_APP_SSE_URL + username);

            eventSource.current.addEventListener('connect', e => {
                const data = e.data;
                console.log(data);
            });

            eventSource.current.addEventListener('message', e => {
                const data: notificationType[] = JSON.parse(e.data);
                setNotification(data);
            });

            eventSource.current.addEventListener('error', e => {
                console.log(e);
            });

            window.addEventListener("beforeunload", e => {
                e.preventDefault();
            })
        }

        return () => {
            eventSource.current?.close();
        }
    }, [username])

    const logout = () => {
        closeSSE(username);
        deleteName();
        navigate("/", { replace: true });
        setNotification([]);
        eventSource.current?.close();
    }

    const openNotification = (e: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(e.currentTarget);
    }

    const closeNotification = () => {
        setAnchorEl(null);
    };

    const open = Boolean(notification.length) && Boolean(anchorEl);
    const id = open ? 'simple-popover' : undefined;

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" color="info">
                <Toolbar>
                    <Button color="inherit" onClick={() => navigate("/")}>메인</Button>
                    <Button color="inherit" onClick={() => navigate("/activity")}>활동</Button>
                    <IconButton color="inherit" onClick={openNotification}>
                        <Badge badgeContent={notification.length} color="success">
                            <NotificationsNoneIcon />
                        </Badge>
                    </IconButton>
                    <Notification id={id} open={open} anchorEl={anchorEl} notification={notification} onClose={closeNotification} />
                    <Typography variant="h6" component="div" sx={{ textAlign: "center", flexGrow: 1 }}>
                        모의 투자
                    </Typography>
                    {
                        username
                            ? (
                                <>
                                    <PersonIcon sx={{ marginBottom: "4px", color: "#130f86" }} />
                                    <div style={{
                                        fontSize: 18,
                                        fontWeight: "bold",
                                        paddingRight: 30,
                                        paddingLeft: 3,
                                        height: 26,
                                    }}>{username}
                                    </div>
                                    <Button color="inherit" onClick={() => navigate("/investment")}>내 투자</Button>
                                    <Button color="inherit" onClick={logout}>로그아웃</Button>
                                </>
                            )
                            : (
                                <>
                                    <Button color="inherit" onClick={() => navigate("/login")}>로그인</Button>
                                    <Button color="inherit" onClick={() => navigate("/signup")}>회원가입</Button>
                                </>
                            )


                    }
                </Toolbar>
            </AppBar>
        </Box>
    );
}

export default memo(Nav);