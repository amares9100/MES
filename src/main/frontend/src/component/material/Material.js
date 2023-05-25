import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter , Routes , Route } from "react-router-dom";
/* ---------table mui -------- */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Pagination from '@mui/material/Pagination';
/* ---------------------------*/
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import MaterialCreate from "./MaterialCreate";
import MaterialView from "./MaterialView"
import Stack from '@mui/material/Stack';
import MaterialUpdate from "./MaterialUpdate";

export default function Material(props) {

     if(sessionStorage.getItem('member') == null){
          alert('로그인 먼저 해주세요.')
          window.location.href = '/component/member/Login';
      }

    let [count , setCount] = useState(0);
    const [info , setInfo] = useState({});

    useEffect(() => {
               console.log('count : '+count);
    } , [count] )

    // 자재 수정을 위한 핸들러
    const countHandler =(e)=>{
        console.log(e);
        setCount(e.count);
        setInfo(e);
    }
    // 자재 수정을 위한 핸들러
    const returnHandler =(e)=>{
            console.log(e);
            setCount(e);

    }

    return (<>

            <div>
              <h2>자재</h2>
            </div>
            <div>
                <h3>자재 현황</h3>
                <MaterialView countHandler={countHandler}/>
            </div>


            <div>
               {count === 0 ? <MaterialCreate /> : <MaterialUpdate returnHandler={returnHandler} getInfo={info}/>}
            </div>

      </>);
}