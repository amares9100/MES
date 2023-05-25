import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import AllowForm from './AllowForm';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
 Panel, Pagination, Container, Button, Box, InputLabel, MenuItem,
 FormControl, Select, Stack, TextField } from '@mui/material';

export default function AllowMaterial() {
    return <AllowForm type={1} />;
}