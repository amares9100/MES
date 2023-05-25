import React,{ useState , useEffect } from 'react';
import axios from 'axios';

import PerformanceForm from './PerformanceForm';

export default function PerformanceSales(props) {
    return <PerformanceForm type={2} />;
}