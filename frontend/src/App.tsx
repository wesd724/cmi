import Main from './components/main';
import "./App.css";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Nav from './components/nav';
import Exchange from './components/exchange';
import SignUp from './components/signUp';
import Login from './components/login';
import ActivityTabs from './components/activityTabs';
import InvestmentTabs from './components/investmentTabs';

function App() {
  return (
    <BrowserRouter>
      <Nav />
      <Routes>
        <Route path='/' element={<Main />}></Route>
        <Route path='/exchange/:id' element={<Exchange />}></Route>
        <Route path='/investment' element={<InvestmentTabs />}></Route>
        <Route path='/activity' element={<ActivityTabs />}></Route>
        <Route path='/login' element={<Login />}></Route>
        <Route path='/signup' element={<SignUp />}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
