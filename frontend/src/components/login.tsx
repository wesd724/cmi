import { Button } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/login";
import "./css/login.css";

const Login = () => {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const navigate = useNavigate();

    const ChangeUsername = (e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value);
    const ChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value);

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const data = await login({ username, password });
        if(data) {
            localStorage.setItem("username", username);
            navigate("/");
        }
    }
    return (
        <div className="login">
            <form onSubmit={onSubmit}>
                <input onChange={ChangeUsername} placeholder="username" required /><br />
                <input onChange={ChangePassword} placeholder="password" type="password" required /><br />
                <Button type="submit" color="success" size="small" variant="contained">hello</Button>
            </form>
        </div>
    )
}

export default Login;