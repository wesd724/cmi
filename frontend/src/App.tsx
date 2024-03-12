import React from 'react';
import Main from './components/main';
import "./App.css";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Nav from './components/nav';
import Exchange from './components/exchange';

function App() {
  return (
    <BrowserRouter>
      <Nav />
      <Routes>
        <Route path='/' element={<Main />}></Route>
        <Route path='/exchange' element={<Exchange />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
