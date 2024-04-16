import { memo, useEffect, useRef } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';

const Nav = () => {
    const navigate = useNavigate();
    const username = localStorage.getItem("username");
    const eventSource = useRef<EventSource | null>(null);

    useEffect(() => {
        if (username) {
            eventSource.current = new EventSource(process.env.REACT_APP_SSE_URL + username);

            eventSource.current.addEventListener('connect', e => {
                const data = e.data;
                console.log(data)
            });

            eventSource.current.addEventListener('msg', e => {
                const data = e.data;
                console.log(JSON.parse(data))
            });

            eventSource.current.addEventListener('error', e => {
                console.log(e)
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
        localStorage.clear();
        navigate("/", { replace: true });
        eventSource.current?.close();
    }

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" color="info">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        CMI
                    </Typography>
                    <Button color="inherit" onClick={() => navigate("/")}>메인</Button>
                    <Button color="inherit" onClick={() => navigate("/activity")}>활동</Button>
                    {
                        username
                            ? (
                                <>
                                    <Button color="inherit" onClick={() => navigate("/investment")}>내 투자</Button>
                                    <Button color="inherit" onClick={logout}>로그아웃</Button>
                                </>
                            )
                            : <Button color="inherit" onClick={() => navigate("/login")}>로그인</Button>
                    }
                </Toolbar>
            </AppBar>
        </Box>
    );
}

export default memo(Nav);