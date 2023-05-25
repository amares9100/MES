import React,{ useEffect , useState , useRef } from 'react';
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
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

import SalesHeader from './SalesHeader'
export default function StockView( props ){
    const [stock , setStock] = useState([]);

    let [ pageInfo1 , setPageInfo1 ] = useState( { 'page' : 1 , 'keyword' : '' , 'prodProcNo' : 0 } )
    let[totalPage1, setTotalPage1] = useState(1); //총 페이지수
    let[totalCount1, setTotalCount1] = useState(0); //총 판매 등록 개수

    // stock 출력 페이지 렌더링
    useEffect(() => {
      axios.get('/sales/getproduct_process', { params: pageInfo1 })
        .then(r => {
          console.log(r);
          setStock(r.data.productProcessDtoList);
          setTotalPage1(r.data.totalPage);
          setTotalCount1(r.data.totalCount);
        });
    }, [pageInfo1]);

     // stock 검색
     const onSearch1 =()=>{
         pageInfo1.keyword = document.querySelector(".keyword1").value;
         pageInfo1.page = 1
         document.querySelector(".keyword1").value = '';
         setPageInfo1({...pageInfo1});
     }

    // stock page 선택
     const selectPage1 = (e , value) => {
         console.log(value); //
         pageInfo1.page = value;
         setPageInfo1({...pageInfo1});
     }

    return (

      <div>
                <TableContainer component={Paper}>
                          <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                              <TableRow>
                                <TableCell align="center" style={{ width:'5%' }}> 생산번호</TableCell>
                                <TableCell align="center" style={{ width:'15%' }}>생산날짜</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>물품번호</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>물품명</TableCell>
                                <TableCell align="center" style={{ width:'15%' }}>재고량</TableCell>
                              </TableRow>
                            </TableHead>

                            <TableBody>
                                {stock.map((e) => (
                                  <TableRow>
                                    <TableCell align="center">{e.prodProcNo}</TableCell>
                                    <TableCell align="center">{e.prodProcDate}</TableCell>
                                    <TableCell align="center">{e.prodId}</TableCell>
                                    <TableCell align="center">{e.prodName}</TableCell>
                                     <TableCell align="center">{e.prodStock}</TableCell>
                                  </TableRow>
                                ))}
                            </TableBody>

                          </Table>
                        </TableContainer>
                        <Container>
                            <div style={{display : 'flex' , justifyContent : 'center' , }}>
                                        <Pagination count={totalPage1}  color="primary" onChange={selectPage1}/>
                            </div>

                            <div style={{display : 'flex' , justifyContent : 'center' , padding : '10px'  }}>
                                <input type="text" className="keyword1" />
                                <button type="button" onClick={onSearch1}> 검색 </button>
                            </div>
                        </Container>
      </div>

    )

}