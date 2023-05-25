import React, {useState, useEffect} from 'react';
import axios from 'axios'

/* -------------- mui -------------- */
import TextField from '@mui/material/TextField'; //텍스트 필드
import Container from '@mui/material/Container';
import Select from '@mui/material/Select';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
/* -------------- mui -------------- */

export default function EditProduct(props){ //제품 추가 부분
    let[companyList, setCompanyList] = useState([]) //DB에 저장된 회사 정보를 담는
    let [company , setCompany] = useState(0) //변경된 회사 정보를 담는 useState
    let [product, setProduct] = useState({}); //매개변수로 받은 제품 객체 정보 저장

    const[productPrice, setProductPrice] = useState(0) //기존 제품 가격에서 onchange()하기위해 사용
    const[productName, setProductName] = useState('') //기존 제품 이름에서 onchange()하기 위해 사용

    //회사 정보를 가져오는 useEffect
     useEffect( ()=>{
        axios.get('/materials/getcompany')
          .then( r => {
                console.log(r)
                setCompanyList(r.data)
            } )
     }, [] )

     useEffect(() => {
        setProduct(props.product)
        setProductName(product.prodName)
        setProductPrice(product.prodPrice)
        setCompany(product.companyDto?.cno)
     }, [props.product])

    const companyChangeHandler = (e) => { //회사 변경 이벤트 처리(select)
        console.log(e.target.value)
        setCompany(e.target.value);
    }



    //제품 수정
    const updateProduct = () => {

        // 유효성검사
        if(company == 0){
            alert('회사를 선택해주세요.');
            return false;
        }

        if(document.getElementById('prodName').value == ''){
            alert('제품 이름을 입력해주세요.');
            return false;
        }

        if(document.getElementById('prodPrice').value == ''){
            alert('제품 가격을 입력해주세요.');
            return false;
        }

        //유효성 통과시 전달할 객체 보내기
        let info ={
            prodId : props.product.prodId,
            prodCode : 'C',
            prodName : document.getElementById('prodName').value,
            prodPrice : document.getElementById('prodPrice').value,
            companyDto : companyList.find(e => e.cno === company), //해당 cno가 company의 정보를 가진 객체를 넣음
            type : 1
        }

        console.log(info);

        axios.put('/product', info)
            .then((r) => {
                if(r.data == true){
                    alert('수정 성공');
                    props.callback()
                }else{
                    alert('수정 실패')
                }
            })
    }

    //작업 취소
    const cancel = () => {
        setCompany(0)
        setProductName('')
        setProductPrice(0)
    }

    return(<>
        <Container>
                <div>
                  <FormControl style={{ width : '100px' , margin : '20px 0px'}}>
                    <InputLabel id="demo-simple-select-label">회사</InputLabel>
                    <Select value={ company } label="카테고리" onChange={ companyChangeHandler } >
                        <MenuItem value={0}>회사</MenuItem>
                        {
                            companyList.map( (c) => {
                                return   <MenuItem value={c.cno}>{ c.cname }</MenuItem>
                            })
                        }
                    </Select>
                  </FormControl>
                </div>

               <div>
                  <TextField style={{padding : '10px', margin : '10px'}} className="prodCode" id="prodCode" label="제품코드" variant="outlined" value={'C'} inputProps= {{readOnly : true}}/>
                  <TextField style={{padding : '10px', margin : '10px'}} className="prodName" id="prodName" label="제품명" variant="outlined" value={productName} onChange={(e) => setProductName(e.target.value)}/>
                  <TextField style={{padding : '10px', margin : '10px'}} className="prodPrice" id="prodPrice" label="제품가격" variant="outlined" value={productPrice} onChange={(e) => setProductPrice(e.target.value)} />
               </div>

              <div>
                    <Button type ="button" onClick={updateProduct}>제품 수정</Button>
                    <Button type ="button" onClick={cancel}>작업 취소</Button>
              </div>
        </Container>
    </>)

}