import React,{ useEffect , useState , useRef } from 'react';
import axios from 'axios';
import {useParams} from 'react-router-dom'; // HTTP경로상의 매개변수 호출
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
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import LoginSocket from '../webSocket/LoginSocket'
import ApexChart from './ApexChart'

export default function MaterialInoutList(props){


    const params = useParams(); // 매개변수가 객체형태로 들어옴.
    console.log(params.matID); // params값 확인

    const [list, setList] = useState([]);
    const [inOutList , setInOutList] = useState([]);
    const [ pageInfo , setPageInfo ] = useState( { 'page' : 1 , 'matID' : params.matID } )
    const [totalPage , setTotalPage ] = useState(1);
    const [totalCount , setTotalCount ] = useState(1);
    const [categories , setCategories ] = useState([]);
    const [data , setData] = useState([]);

        // 선택한 자재 정보 불러오기
         useEffect( ()=>{
                axios.get('/materials/materialList' , { params : {matID : params.matID} })
                  .then( r => {
                        console.log(r)
                        setList(r.data.materialList)
                        MaterialInOut();

                    })



            }, [pageInfo] )

    // 차트용도
    const setChart=(apexCharts)=>{
        const categories = [];
        const data = [];
        apexCharts.forEach((e)=>{
            categories.push(e.date)
            data.push(e.stock)
        })
        setCategories(categories)
        setData(data)
    }




    // 자재 재고 생성
    const MaterialIn = () =>{
        //유효성 검사
        const pattern_num = /[0-9]/;
        if(sessionStorage.getItem('member') == null){
            alert('로그인 후 생성 가능합니다.')
            return false
        }
        if(!(pattern_num.test(document.getElementById('mat_in_type').value))){
              alert('재고량은 숫자만 입력가능합니다.')
              return false;
        }

        // 전달할 객체 생성
        let info = {
            mat_in_type : document.getElementById('mat_in_type').value,
            matID : params.matID,
            memberdto :  JSON.parse(sessionStorage.getItem('member'))
        }

        console.log(info) // 확인
        axios.post('/materialInout/materialIn', info)
                        .then( r => { console.log(r);
                        if(r.data == true){
                        alert('등록성공 결제대기합니다.');
                        window.location.href = `/component/material/MaterialInoutList/${params.matID}`;

                        }
                        })
        }

    // 자재 재고 생성 리스트 출력
    const MaterialInOut = () =>{
            axios.get('/materialInout/MaterialInOutList' , { params : pageInfo })
                     .then( r => {
                     console.log(r)
                    setInOutList(r.data.materialInOutDtoList)
                    setTotalPage(r.data.totalPage);
                    setTotalCount(r.data.totalCount);
                    setChart(r.data.apexCharts);
            } )


    }

    // 페이지네이션
    const selectPage = (event , value) => {
            console.log(value); //
            pageInfo.page = value;
            setPageInfo({...pageInfo});
        }

    // 자재 재고 생성 최종확인
    const MaterialStock = (e) => {
        const al_app_no = e.target.value;
        let info = {
                    al_app_no : al_app_no,
                    matID : params.matID
                }
        axios.put('/materialInout/MaterialStock', info)
                                .then( r => { console.log(r);
                                if(r.data == true){
                                alert('재고증가');
                                window.location.href = `/component/material/MaterialInoutList/${params.matID}`;

                                }
                                })


    }

    // 자재 등록 취소
    const MaterialDelete = (e) => {
        const mat_in_outid = e.target.value;
        axios.delete('/materialInout/MaterialDelete' , { params : {mat_in_outid : mat_in_outid} })
                            .then( r => {
                            console.log(r)
                            if(r.data == true){alert('등록취소되었습니다.')
                            window.location.href = `/component/material/MaterialInoutList/${params.matID}`;
                            }


                    } )


    }

    // 뒤로가기 버튼
    const backSpace = ()=>{
        window.location.href = `/component/material/Material`;
    }




    return (<>

        <div>
        <h3>자제 상세내역</h3>
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
                                  <TableCell align="center" style={{ width:'15%' }}>구입일</TableCell>
                                  <TableCell align="center" style={{ width:'5%' }}>코드</TableCell>

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
                                   <TableCell align="center" >{e.mat_code}</TableCell>
                                  </TableRow>
                                ))}
                              </TableBody>
                            </Table>
                          </TableContainer>

            </div>
        <div>
            <h3>총 재고량</h3>
            <ApexChart categories={categories} data={data}/>
        </div>
        <div>
         <h3>자재 현황</h3>
                        <TableContainer component={Paper}>
                            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                              <TableHead>
                                <TableRow>
                                  <TableCell align="center" style={{ width:'5%' }}>번호</TableCell>
                                  <TableCell align="center" style={{ width:'8%' }}>자재명</TableCell>
                                  <TableCell align="center" style={{ width:'8%' }}>원가</TableCell>
                                  <TableCell align="center" style={{ width:'5%' }}>단위</TableCell>
                                  <TableCell align="center" style={{ width:'8%' }}>수량</TableCell>
                                  <TableCell align="center" style={{ width:'8%' }}>재고</TableCell>
                                  <TableCell align="center" style={{ width:'7%' }}>결제요청자</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>결제 상태</TableCell>
                                  <TableCell align="center" style={{ width:'10%' }}>결제일</TableCell>
                                  <TableCell align="center" style={{ width:'5%' }}>승인자</TableCell>
                                  <TableCell align="center" style={{ width:'6%' }}>비고</TableCell>

                                </TableRow>
                              </TableHead>
                              <TableBody>
                                {inOutList.map((e) => (
                                  <TableRow>
                                   <TableCell align="center" >{e.mat_in_outid}</TableCell>
                                   <TableCell align="center" >{e.materialDto.mat_name}</TableCell>
                                   <TableCell align="center" >{e.materialDto.mat_price}</TableCell>
                                   <TableCell align="center" >{e.materialDto.mat_unit}</TableCell>
                                   <TableCell align="center" >{e.mat_in_type}</TableCell>
                                   <TableCell align="center" >{e.mat_st_stock}</TableCell>
                                   <TableCell align="center" >{e.memberdto.mname}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalDto.al_app_whether == false
                                                            ? "결제대기중" : e.mat_in_code == 0
                                                            ? <Button variant="contained" type="button" value={e.allowApprovalDto.al_app_no} onClick={MaterialStock}>결제확인</Button>
                                                            : "결제완료"}
                                                            </TableCell>
                                   <TableCell align="center" >{e.allowApprovalDto.al_app_date == null ? "" : e.allowApprovalDto.al_app_date.substr(0,10)}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalDto.memberEntity == null ? "" : e.allowApprovalDto.memberEntity.mname}</TableCell>
                                   <TableCell align="center" >{e.allowApprovalDto.al_app_whether == true
                                                            && e.mat_in_code == 0
                                                            ? <Button variant="contained" type="button" value={e.mat_in_outid} onClick={MaterialDelete}>등록취소</Button>
                                                            : ""}</TableCell>
                                  </TableRow>
                                ))}
                              </TableBody>
                            </Table>
                          </TableContainer>
             <div style={{display : 'flex' , justifyContent : 'center' }}>
                    <Pagination count={totalPage}  color="primary" onChange={selectPage}/>
             </div>

        </div>


        <div>
            <h3> 자재 입고 </h3>
            <TextField style={{padding : '10px', margin : '10px'}} className="mat_in_type" id="mat_in_type" label="수량" variant="outlined" />
            <Stack spacing={2} direction="row">
                <Button style={{padding : '10px', margin : '10px 20px'}}variant="contained" type="button" onClick={MaterialIn}>입고</Button>
            </Stack>
        </div>
        <Button style={{padding : '10px', margin : '10px 20px'}}variant="contained" type="button" onClick={backSpace}>뒤로가기</Button>



        </div>

    </>)

}