import React,{useState, useEffect} from 'react';
import axios from 'axios'

/* -------------- mui -------------- */
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';

import Button from '@mui/material/Button';
import Pagination from '@mui/material/Pagination';
import {Container} from '@mui/material'

import { DataGrid, GridToolbar } from '@mui/x-data-grid';

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

const stylesDialog = {
  dialogContainer: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
};

export default function PlanProductList(props) {
   // 1. 상태변수 선언[ 결재 리스트 관리 Controller get으로 받아서 초기화 예정 / DataGrid 적용 ]
   const [ rows, setRows ] = useState([]);
   const [rowSelectionModel, setRowSelectionModel] = React.useState([]);

   const[deleteNotice, setDeleteNotice] = useState([]); //삭제 결과 확인
   const [openDialog, setOpenDialog] = useState(false); //dialog 닫기/열기


    useEffect(() => {
        getPlanProduct();
    }, [])

    const getPlanProduct = () => { //생산 지시 목록 가져오기
        axios.get('/planProduct')
            .then((r) => {
                console.log(r.data)
                setRows(r.data);
            })
    }

    //생산 지시 취소 => 승인이 안되었을 때만
    const deletePlanProduct = () => {
        console.log(rowSelectionModel);

        rowSelectionModel.forEach((number) => {

            axios.delete('/planProduct', { params : {prodPlanNo : number}})
              .then((r) => {
                  let note = r.data;
                  setDeleteNotice((prevDeleteNotice) => [...prevDeleteNotice, note]);
              })
          })

        openDialogHandler();
    }

    const openDialogHandler = () => { //dialog 열기
        setOpenDialog(true);
    };

     const closeDialog = () => { //dialog 닫기
       setOpenDialog(false);
       getPlanProduct();
     };


    // planProductDto를 담는 공간
    let columns = [
                { field: 'productDto.prodId', headerName: '제품번호', width: 150,
                    valueGetter: (params) => {
                         const { prodId, productDto } = params.row;
                         return `${productDto.prodId}`
                    }
                },
                { field: 'productDto.prodName', headerName : '제품명', width : 150,
                    valueGetter: (params) => {
                         const { prodName, productDto } = params.row;
                         return `${productDto.prodName}`
                    }
                },
                { field: 'productDto.prodCode', headerName : '제품코드', width : 150,
                     valueGetter: (params) => {
                         const { prodCode, productDto } = params.row;
                         return `${productDto.prodCode}`
                     }
                },
                { field: 'productDto.prodPrice', headerName : '제품가격', width : 150,
                    valueGetter: (params) => {
                         const { prodPrice, productDto } = params.row;
                         return `${productDto.prodPrice}`
                    }
                },
                { field: 'productDto.companyDto.cname', headerName : '제조사' , width : 150,
                    valueGetter: (params) => {
                         const { companyDto, productDto } = params.row;
                         return `${productDto.companyDto.cname}`
                    }
                },
                { field: 'prodPlanDate', headerName: '요청일자', width: 200 },
                { field: 'allowApprovalDto.al_app_whether', headerName: '승인여부', width: 150,
                  valueGetter: (params) => params.row.allowApprovalDto.al_app_whether ? '승인완료' : '승인대기'
                },
                { field: 'allowApprovalDto.memberEntity.mname', headerName: '승인자', width: 150,
                    valueGetter: (params) => {
                        const { memberEntity, allowApprovalDto } = params.row;
                        const mname = allowApprovalDto.memberEntity == null? '-' : allowApprovalDto.memberEntity.mname
                        return `${mname}`
                    }
                },
            ]


    return(<>
        <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button sx={{ padding: '10px', margin: '10px 20px' }} variant="contained"
                type ="button"
                onClick={deletePlanProduct}
                disabled={ rowSelectionModel.length === 0 ? true : false }
            >
                생산지시 취소
            </button>
        </Box>
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={rows}
                columns={columns}
                getRowId={((row) => row.prodPlanNo)}
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 5 },
                    },
                }}
            pageSizeOptions={[5, 10]}
            isRowSelectable={(params)=>params.row.allowApprovalDto.al_app_whether==true ? false : true}
            checkboxSelection
            onRowSelectionModelChange={(newRowSelectionModel) => {
                setRowSelectionModel(newRowSelectionModel);
            }}
            />
        </div>
        <div style={stylesDialog.dialogContainer}>
               <Dialog
                 open={openDialog}
                 TransitionComponent={Transition}
                 keepMounted
                 onClose={closeDialog}
                 aria-describedby="alert-dialog-slide-description"
               >
                 <DialogTitle>{"생산 지시 취소 처리 결과입니다."}</DialogTitle>
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
    </>);
}