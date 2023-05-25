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
import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';

export default function MaterialCreate() {

    let [list , setList] = useState([])


     useEffect( ()=>{
        axios.get('/materials/getcompany')
          .then( r => {
                console.log(r)
                setList(r.data)
            } )

    }, [] )


    const MaterialCreate=()=>{
    const pattern_num = /[0-9]/;	// 숫자
    if(company == 0){
        alert('회사를 선택해주세요')
        return false;
    }
    if(code == 0){
        alert('코드를 선택해주세요')
        return false;
    }
    if(!(pattern_num.test(document.getElementById('MatPrice').value))){
        alert('단가는 숫자만 입력가능합니다.')
        return false;
    }



    let info={
        mat_name : document.getElementById('MatName').value,
        mat_unit : document.getElementById('MatUnit').value,
        mat_st_exp : document.getElementById('MatStExp').value,
        mat_price : document.getElementById('MatPrice').value,
        cno : company,
        mat_code : code
    }

    console.log(info)

    axios.post('/materials/materialCreate', info)
                .then( r => { console.log(r);
                if(r.data == true){
                alert('등록성공')
                window.location.href="/component/material/Material"



                }

                } )


     }


    const [company, setCompany] = useState(0);
        const handleChange = (event) => {
            console.log(event.target.value)
            setCompany(event.target.value);

        };

    const [code, setCode] = useState(0);

    const handleChange2 = (event) => {
        console.log(event.target.value)
        setCode(event.target.value);

    };



  return (<>
  <h3>자재 등록</h3>
    <div style={{border: "2px solid #1a75ff" , borderRadius : '15px'}}>
    <div style={{display : 'flex' , padding : '10px', margin : '10px'}}>
           <Box sx={{ minWidth: 120 }}>
                      <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                        <InputLabel id="demo-simple-select-label">회사</InputLabel>
                        <Select  value={ company } label="카테고리" onChange={ handleChange } >
                            <MenuItem value={0}>회사</MenuItem>
                            {
                                list.map( (c) => {
                                    return   <MenuItem value={c.cno}>{ c.cname }</MenuItem>
                                })
                            }
                        </Select>
                      </FormControl>
                    </Box>
            <Box sx={{ minWidth: 120 }}>
                      <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                        <InputLabel id="demo-simple-select-label">식별코드</InputLabel>
                        <Select  value={ code } label="식별코드" onChange={ handleChange2 } >
                            <MenuItem value={0}>코드</MenuItem>
                            <MenuItem value="A">A</MenuItem>
                            <MenuItem value="B">B</MenuItem>
                            <MenuItem value="C">C</MenuItem>
                            <MenuItem value="D">D</MenuItem>
                            <MenuItem value="E">E</MenuItem>
                        </Select>
                      </FormControl>
                    </Box>
            </div>
    <div>

              <TextField style={{padding : '10px', margin : '10px'}} className="MatName" id="MatName" label="자재이름" variant="outlined" />
              <TextField style={{padding : '10px', margin : '10px'}} className="MatUnit" id="MatUnit" label="단위" variant="outlined" />
              <TextField style={{padding : '10px', margin : '10px'}} className="MatStExp" id="MatStExp" label="유통기한(Day)" variant="outlined" />
              <TextField style={{padding : '10px', margin : '10px'}} className="MatPrice" id="MatPrice" label="단가" variant="outlined" />

        <Stack spacing={2} direction="row">
            <Button style={{padding : '10px', margin : '10px 20px'}}variant="contained" type="button" onClick={MaterialCreate}>자재등록</Button>
        </Stack>
     </div>
    </div>
</>
  );
}