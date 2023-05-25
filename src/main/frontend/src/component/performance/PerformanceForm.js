import React,{ useState , useEffect } from 'react';
import axios from 'axios';

import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
 Panel, Pagination, Container, Button, Box, InputLabel, MenuItem,
 FormControl, Select, Stack, TextField } from '@mui/material';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';

import ViewChart from './ViewChart';

export default function PerformanceForm(props) {

    // 1. 상태변수
    const [ rows, setRows ] = useState([]);
    const [rowSelectionModel, setRowSelectionModel] = React.useState([]);
    const [ type, setType ] = useState(props.type);

    // 2. fetchRows 메소드 생성
    // 생성이유: Controller랑 소통 창구 (type: 1 - 생산실적, 2 - 판매실적)
    const fetchRows = (type, setRows) => {
        axios.get('/perform', { params: { type: type } })
            .then((r) => {
                    console.log(r);
                setRows(r.data);
            })
            .catch((error) => { //--- 에러 처리
                console.log(error);
            });
    };

    // 3. type 변경될 때 렌더링 진행
    useEffect(() => {
      fetchRows(type, setRows);
    }, [type]);

    // 4. 실적 출력 양식 [type: 1 - 생산실적, 2 - 판매실적]
    let columns;
    if( type === 1 ) {
        columns = [
            { field: 'prodName', headerName: '제품명', width: 210, align: 'center', headerAlign: 'center' },
            { field: 'prodPrice', headerName: '제품 원가', width: 210, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '원'
            },
            { field: 'averageProductionCount', headerName: '평균 생산량', width: 210, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '개'
            },
            { field: 'totalProductionCount', headerName: '총 생산횟수', width: 210, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '개'
            },
            { field: 'totalProductionAmount', headerName: '총 생산량', width: 210, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '개'
            },
            { field: 'productionPercentage', headerName: '생산비중', width: 210, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => params.value + '%'
            }
        ];
    } else if( type === 2 ) {
        columns = [
            { field: 'prodName', headerName: '제품명', width: 180, align: 'center', headerAlign: 'center' },
            { field: 'prodPrice', headerName: '제품 원가', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '원'
            },
            { field: 'averageSalesPrice', headerName: '평균 판매가', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '원'
            },
            { field: 'totalOrderCount', headerName: '총 판매량', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '개'
            },
            { field: 'totalSalesAmount', headerName: '총 판매 금액', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '원'
            },
            { field: 'profit', headerName: '수익금', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => formatNumber(params.value) + '원'
            },
            { field: 'profitMargin', headerName: '수익률', width: 180, align: 'center', headerAlign: 'center',
            valueFormatter: (params) => params.value + '%'
            },
        ];
    }
    // 수정 필요 내용: 수익률 1,000 자리 쉼표 표기 적용되지 않음

    // 4-1. (option) 숫자 리모델링 함수 생성(1,000 단위 기준으로 표기)
    function formatNumber(value) {
    if (typeof value !== 'number') { return value; }
        return value.toLocaleString();
    }

    return(<>
        <div style={{ height: 400, width: '100%', display:'flex', justifyContent : 'center', marginBottom : '20px'}}>
            <ViewChart chartData={rows} type={type}/>
        </div>
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                rows={rows}
                columns={columns}
                getRowId={type === 1 ? ((row) => row.prodName) : ((row) => row.prodName) }
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 5 },
                    },
                }}
            pageSizeOptions={[5, 10]}
            />
        </div>
    </>);
}