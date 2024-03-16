import React from 'react';
import Main from './components/main';
import "./App.css";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Nav from './components/nav';
import Exchange from './components/exchange';
import SignUp from './components/signUp';
import Asset from './components/asset';
import Login from './components/login';

function App() {
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
