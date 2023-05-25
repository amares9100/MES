import React,{ useState , useEffect } from 'react';
import { BrowserRouter , Routes , Route } from "react-router-dom";
import Header from "./component/Header";
import "./App.css";
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Divider from '@mui/material/Divider';
import InboxIcon from '@mui/icons-material/Inbox';
import DraftsIcon from '@mui/icons-material/Drafts';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import Link from '@mui/material/Link';
import SwipeableDrawer from '@mui/material/SwipeableDrawer';
import Button from '@mui/material/Button';


/*--------------------------- 자재 부분 ------------------------------*/
import Main from "./Main";
import Material from "./component/material/Material";
import MaterialInoutList from "./component/material/MaterialInoutList";

/*--------------------------- 제품 부분 ------------------------------*/
import ProductTab from "./component/product/ProductTab";
import CreateProduct from "./component/product/CreateProduct";
import PlanProduct from "./component/product/PlanProduct";
import ManageProduct from "./component/product/ManageProduct";
/*--------------------------- 멤버 부분 ------------------------------*/
import Login from "./component/member/Login";
import AllowApproval from "./component/member/AllowApproval";
import AllowForm from "./component/member/AllowForm";
import AllowMaterial from "./component/member/AllowMaterial";
import AllowProduct from "./component/member/AllowProduct";
import AllowSales from "./component/member/AllowSales";
/* --------------------------- 판매 부분 --------------------------- */
import SalesHeader from "./component/sales/SalesHeader";
/* --------------------------- 실적 부분 --------------------------- */
import Performance from "./component/performance/Performance";
import PerformanceForm from "./component/performance/PerformanceForm";
import PerformanceProduction from "./component/performance/PerformanceProduction";
import PerformanceSales from "./component/performance/PerformanceSales";



export default function Index( props ) {

 return ( <>
        <BrowserRouter>
            <div className="header">
                <Header />
            </div>
            <div className="content">
                <div className="main-content">
                    <Routes >
                        <Route path="/component/member/Login" element = { <Login /> } />
                        <Route path="/component/material/Material" element = { <Material/> } />
                        <Route path="/component/product/ProductTab" element ={<ProductTab/>}/>
                        <Route path="/component/material/MaterialInoutList/:matID" element = { <MaterialInoutList/> } />
                        <Route path="/component/sales/SalesHeader" element ={<SalesHeader/>}/>
                        <Route path="/component/member/AllowApproval" element = { <AllowApproval /> } />
                        <Route path="/component/performance/Performance" element = { <Performance /> } />
                    </Routes>

                </div>
            </div>
        </BrowserRouter>
    </> );
}
