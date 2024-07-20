import React, { Fragment, useState, useEffect, useRef } from 'react';
import TextField from '@mui/material/TextField';
import "./css/comment.css";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Divider from '@mui/material/Divider';
import { MARKET } from '../data/constant';
import { deleteComment, getComments, saveComment, updateComment } from '../api/comment';
import Loading from "./loading";
import userStore from '../store/userStore';
import EditIcon from '@mui/icons-material/Edit';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import Button from '@mui/material/Button';

interface marketProps {
    market: string;
}

interface commentProps {
    id: number;
    username: string;
    content: string;
    createdDate: string;
}

interface textListProps {
    commentList: commentProps[];
    setCommentList: React.Dispatch<React.SetStateAction<commentProps[]>>;
}

interface buttonProps {
    check: boolean;
    comment: commentProps;
    editComment: (c: commentProps) => void;
    removeComment: (id: number) => void;
}

const EditRemoveButton = ({ check, comment, editComment, removeComment }: buttonProps) => {
    return (
        <span style={{
            paddingLeft: "20px"
        }}>
            <Button
                onClick={() => editComment(comment)}
                variant='outlined'
                sx={{
                    p: "3px",
                    mr: "5px",
                    minWidth: "10px",
                }}
            >

                <EditIcon
                    fontSize="small"
                    color={check ? 'disabled' : 'primary'}
                />
            </Button>
            <Button
                onClick={() => removeComment(comment.id)}
                variant='outlined'
                color='error'
                sx={{
                    p: "3px",
                    minWidth: "10px",
                }}
            >

                <RemoveCircleOutlineIcon
                    fontSize="small"
                    sx={{ color: "red" }}
                />
            </Button>
        </span>
    )
}

const RenderTextList = ({ commentList, setCommentList }: textListProps) => {
    const [isEdit, setIsEdit] = useState<boolean>(false);
    const [editId, setEditId] = useState<number>(0);
    const [content, setContent] = useState<string>("");

    const { username } = userStore();

    const editComment = async (comment: commentProps) => {
        if (!isEdit) {
            setIsEdit(true);
        } else if (editId === comment.id) {
            if (content !== "" && content !== comment.content) {
                await updateComment(comment.id, content);
                setCommentList(commentList.map(v => {
                    if (v.id === comment.id) v.content = content;
                    return v
                }))
            }
            setIsEdit(false);
        }

        setEditId(comment.id);
        setContent(comment.content);
    }

    const removeComment = async (id: number) => {
        if (window.confirm("삭제하시겠습니까?")) {
            await deleteComment(id);
            setCommentList(commentList.filter(v => v.id !== id));
        }
    }

    const ChangeComment = (e: React.ChangeEvent<HTMLInputElement>) => setContent(e.target.value);

    return (
        <>
            {commentList.map((v) => (
                <Fragment key={v.id}>
                    <ListItem>
                        {
                            v.id === editId && isEdit
                                ? (
                                    <TextField sx={{
                                        flex: "auto",
                                        width: "60vw",
                                    }}
                                        value={content}
                                        onChange={ChangeComment}
                                        variant="outlined" />
                                )
                                : (
                                    <div className="item">
                                        <div>{v.content}</div>
                                        <span>{v.username}</span>
                                        <span>{v.createdDate.replace(/T/, ' ')}</span>
                                    </div>
                                )
                        }
                        {username === v.username && <EditRemoveButton check={v.id === editId && isEdit} comment={v} editComment={editComment} removeComment={removeComment} />}
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
            const { id, createdDate } = await saveComment({ username, currencyId, content });
            setCommentsData(prev => [...prev, { id, username, content, createdDate: createdDate.replace(/\..*/, "") }]);
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
                            <RenderTextList commentList={commentsData} setCommentList={setCommentsData} />
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
                    disabled={username ? false : true}
                    label={username ? "의견" : "로그인 하세요"} size="small" variant="filled" />
            </form>

        </div>
    )
}

export default Comment;
