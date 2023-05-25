import React,{useState, useEffect} from 'react';
import axios from 'axios'

/* -------------- mui -------------- */
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
 Panel, Pagination, Container, Button, Box, InputLabel, MenuItem, Paper,
 FormControl, Select, Stack, TextField } from '@mui/material';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

import Modal from '@mui/material/Modal';

//모달 내용
import PlanProductModalContent from './PlanProductModalContent';

//PlanProductList component
import PlanProductList from './PlanProductList'

//모달 CSS
const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '90%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  pt: 2,
  px: 4,
  pb: 3,
};

export default function PlanProduct(){
    let[pageInfo, setPageInfo] = useState({"page" : 1, "key" : '', "keyword" : ''}) //검색기능과 페이지네이션을 위해
    let[totalPage, setTotalPage] = useState(1); //총 페이지수
    let[totalCount, setTotalCount] = useState(0); //총 몇개의 제품

    let[productList, setProductList] = useState([]); //모든 제품을 담는
    const [open, setOpen] = useState(false); //모달 띄우는 상태

    const[planProduct, setPlanProduct] = useState({}); //제품 지시 내리는 제품

    const getProduct = () => {
    axios.get("/product", {params : pageInfo})
        .then(r => {
            console.log(r)
            console.log(r.data);
            setProductList(r.data.productDtoList);
            setTotalCount(r.data.totalCount);
            setTotalPage(r.data.totalPage)
        })
    }

    useEffect(() => { //컴포넌트 재렌더링시 (시작시) 제품 정보 가져오기
       getProduct();
    }, [pageInfo])

    //검색
    const onSearch = () => {
        pageInfo.key = document.querySelector('.key').value; //검색할 대상
        pageInfo.keyword = document.querySelector('.keyword').value; //검색 값
        pageInfo.page = 1; //페이지 1
        console.log("key : " + pageInfo.key + " " + "keyword : " + pageInfo.keyword)

        setPageInfo({...pageInfo})
    }

    //페이지네이션 페이지 선택
    const selectPage = (e, value) => {
        console.log(value)
        pageInfo.page = value; //클릭된 페이지 번호를 가져와서 상태변수에 대입
        setPageInfo({...pageInfo}) //클릭된 페이지번호를 상태변수에 저장
    }

   const handleOpen = (event, planProdId) => { //모달 열기
     setPlanProduct(productList.find(f => f.prodId === planProdId))
     console.log(planProduct)
     console.log(planProdId)
     setOpen(true)
   };

   const handleClose = () => { //모달 닫기
     setOpen(false);
   };


    return(<>
         <Container>
            <div>현재페이지 : {pageInfo.page}  제품 수 : {totalCount}</div>
             <TableContainer component={Paper}>
                  <Table sx={{ minWidth: 650 }} aria-label="simple table">
                    <TableHead>
                      <TableRow>
                        <TableCell align ="center" style={{width:'10%'}}>제품번호</TableCell>
                        <TableCell align ="center" style={{width:'10%'}}>제품코드</TableCell>
                        <TableCell align ="center" style={{width:'20%'}}>등록날짜</TableCell>
                        <TableCell align ="center" style={{width:'35%'}}>제품명</TableCell>
                        <TableCell align ="center" style={{width:'20%'}}>제품가격</TableCell>
                        <TableCell align ="center" style={{width:'25%'}}>회사명</TableCell>
                        <TableCell align = "center" style={{width:'20%'}}>제품지시</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {productList.map((row) => (
                          <TableRow
                              key={row.name}
                              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                          >
                            <TableCell component ="th" align="center" scope="row">{row.prodId}</TableCell>
                            <TableCell component ="th" align="left">{row.prodCode}</TableCell>
                            <TableCell component ="th" align="center">{row.prodDate}</TableCell>
                            <TableCell component ="th" align="center">{row.prodName}</TableCell>
                            <TableCell component ="th" align="center">{row.prodPrice}</TableCell>
                            <TableCell component ="th" align="center">{row.companyDto.cname}</TableCell>
                            <TableCell component ="th" align="center"><Button onClick={(event) => handleOpen(event, row.prodId)}>{row.prodName} 지시</Button></TableCell>
                      </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
                <div class ="searchBox" style={{display : "flex", justifyContent : "center", margin : '40px 0px'}}>
                     <select className ="key">
                         <option value = "cname">회사명</option>
                         <option value = "prodName">제품명</option>
                     </select>
                     <input type = "text" className = "keyword"/>
                     <button type = "button" onClick={onSearch}>검색</button>
                 </div>
                <div style={{display : "flex", justifyContent : "center", margin : '40px 0px'}}>
                    <Pagination count={totalPage} page = {pageInfo.page} color="primary" onChange = {selectPage}/>
                </div>
                <PlanProductList />
                <div>
                    <Modal
                        open={open}
                        onClose={handleClose}
                        aria-labelledby="parent-modal-title"
                        aria-describedby="parent-modal-description"
                      >

                        <Box sx={{...style, width: '80%' }}>
                          <h2 id="parent-modal-title">{planProduct.prodName}의 생산 지시</h2>
                          <PlanProductModalContent planProductInfo={planProduct} closeModal={handleClose}/>
                          <Button onClick={handleClose}>닫기</Button>
                        </Box>
                      </Modal>
                 </div>
            </Container>
    </>)
}