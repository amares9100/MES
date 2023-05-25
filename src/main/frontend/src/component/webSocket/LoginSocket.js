import React,{ useState , useEffect , useRef} from 'react';
import axios from 'axios';
import { BrowserRouter , Routes , Route } from "react-router-dom";
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import "./LoginSocket.css";


export default function LoginSocket(props) {
    console.log('LoginSocket실행중')

    let [msg , setMsg ] = useState([]);

    let ws = useRef( null ) ;   // 1.모든 함수 사용할 클라이언트소켓 변수



    useEffect(() => {

    if( !ws.current ){      // 3.클라이언트소켓이 접속이 안되어있을때. [ * 유효성검사 ]

                ws.current = new WebSocket("ws://192.168.17.17:8080/chat") ; // 4. 서버소켓 연결

                ws.current.onopen = () => {   console.log('서버 접속했습니다.');}

                ws.current.onmessage = (e) => {  console.log('서버소켓으로 메시지 받음');  console.log( e);  console.log( e.data );
                    let data = JSON.parse( e.data );
                    let member = JSON.parse(sessionStorage.getItem('member'));
                    let alertMsg = '';

                        if(data == 10 && member.position == "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="info">자재 : 결제확인이 필요합니다.</Alert></Stack>}
                        else if(data == 11 && member.position !== "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="success">자재 : 결제되었습니다.</Alert></Stack>}

                        else if(data == 20 && member.position == "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="info">제품 : 결제확인이 필요합니다.</Alert></Stack>}
                        else if(data == 21 && member.position !== "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="success">제품 : 결제되었습니다.</Alert></Stack>}

                        else if(data == 30 && member.position == "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="info">판매 : 결제확인이 필요합니다.</Alert></Stack>}
                        else if(data == 31 && member.position !== "임원"){alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="success">판매 : 결제되었습니다.</Alert></Stack>}


                        /* ---- 자동 생산 관련 ---- */
                        else if(data.type == 1){ //자동 생산 실패 - 자재 부족시
                            alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="error">[자동 생산 실패] {data.prodName} 제품에 {data.matName} 자재가 부족합니다.</Alert></Stack>
                        }else if(data.type == 2){ //자동 생산 성공시
                             alertMsg = <Stack sx={{ width: '90%' , height : '50px' , margin: '5px' }} spacing={1}><Alert severity="success">[자동 생산 성공] {data.prodName} 제품 생산이 완료되었습니다.</Alert></Stack>
                        }


                    setMsg( (msg)=> [...msg , alertMsg  ] ); // 재 렌더링
                }
            }
    } , [])

    useEffect ( () => {
            document.querySelector('.socketWrap').scrollTop = document.querySelector('.socketWrap').scrollHeight;
        },[msg])


  return (<>
       <div className='socketWrap' style={{ width: '40%', height: '125px'}}>
          {msg}
       </div>
  </>);
}
