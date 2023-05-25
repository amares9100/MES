import React from 'react';

import LoginSocket from './webSocket/LoginSocket'
import SwipeableTemporaryDrawer from './SwipeableTemporaryDrawer'
import HeaderImg from '../component/member/img/Header.jpg';

export default function Header() {
  return (<>
        <div style={{ display: 'flex', justifyContent: 'space-between' , backgroundImage: `url(${HeaderImg})` , backgroundSize: 'cover' , width : '100%' , height : '150px'}}>
            { /*헤더 이미지 구역*/ }
            <div style={{ display: 'flex' , width : '50%' }}>
                <SwipeableTemporaryDrawer />
            </div>
            { /*헤더 소캣 메시지 출력 구역*/ }
            <div style={{ width: '50%' , display: 'flex' , justifyContent: 'right'}}>
                <LoginSocket />
            </div>
         </div>
     </>
  );
}