import React, { Fragment, useState, useEffect, useRef } from 'react';
import TextField from '@mui/material/TextField';
import "./css/comment.css";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import { MARKET } from '../data/constant';
import { getComments, saveComment } from '../api/comment';
import Loading from "./loading";
import userStore from '../store/userStore';

interface marketProps {
    market: string;
}

interface commentProps {
    id: number;
    username: string;
    content: string;
}

const RenderTextList = ({ commentList }: { commentList: commentProps[] | undefined }) => {
    return (
        <>
            {commentList?.map((v) => (
                <Fragment key={v.id}>
                    <ListItem>
                        <ListItemText
                            primary={`${v.content}`}
                            secondary={`${v.username}`}
                        />
                    </ListItem>
                    <Divider />
                </Fragment>
            ))}
        </>
    )
}

// const makeFakeData = (count: number) => {
//     return Array.from({ length: count }).map((_, i) => ({
//         id: i + 1,
//         username: String.fromCharCode(i + 65),
//         content: String.fromCharCode(...Array.from({ length: 3 }).map((_, k) => i + k + 65))
//     }));
// }

const Comment = ({ market }: marketProps) => {
    const [content, setContent] = useState<string>("");
    const [commentsData, setCommentsData] = useState<commentProps[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const element = useRef<HTMLDivElement>(null);

    const { username } = userStore();
    const currencyId: number = MARKET.indexOf(market) + 1;

    useEffect(() => {
        (async () => {
            const data = await getComments(market);
            setLoading(false);
            setCommentsData(data);
        })();
    }, [market])

    useEffect(() => {
        if (element.current)
            element.current.scrollTop = element.current.scrollHeight;
    }, [commentsData])

    const changeContent = (e: React.ChangeEvent<HTMLInputElement>) => setContent(e.target.value);

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (content) {
            const id = await saveComment({ username, currencyId, content });
            setCommentsData(prev => [...prev, { id, username: "test", content }]);
            setContent("");
        }
    }

    return (
        <div className="comment-container">
            <div className="comment-list" ref={element}>
                {
                    loading ?
                        <Loading /> :
                        <List>
                            <RenderTextList commentList={commentsData} />
                        </List>
                }
            </div>
            <form onSubmit={onSubmit} className="form">
                <TextField
                    sx={{
                        position: "absolute",
                        left: "2px",
                        bottom: "0px",
                        width: "70vw",
                    }}
                    value={content}
                    onChange={changeContent}
                    disabled={username ? false: true}
                    label="의견" size="small" variant="filled" />
            </form>

        </div>
    )
}

export default Comment;
