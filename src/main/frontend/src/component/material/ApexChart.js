import React, { useEffect } from "react";
import Chart from "react-apexcharts";

const ApexChart = (props) => {
  // props로 가져온 데이터 확인 + props가 넘어올때마다 랜더링
  useEffect(() => {
    console.log(props.categories);
    console.log(props.data);
  }, [props]);
  // 차트 옵션
  const options = {
    chart: {
      id: "basic-bar"
    },
    xaxis: {
      categories: props.categories
    }

  };

  const series = [
    {
      name: "총재고량",
      data: props.data
    }
  ];

  return (
    <div className="ApexChart">
      <div className="row">
        <div className="mixed-chart">
          <Chart options={options} series={series} type="line" width="100%" height="400"/>
        </div>
      </div>
    </div>
  );
};

export default ApexChart;