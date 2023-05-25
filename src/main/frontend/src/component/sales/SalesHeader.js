import React,{ useState , useEffect } from 'react';
import axios from 'axios';
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

import StockView from "./StockView"
import SalesCreate from "./SalesCreate";
import SalesView from "./SalesView"

export default function SalesHeader() {

   if(sessionStorage.getItem('member') == null){
        alert('로그인 먼저 해주세요.')
        window.location.href = '/component/member/Login';
    }

  return (
    <>
      <div>
        <h3> 제품공정 현황 </h3>
        <StockView />
      </div>

      <div>
        <h3> 판매 등록 </h3>
        <SalesCreate />
      </div>

      <div>
        <h3> 판매 현황 </h3>
        <SalesView />
      </div>

    </>
  );
}






