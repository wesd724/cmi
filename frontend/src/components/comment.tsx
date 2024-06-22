import React, { Fragment, useState, useEffect, useRef } from 'react';
import TextField from '@mui/material/TextField';
import "./css/comment.css";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';

interface marketProps {
    market: string;
}

interface commentProps {
    id: number;
    username: string;
    comment: string;

}

const RenderTextList = ({ commentList }: { commentList: commentProps[] | undefined }) => {
    return (
        <>
            {commentList?.map((v) => (
                <Fragment key={v.id}>
                    <ListItem>
                        <ListItemText
                            primary={`${v.comment}`}
                            secondary={`${v.username}`}
                        />
                    </ListItem>
                    <Divider />
                </Fragment>
            ))}
        </>
    )
}

const makeFakeData = (count: number)  => {
    return Array.from({length: count}).map((_, i) => ({
        id: i + 1,
        username: String.fromCharCode(i + 65),
        comment: String.fromCharCode(...Array.from({ length: 3 }).map((_, k) => i + k + 65))
    }));
}

const Comment = ({ market }: marketProps) => {
    const [text, setText] = useState<string>("");
    const [commentsData, setCommentsData] = useState<commentProps[]>([]);
    const element = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const data: commentProps[]  = makeFakeData(10);
        setCommentsData(data);
    }, [])

    useEffect(() => {
        if(element.current)
            element.current.scrollTop = element.current.scrollHeight;
    }, [commentsData])

    const changeText = (e: React.ChangeEvent<HTMLInputElement>) => setText(e.target.value);

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setCommentsData(prev => [...prev, {id: prev.length + 1, username: "KB", comment: text }]);
        setText("");
    }

    return (
        <div className="comment-container">
            {market}
            <div className="comment-list" ref={element}>
                <List>
                    <RenderTextList commentList={commentsData} />
                </List>
            </div>
            <form onSubmit={onSubmit} className="form">
                <TextField
                    sx={{
                        position: "absolute",
                        left: "2px",
                        bottom: "0px",
                        width: "70vw",
                    }}
                    value={text}
                    onChange={changeText}
                    label="의견" size="small" variant="filled" />
            </form>

        </div>
    )
}

export default Comment;
