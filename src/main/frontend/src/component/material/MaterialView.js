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
import MaterialInoutList from "./MaterialInoutList";
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Material from './Material';

export default function MaterialView(props) {

    console.log(props.countHandler)

    const [list, setList] = useState([]);
    const [ pageInfo , setPageInfo ] = useState( { 'page' : 1 , 'keyword' : '' , 'matID' : 0 } )
    const [totalPage , setTotalPage ] = useState(1);
    const [totalCount , setTotalCount ] = useState(1);

     // 자재 리스트 불러오기
     useEffect( ()=>{
            axios.get('/materials/materialList',{ params : pageInfo } )
              .then( r => {
                    console.log(r)
                    setList( r.data.materialList );
                    setTotalPage(r.data.totalPage);
                    setTotalCount(r.data.totalCount);

                } )

        }, [pageInfo] )



     // 선택한 자재 상세보기
     const MaterialInoutList=(e)=>{
            console.log(e.target.value)
            const matID = e.target.value;
            window.location.href = `/component/material/MaterialInoutList/${matID}`;
     }
    // 페이지네이션
    const selectPage = (event , value) => {
        console.log(value); //
        pageInfo.page = value;
        setPageInfo({...pageInfo});
    }
    // 검색창
    const onSearch =()=>{
        pageInfo.keyword = document.getElementById("keyword").value;
        pageInfo.page = 1
        document.getElementById("keyword").value = '';
        setPageInfo({...pageInfo});
    }
    // 페이지네이션
    const onChangecountHandler=(e)=>{
        let info ={
            matid : e.target.value,
            count : 1
        }
        props.countHandler(info)
    }


 return(<>

       <div>
      <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="center" style={{ width:'5%' }}>등록번호</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>자재명</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>원가</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>단위</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>유통기한(Day)</TableCell>
                      <TableCell align="center" style={{ width:'10%' }}>생산자</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>등록일</TableCell>
                      <TableCell align="center" style={{ width:'5%' }}>재고</TableCell>
                      <TableCell align="center" style={{ width:'5%' }}>코드</TableCell>
                      <TableCell align="center" style={{ width:'15%' }}>비고</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {list.map((e) => (
                      <TableRow>
                       <TableCell align="center" >{e.matID}</TableCell>
                       <TableCell align="center" >{e.mat_name}</TableCell>
                       <TableCell align="center" >{e.mat_price}</TableCell>
                       <TableCell align="center" >{e.mat_unit}</TableCell>
                       <TableCell align="center" >{e.mat_st_exp}</TableCell>
                       <TableCell align="center" >{e.companyDto.cname}</TableCell>
                       <TableCell align="center" >{e.mdate}</TableCell>
                       <TableCell align="center" >{e.m_stock} / {e.mat_unit}</TableCell>
                       <TableCell align="center" >{e.mat_code}</TableCell>
                       <TableCell align="center" >
                           <ButtonGroup variant="contained" aria-label="outlined Secondary button group">
                             <Button type="button" value={e.matID} onClick={MaterialInoutList}>상세보기</Button>
                             <Button onClick={onChangecountHandler} value={e.matID} >수정</Button>
                            </ButtonGroup>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
      </TableContainer>
              <div style={{display : 'flex' , justifyContent : 'center' }}>
                          <Pagination count={totalPage}  color="primary" onChange={selectPage}/>
              </div>
              <div style={{display : 'flex' , justifyContent : 'center' }}>

                 <TextField style={{padding : '10px'}} className="keyword" id="keyword" label="자재명" variant="outlined" />
              <Box sx={{ '& button': { m: 2} }}>
                 <Button style={{padding : '10px'}} variant="contained" size="medium" type="button" onClick={onSearch}>검색</Button>
              </Box>
              </div>

        </div>

    </>);
}