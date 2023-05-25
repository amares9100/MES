import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Container, Grid, Typography, TextField, Button, Box } from '@mui/material';
import LoginSocket from '../webSocket/LoginSocket'
import MESInfo from './img/MESInfo.png';

export default function Login(props) {

    const [ mname, setMname ] = useState('');
    const [ password, setPassword ] = useState('');
    const [ loggedIn, setLoggedIn ] = useState(false);
    const [ member, setMember ] = useState(null);

    const handleUsernameChange = (event) => setMname(event.target.value);
    const handlePasswordChange = (event) => setPassword(event.target.value);

    // 1. 로그인 메소드
    const handleLogin = () => {
        let data = { mname: mname, mpassword: password };
        axios.post('/home/login', data)
            .then( (response) => {
                   console.log(response.data); // --- 확인 완료
                if(response.data){
                    setLoggedIn(true);
                    handleUserInfo(); // 로그인 정보 업로드
                    // window.location.href = '/component/member/AllowApproval';
                } else{
                    window.location.href = '/component/member/Login';
                }
            })
            .catch(error => {
                console.error('로그인 실패', error);
            });
    };

    // 2. UserInfo 받아오는 메소드
    const handleUserInfo = ()=>{
        axios.get('/home/loginInfo')
            .then((response)=>{
                console.log(response);
                setMember(response.data);
                sessionStorage.setItem('member', JSON.stringify(response.data));

                const { position } = response.data;
                if (position === "임원") {
                    window.location.href = '/component/member/AllowApproval';
                } else {
                    window.location.href = '/component/member/Login';
                }

                {<LoginSocket />}
            })
            .catch(error => {
                console.error('login user 데이터 업로딩 실패', error);
            });
    };

    // 3. 로그아웃 메소드
    const handleLogout = () => {
        console.log('logout click');
        axios.get('/home/logout')
            .then(response => {
                setLoggedIn(false);
                setMember(null);
                sessionStorage.removeItem('member');
                window.location.href = '/component/member/Login';
                console.log('로그아웃 성공');
            })
            .catch(error => {
                console.error('로그아웃 실패:', error);
            });
        };

    return (
      <>
        {sessionStorage.getItem('member') != null ? (
          <Container component="main" maxWidth="xs" style={{ marginTop: '3%' }}>
            <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
              <Button
                sx={{ padding: '10px', margin: '10px 20px' }}
                variant="contained"
                type="button"
                onClick={handleLogout}
              >
                로그아웃
              </Button>
            </Box>
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
              <img src={MESInfo} alt="MES Info" style={{ width: '100%' }} />
            </Box>
            <Box mt={5} className="copyright">
              <Copyright />
            </Box>
          </Container>
        ) : (
          <Container component="main" maxWidth="xs" style={{ marginTop: '8%' }}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <Typography variant="h5" component="h1">
                  로그인
                </Typography>
              </Grid>
            </Grid>
            <form noValidate>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    variant="outlined"
                    required
                    fullWidth
                    id="username"
                    label="아이디"
                    name="mname"
                    autoComplete="mname"
                    onChange={handleUsernameChange}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    variant="outlined"
                    required
                    fullWidth
                    type="password"
                    id="password"
                    label="패스워드"
                    name="password"
                    autoComplete="current-password"
                    onChange={handlePasswordChange}
                  />
                </Grid>
                <Grid item xs={12}>
                  <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    type="button"
                    onClick={handleLogin}
                  >
                    로그인
                  </Button>
                </Grid>
              </Grid>
            </form>
            <Box mt={5} className="copyright">
              <Copyright />
            </Box>
          </Container>
        )}
      </>
    );
}

// 하단 회사명
function Copyright(props) {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            MES, {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}