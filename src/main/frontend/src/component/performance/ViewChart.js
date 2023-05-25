import React,{ useState , useEffect } from 'react';
import axios from 'axios';
import Chart from 'chart.js/auto';
import { Doughnut } from 'react-chartjs-2';
import { Bar } from 'react-chartjs-2';//

//판매/생산 실적 차트
export default function ViewChart(props) {
    //해당 표를 띄울 데이터 저장
    const[showInfo, setShowInfo] = useState([]);

    //lables 값만 저장
    const[showLabel, setShowLabel] = useState([]);
    //chartData걊만 저장
    const[showData, setShowData] = useState([]);

    //생산량 막대그래프
    const[barData, setBarData] = useState([]);

    //chartInfo 정보 설정
    const chartInfo = {
      labels: showLabel, // X 축 레이블
      datasets: [
        {
          label: props.type == 1 ? '생산실적' : '판매 실적', // 데이터셋 레이블
          data: showData, // 데이터 값
          backgroundColor: generateBackgroundColors(showLabel.length), // 데이터셋 배경색
          borderColor: 'black', // 데이터셋 테두리 색
          borderWidth: 1, // 데이터셋 테두리 너비
        },
      ],
    };

    //chartInfo2 정보 설정 => 막대 그래프
    const chartInfo2 = {
       labels: showLabel, // X 축 레이블
       datasets: [
         {
           label: '생산량', // 데이터셋 레이블
           data: barData, // 데이터 값
           backgroundColor: '#5F84A2', // 데이터셋 배경색
           borderColor: 'white', // 데이터셋 테두리 색
           borderWidth: 1, // 데이터셋 테두리 너비
         },
       ],
     };

    //실질적인 색상 지정 함수
    function selectColor(){
        const r = Math.floor(Math.random() * 256);
        const g = Math.floor(Math.random() * 256);
        const b = Math.floor(Math.random() * 256);
        const color = `rgba(${r}, ${g}, ${b}, 0.5)`;

        return color;
    }

    //자동 색상 지정
    function generateBackgroundColors(count) {
      const colors = [];
      for (let i = 0; i < count; i++) {
        const color = selectColor();
        if(colors.find(f => f === color)){
            i--;
            continue;
        }
        colors.push(color);
      }
      return colors;
    }

    useEffect(() => {
        console.log(props.chartData);
        setShowInfo(props.chartData)
    }, [props.chartData]) //해당 표를 띄울 데이터 받아오기(매개변수로) => 매개변수 값이 바뀔때마다 표 정보를 바꾸기

    useEffect(() => {

      if(props.type == 1){  //생산 실적일 경우
          //라벨 값
          const labelInfo = showInfo.map(item => item.prodName);
          setShowLabel(labelInfo);
          console.log("라벨 값 : " + labelInfo)

          //실질적으로 chart data 값(도넛 그래프)
          const dataInfo = showInfo.map(item => item.productionPercentage); //생산비율
          setShowData(dataInfo);
          console.log("데이터 값 : " + dataInfo)

          //실질적으로 chart data 값(막대의 길이를 결정하는)
          const barInfo = showInfo.map(item => item.totalProductionAmount); //생산비율
          setBarData(barInfo);
          console.log("데이터 값 : " + barInfo)

      }else if(props.type == 2){ //판매 실적일 경우
          //라벨 값
          const labelInfo = showInfo.map(item => item.prodName);
          setShowLabel(labelInfo);
          console.log("라벨 값 : " + labelInfo)

          //실질적으로 chart data 값(막대의 길이를 결정하는)
          const dataInfo = showInfo.map(item => item.profit); //수익금
          setShowData(dataInfo);
          console.log("데이터 값 : " + dataInfo)
      }
    }, [showInfo]);

    //데이터가 안들어왔으면 다른 값을 리턴한다.
     if (!chartInfo ||showInfo.length === 0) {
        return <div>Loading...</div>; // 데이터 로딩 중인 동안에 표시할 내용
      }

    //데이터가 들어왔을 때
     return (
        <>
            <div style={{ display: 'flex', justifyContent: 'center' }}>
                {props.type == 1 ? <Bar style={{marginRight : '30px'}} data={chartInfo2} /> : null}
                <Doughnut data={chartInfo} />
            </div>
        </>
    )

}