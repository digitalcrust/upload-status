import React from 'react'
import {render} from 'react-dom'
import SockJS from 'sockjs-client'
import Root from './containers/Root'
import User from './components/user'
import WebsocketConnection from './components/websocketConnection'
import '../css/general.css'
import classNames from 'classnames'

render(
  <div className={classNames('App', 'container')}>
    <a className="login" href="/login/google">Login</a>
    <WebsocketConnection/>
    <User/>
    <Root/>
  </div>,
  document.getElementById('content')
)
