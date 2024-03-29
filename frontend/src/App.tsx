import React, { useEffect } from 'react';
import Main from './components/main';
import "./App.css";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Nav from './components/nav';
import Exchange from './components/exchange';
import SignUp from './components/signUp';
import Asset from './components/asset';
import Login from './components/login';

function App() {

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/connect")

    eventSource.addEventListener('connect', e => {
      const data = e.data;
      console.log(data)
    });

    eventSource.addEventListener('msg', e => {
      const data = e.data;
      console.log(JSON.parse(data))
    });

    eventSource.addEventListener('error', e => {
      console.log(e)
    });

    window.addEventListener("beforeunload", e => {
		e.preventDefault();
	})

    return () => {
      eventSource.close();
    }

  }, [])

  return (
    <BrowserRouter>
      <Nav />
      <Routes>
        <Route path='/' element={<Main />}></Route>
        <Route path='/exchange/:id' element={<Exchange />}></Route>
        <Route path='/asset' element={<Asset />}></Route>
        <Route path='/login' element={<Login />}></Route>
        <Route path='/signup' element={<SignUp />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
