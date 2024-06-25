import { CircularProgress } from '@mui/material';
const Loading = () => {
    return (
        <CircularProgress
            sx={{ color: "#375d6c", position: "absolute", left: "45%", top: "45%" }}
            size="100px" />
    )
}

export default Loading;