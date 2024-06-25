import { Button } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signUp } from "../api/login";
import userStore from "../store/userStore";
import "./css/signup.css";

const Signup = () => {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [checkPassword, setCheckPassword] = useState<string>("");
    const navigate = useNavigate();

    const { setName } = userStore();

    const ChangeUsername = (e: React.ChangeEvent<HTMLInputElement>) => setUsername(e.target.value);
    const ChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value);
    const ChangeCheckPassword = (e: React.ChangeEvent<HTMLInputElement>) => setCheckPassword(e.target.value);

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (password !== checkPassword) {
            alert("비밀번호가 다릅니다.");
            return;
        }
            
        const data = await signUp({ username, password });

        if (data) {
            setName(username);
            navigate("/");
            return;
        }
        alert("아이디가 이미 존재합니다.");
    }
    return (
        <div className="signup">
            <form onSubmit={onSubmit}>
                <input value={username} onChange={ChangeUsername} placeholder="username" required /><br />
                <input value={password} onChange={ChangePassword} placeholder="password" type="password" required /><br />
                <input value={checkPassword} onChange={ChangeCheckPassword} placeholder="check password" type="password" required /><br />
                <Button type="submit" color="success" size="small" variant="contained">회원가입</Button>
            </form>
        </div>
    )
}

export default Signup;