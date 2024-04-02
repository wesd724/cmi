import { memo } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';

const Nav = () => {
    const navigate = useNavigate();
    const username = localStorage.getItem("username");

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" color="info">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        CMI
                    </Typography>
                    <Button color="inherit" onClick={() => navigate("/")}>메인</Button>
                    {
                        username
                            ? (
                                <>
                                    <Button color="inherit" onClick={() => navigate("/asset")}>내 자산</Button>
                                    <Button color="inherit" onClick={() => {
                                        localStorage.clear();
                                        navigate("/", {replace: true});
                                    }}>로그아웃</Button>
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