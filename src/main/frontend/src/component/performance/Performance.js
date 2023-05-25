import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Typography, Box, Tabs, Tab } from '@mui/material';

import PerformanceForm from './PerformanceForm';
import PerformanceProduction from './PerformanceProduction';
import PerformanceSales from './PerformanceSales';

export default function Performance() {

    if(sessionStorage.getItem('member') == null){ // 로그인 여부 체킹
        alert('로그인 먼저 해주세요.')
        window.location.href = '/component/member/Login';
    }

    const[ value, setValue ] = useState(0);
    const[ screen, setScreen ] = useState(<PerformanceProduction/>)

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);

        if(newValue == 0){              // --- 생산 실적
            setScreen(<PerformanceProduction/>)
        }else if(newValue == 1){        // --- 판매 실적
            setScreen(<PerformanceSales/>)
        }
    };

    return(<>
        <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
            <Tabs value={value} onChange={handleChange} centered>
                <Tab label="생산 실적" />
                <Tab label="판매 실적" />
            </Tabs>
            {screen}
        </Box>
    </>);
}