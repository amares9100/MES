import React, { useState, useEffect } from 'react';

function Clock() {
  const [time, setTime] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => {
      setTime(new Date());
    }, 1000);

    return () => {
      clearInterval(timer);
    };
  }, []);

  const year = time.getFullYear();
  const month = time.getMonth() + 1;
  const day = time.getDate();

  return (
    <div style={{ marginLeft : '10px' }}>
      <div>{year}년 {month}월 {day}일</div>
      <div>{time.toLocaleTimeString()}</div>
    </div>
  );
}

export default Clock;