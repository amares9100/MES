import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import { Typography, Box, Tabs, Tab } from '@mui/material';

import AllowMaterial from './AllowMaterial';
import AllowProduct from './AllowProduct';
import AllowSales from './AllowSales';

export default function AllowApproval() {

    const[ value, setValue ] = useState(0);
    const[ screen, setScreen ] = useState(<AllowMaterial/>)

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);

        if(newValue == 0){              // --- 자재
            setScreen(<AllowMaterial/>)
        }else if(newValue == 1){        // --- 제품
            setScreen(<AllowProduct/>)
        }else if(newValue == 2){        // --- 판매
            setScreen(<AllowSales/>)
        }
    };

    return(<>
        <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
            <Tabs value={value} onChange={handleChange} centered>
                <Tab label="자재 결재" />
                <Tab label="제품 결재" />
                <Tab label="판매 결재" />
            </Tabs>
            {screen}
        </Box>
    </>);
}