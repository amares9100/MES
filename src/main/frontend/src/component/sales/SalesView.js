import React,{ useEffect , useState , useRef } from 'react';
import {useParams} from 'react-router-dom'; // HTTP경로상의 매개변수 호출
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
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

import Chart from 'chart.js/auto';

import SalesHeader from './SalesHeader'
import SalesChart from './SalesChart'


export default function SalesView( props ){
    const [list, setList] = useState([]); // sales 모든 정보 담긴 변수
    const [CompanyList , setCompanyList ] = useState([])     // 저장된 회사 리스트
    const [ProductList , setProductList ] = useState([])     // 저장된 물품 리스트
    const [orderCount, setOrderCount] = useState('');         // 개수
    const [salesPrice, setSalesPrice] = useState('');         // 가격

    let [pageInfo2 , setPageInfo2 ] = useState( { 'page' : 1 , 'keyword' : '' , 'order_id' : 0 } )
    let[totalPage2, setTotalPage2] = useState(1);   //총 페이지수
    let[totalCount2, setTotalCount2] = useState(0); //총 판매 등록 개수

    // 판매 출력 페이지 렌더링
    useEffect(() => {
      axios.get('/sales/salesView', { params: pageInfo2 })
        .then(r => {
          console.log(r);
          setList(r.data.salesDtoList);
          setTotalPage2(r.data.totalPage2);
          setTotalCount2(r.data.totalCount2);
        });
    }, [pageInfo2]);

    // sales order_status 검색 [ 0 : 승인 전 / 1 : 승인 후, 판매 전 / 2 : 판매 후 ]
    const onSearch2 =()=>{
        pageInfo2.keyword = document.querySelector(".keyword").value;
        pageInfo2.page = 1
        document.querySelector(".keyword").value = '';
        setPageInfo2({...pageInfo2});
    }

    // sales 페이지 선택
    const selectPage2 = (e , value) => {
        console.log(value); //
        pageInfo2.page = value;
        setPageInfo2({...pageInfo2});
    }

    // 판매 삭제
    const SalesDelete = (e) => {

        if(sessionStorage.getItem('member') == null){ // 로그인 유효성 검사
        alert('로그인 후 가능한 기능입니다.'); return false;
        }

        const order_id = e.target.value
        axios.delete('/sales/delete' , {params : { order_id : order_id}})
            .then ( r => {
                console.log(r);
                if( r.data == true ) {
                    alert('판매등록이 취소되었습니다.')
                    window.location.href = "/component/sales/SalesHeader"
                    }
            })
    }
    // 판매 수정 박스 style
    const style = { position: 'absolute', top: '50%', left: '50%',
      transform: 'translate(-50%, -50%)', width: 360, bgcolor: 'background.paper',
      border: '2px solid #000', boxShadow: 24, p: 4,
    };


    const [open, setOpen] = React.useState(false);
    const handleClose = () => setOpen(false);

    const handleOpen = (e) =>{
       const order_id = e.target.value;
       const selectedOrder = list.find((item) => item.order_id === order_id);
       console.log(order_id);
       setOpen(true);
    }

    // 판매 확정
    const SalesResult = (e) => {

        if(sessionStorage.getItem('member') == null){ // 로그인 유효성 검사
        alert('로그인 후 가능한 기능입니다.'); return false;
        }

         console.log(e.target.value)
         const [order_id, al_app_no , prodId] = e.target.value.split(","); // 값 전달 split , 로 분류
         let info = { al_app_no : al_app_no , order_id : order_id , prodId : prodId }
         axios.put('/sales/SalesStock' , info )
             .then ( r => {
                 if( r.data == true ) {
                     alert('판매 최종 확정 처리되었습니다.')
                     window.location.href = "/component/sales/SalesHeader"
                 } else{
                    alert('재고량보다 판매량이 더 많습니다.(삭제요망)')
                 }
             })
    }

    // 회사 호출
    useEffect ( () => {
        axios.get('/sales/getcompany')
            .then( r => {
                console.log(r)
                setCompanyList(r.data)
            })
    }, [] )

    // 물품 호출
        useEffect ( () => {
            axios.get('/sales/getproduct')
                .then( r => {
                    console.log(r)
                    setProductList(r.data)
                })
        }, [] )

    // 판매 수정
    const SalesUpdate = (order_id) => {
        // 유효성검사1 [ 공백 or 기본값인 경우 불가 ]
        if ( company == 0 ){ alert('회사를 선택해주세요.'); return false; }
        if ( prodName == 0 ){ alert('판매할 물품 이름을 선택해주세요.'); return false; }
        if ( orderCount == '' ){ alert('판매할 물품 개수를 입력해주세요.'); return false; }
        if ( salesPrice == '' ){ alert('판매할 물품 가격을 입력해주세요.'); return false; }

        // 유효성검사2 [ 아이디 로그인 ]
        // 로그인해야 판매등록 가능!
        if(sessionStorage.getItem('member') == null){
        alert('로그인 후 가능한 기능입니다.'); return false;
        }

        let info = {
          order_id : order_id ,
          memberDto : JSON.parse(sessionStorage.getItem('member')) ,
          orderCount: orderCount, salesPrice: salesPrice,
          cno : company , prodId : prodName
        }

        console.log(info)

        axios.put('/sales/SalesUpdate', info)
          .then(r => { console.log(r);
            if (r.data === true) {
              alert('수정성공')
              window.location.href = "/component/sales/SalesHeader"
            }
          })
          .catch(error => {
            console.error(error);
          });
      }

    // 저장할 회사
    const [company, setCompany] = useState(0);
        const handleChange = (event) => {
            console.log(event.target.value)
            setCompany(event.target.value);
        };

    // 저장할 물품
    const [ prodName, setProdName] = useState(0);
            const handleChange2 = (event) => {
                console.log(event.target.value)
                setProdName(event.target.value);
        };

    // OrderCount  업데이트 함수
    const handleOrderCountChange = (event) => {
      setOrderCount(event.target.value);
    }
    // SalesPrice  업데이트  값
    const handleSalesPriceChange = (event) => {
      setSalesPrice(event.target.value);
    }

    return (
        <div>
                <TableContainer component={Paper}>
                          <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                              <TableRow>
                                <TableCell align="center" style={{ width:'5%' }}>판매번호</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매날짜</TableCell>
                                <TableCell align="center" style={{ width:'6%' }}>물품번호</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>물품명</TableCell>
                                <TableCell align="center" style={{ width:'8%' }}>판매개수</TableCell>
                                <TableCell align="center" style={{ width:'8%' }}>판매가격</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>회사명</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>판매상태</TableCell>
                                <TableCell align="center" style={{ width:'10%' }}>이름(직급)</TableCell>
                                <TableCell align="center" style={{ width:'12%' }}>비고</TableCell>
                              </TableRow>
                            </TableHead>

                            <TableBody>
                                {list.map((e) => (
                                  <TableRow>
                                    <TableCell align="center">{e.order_id}</TableCell>
                                    <TableCell align="center">{e.udate}</TableCell>
                                    <TableCell align="center">{e.prodId}</TableCell>
                                    <TableCell align="center">{e.prodName}</TableCell>
                                    <TableCell align="center">{e.orderCount}</TableCell>
                                    <TableCell align="center">{e.salesPrice}</TableCell>
                                    <TableCell align="center">{e.companyDto.cname}</TableCell>
                                    <TableCell align="center">{e.order_status == 0 ? '판매대기' : e.order_status == 1 ? '판매승인' : '판매확정' }</TableCell>
                                    <TableCell align="center">  {`${e.memberDto.mname} (${e.memberDto.position})`}</TableCell>

                                    <TableCell align="center">
                                      <ButtonGroup variant="contained" aria-label="outlined Secondary button group">
                                        {e.order_status === 0 ?
                                          <>
                                                  <Button type="button" value={e.order_id} onClick={handleOpen}>수정</Button>
                                                  <Modal open={open} onClose={handleClose} aria-labelledby="modal-modal-title" aria-describedby="modal-modal-description" >
                                                    <Box sx={style}>

                                                      <Typography style={{display:'flex' , justifyContent : 'center'}} id="modal-modal-title" variant="h6" component="h2">
                                                        판매 수정
                                                      </Typography>

                                                      <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                                                                         <div style={{border: "2px solid #1a75ff" , borderRadius : '15px'}}>
                                                                         <div style={{display : 'flex' , justifyContent: 'center', padding : '10px', margin : '10px'}}>
                                                                                <Box sx={{ minWidth: 120 }}>
                                                                                           <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                                                                                             <InputLabel id="demo-simple-select-label">회사</InputLabel>
                                                                                             <Select  value={ company } label="카테고리" onChange={ handleChange } >
                                                                                                 <MenuItem value={0}>회사</MenuItem>
                                                                                                 {
                                                                                                     CompanyList.map( (c) => {
                                                                                                         return   <MenuItem value={c.cno}> { c.cname } </MenuItem>
                                                                                                     })
                                                                                                 }
                                                                                             </Select>
                                                                                           </FormControl>
                                                                                         </Box>
                                                                                <Box sx={{ minWidth: 120 }}>
                                                                                           <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                                                                                             <InputLabel id="demo-simple-select-label">물품</InputLabel>
                                                                                             <Select  value={ prodName } label="카테고리" onChange={ handleChange2 } >
                                                                                                 <MenuItem value={0}>물품이름</MenuItem>
                                                                                                 {
                                                                                                     ProductList.map( (p) => {
                                                                                                         return   <MenuItem value={p.prodId}>{ p.prodName }</MenuItem>
                                                                                                     })
                                                                                                 }
                                                                                             </Select>
                                                                                           </FormControl>
                                                                                         </Box>
                                                                                 </div>
                                                                         <div>

                                                                                <TextField  style={{ display : 'flex' , justifyContent: 'center', padding: '10px', margin: '10px' }} className="orderCount" id="orderCount" label="판매개수" variant="outlined" value={orderCount} onChange={(e) => setOrderCount(e.target.value)} />
                                                                                <TextField style={{ display : 'flex' , justifyContent: 'center', padding: '10px', margin: '10px' }} className="salesPrice" id="salesPrice" label="판매가격" variant="outlined" value={salesPrice} onChange={(e) => setSalesPrice(e.target.value)} />

                                                                                <Stack style={{ display : 'flex' , justifyContent: 'center' , marginBottom: '30px' }} spacing={2} direction="row">
                                                                                  <ButtonGroup>
                                                                                        <Button type="submit" value={e.order_id} onClick={() => SalesUpdate(e.order_id)}>
                                                                                          수정완료
                                                                                        </Button>
                                                                                        <Button type="button" onClick={handleClose}> 수정취소 </Button>
                                                                                  </ButtonGroup>
                                                                                </Stack>
                                                                          </div>
                                                                         </div>
                                                      </Typography>
                                                    </Box>
                                                  </Modal>
                                            <Button type="button" value={e.order_id} onClick={SalesDelete}>삭제</Button>
                                          </>
                                          : e.order_status === 1 ?
                                          <ButtonGroup>
                                            <Button type="button" value={`${e.order_id},${e.allowApprovalDto.al_app_no},${e.prodId}`} onClick={SalesResult}>판매확정</Button>
                                            <Button type="button" value={e.order_id} onClick={SalesDelete}>삭제</Button>
                                          </ButtonGroup>
                                          : e.order_status === 2 ? <div> 판매완료 </div> : null
                                        }
                                      </ButtonGroup>
                                    </TableCell>

                                  </TableRow>
                                ))}
                            </TableBody>

                          </Table>
                        </TableContainer>
                        <Container>
                            <div style={{display : 'flex' , justifyContent : 'center' }}>
                                        <Pagination count={totalPage2}  color="primary" onChange={selectPage2}/>
                            </div>

                            <div style={{display : 'flex' , justifyContent : 'center' , padding : '10px'  }}>
                                <input type="text" className="keyword" />
                                <button type="button" onClick={onSearch2}> 검색 </button>
                            </div>
                        </Container>

                        <Container>
                            <div>
                              <SalesChart list={list} />
                            </div>
                        </Container>
        </div>

    )

}