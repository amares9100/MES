import React, {useState, useEffect} from 'react';
import axios from 'axios'

/* -------------- mui -------------- */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

import Button from '@mui/material/Button';
import Pagination from '@mui/material/Pagination';

import {Checkbox} from '@mui/material';
import {Container} from '@mui/material'

import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
/* -------------- mui -------------- */

import EditProduct from './EditProduct'
import MaterialPrint from './MaterialPrint';


/* --------- Dialog -----------*/
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Slide from '@mui/material/Slide';

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

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

const stylesDialog = {
  dialogContainer: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
};

export default function ManageProduct(props){
    let[pageInfo, setPageInfo] = useState({"page" : 1, "key" : '', "keyword" : ''}) //검색기능과 페이지네이션을 위해
    let[totalPage, setTotalPage] = useState(1); //총 페이지수
    let[totalCount, setTotalCount] = useState(0); //총 몇개의 제품
    const [checked, setChecked] = useState([]); //삭제할 제품PK를 모두 받아옴

    let[productList, setProductList] = useState([]); //모든 제품을 담는

    let[putProduct, setPutProduct] = useState({}); //수정할 목록

    const [open, setOpen] = useState(false); //모달 띄우는 상태

    const[putFindProduct, setPutFindProduct] = useState({prodName:''}); //수정할 제품
    const[deleteNotice, setDeleteNotice] = useState([]); //삭제 결과 확인

    const [openDialog, setOpenDialog] = useState(false); //dialog 닫기/열기

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

    //제품 수정 [제품 번호를 눌러야함...]
    const onUpdateHandler = (e) => {
       let uproduct = productList.find(f => f.prodId == e.target.innerHTML)
       setPutProduct({...uproduct})
    }

    //체크 박스 업데이트[삭제를 위한]
    const checkboxEventHandler = (num) => {
       console.log(checked)
       if (checked.includes(num)) { //배열에 저장되어있는데 체크박스를 누른거면 취소니까 해당 배열에 그 번호를 삭제해준다
           setChecked(checked.filter((checked) => checked !== num));
       }else{
            setChecked([...checked, num]); //배열에 해당 클릭한 번호 저장
       }
    }

   const handleOpen = (event, findProd) => { //모달 열기
     setPutFindProduct(productList.find(f => f.prodId === findProd))
     console.log(findProd)
     setOpen(true)
   };

   const handleClose = () => { //모달 닫기
     setOpen(false);
   };

   //선택 삭제
   const selectDelete = (event) => {
        setDeleteNotice([]); //삭제 처리할 때 한번 초기화해주고.

        checked.forEach((item) => {

            axios.delete('/product', { params : {prodId : item}})
              .then((r) => {
                  let note = r.data;
                  setDeleteNotice((prevDeleteNotice) => [...prevDeleteNotice, note]);
              })
          });

          openDialogHandler();
   }

    const openDialogHandler = () => {
        setOpenDialog(true);
    };

     const closeDialog = () => {
       setOpenDialog(false);
       getProduct();
     };

    return(<>
         <Container>
            <div>현재페이지 : {pageInfo.page}  게시물 수 : {totalCount}
            <div style={{width : '100%', display : 'flex', justifyContent : 'flex-end'}}>
                <Button style={{marginLeft : '100px'}} onClick={(event) => selectDelete(event)}>선택삭제</Button></div>
             </div>
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
                        <TableCell align = "center" style={{width:'20%'}}>자재목록</TableCell>
                        <TableCell align ="center" style={{width:'25%'}}>비고</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {productList.map((row) => (
                          <TableRow
                              key={row.name}
                              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                              onClick ={(e) => onUpdateHandler(e)}
                          >
                            <TableCell component ="th" align="center" scope="row">{row.prodId}</TableCell>
                            <TableCell component ="th" align="left">{row.prodCode}</TableCell>
                            <TableCell component ="th" align="center">{row.prodDate}</TableCell>
                            <TableCell component ="th" align="center">{row.prodName}</TableCell>
                            <TableCell component ="th" align="center">{row.prodPrice}</TableCell>
                            <TableCell component ="th" align="center">{row.companyDto.cname}</TableCell>
                            <TableCell component ="th" align="center"><Button onClick={(event) => handleOpen(event, row.prodId)}>자재 수정</Button></TableCell>
                            <TableCell align="center"><Checkbox onChange={() => checkboxEventHandler(row.prodId)}/></TableCell>
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
                <div style={{display : 'flex' , justifyContent : 'center', marginTop:'30px'}}>
                    <EditProduct product={putProduct} callback={getProduct}/> {/*선택한 자재 PK를 제품 입력칸 부분에 전달*/}
                </div>
                <div>
                    <Modal
                        open={open}
                        onClose={handleClose}
                        aria-labelledby="parent-modal-title"
                        aria-describedby="parent-modal-description"
                      >

                        <Box sx={{...style, width: '80%' }}>
                          <h2 id="parent-modal-title">{putFindProduct.prodName}의 자재 목록</h2>
                          <MaterialPrint putProdId={putFindProduct.prodId}/>
                          <Button onClick={handleClose}>닫기</Button>
                        </Box>
                      </Modal>
                 </div>

                 <div style={stylesDialog.dialogContainer}>
                       <Dialog
                         open={openDialog}
                         TransitionComponent={Transition}
                         keepMounted
                         onClose={closeDialog}
                         aria-describedby="alert-dialog-slide-description"
                       >
                         <DialogTitle>{"삭제 처리 결과입니다."}</DialogTitle>
                         <DialogContent>
                           <DialogContentText id="alert-dialog-slide-description">
                             {
                                deleteNotice.map((notice) => {
                                    return <div>{notice}</div>
                                })
                             }
                           </DialogContentText>
                         </DialogContent>
                         <DialogActions>
                           <Button onClick={closeDialog}>닫기</Button>
                         </DialogActions>
                       </Dialog>
                 </div>

          </Container>
    </>)
}