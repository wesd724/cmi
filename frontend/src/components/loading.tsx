import { CircularProgress } from '@mui/material';
const Loading = () => {
    return (
        <CircularProgress
            sx={{ color: "#375d6c", position: "absolute", left: "50vw", top: "50vh" }}
            size="100px" />
    )
}

export default Loading;