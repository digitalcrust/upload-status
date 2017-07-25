import React, { Component } from 'react'
import { Provider } from 'react-redux'
import configureStore from '../store/configureStore'
import AsyncApp from './AsyncApp'

const store = configureStore()
// var socket = new SockJS('http://localhost:9000/upload-status-websocket')
// var stomper = Stomp.over(socket)
// stomper.connect({}, function (frame) {
//     console.log('Connected: ' + frame)
//     stomper.subscribe('/user/queue/pull-status', function (statusupdate) {
//       console.log('uploading...')
//       $("#transferstatus").html(JSON.parse(statusupdate.body).message);
// //        $("#greetings").append("<tr><td>" + JSON.parse(greeting.body).content + "</td></tr>")
//     })
// })

export default class Root extends Component {
  render() {
{/*    return (
      <Provider store={store} >
        <AsyncApp stomper={stomper} />
      </Provider>
    ) */}
    return (
      <Provider store={store} >
        <AsyncApp />
      </Provider>
    )
  }
}
