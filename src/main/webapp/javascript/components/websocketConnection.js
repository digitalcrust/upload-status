import React, {Component} from "react"
import { Button, FormGroup, FormControl } from 'react-bootstrap'
import SockJS from 'sockjs-client'
import webstomp from 'webstomp-client'
import ReactDOM from 'react-dom'

export default class WebsocketConnection extends Component {
  constructor(props) {
    super(props)
    this.handleConnecting = this.handleConnecting.bind(this)
    this.showStatus = this.showStatus.bind(this)
    this.stompClient = null
    this.isConnected = false
  }
  get connected() {
    return this.isConnected
  }
  set connected(value) {
    this.isConnected = value
  }

  handleConnecting() {
    this.connected = true
    this.stompClient.subscribe('/user/queue/pull-status', this.showStatus)
  }

  showStatus(message) {
    console.log(message)
  }

  connectToServer() {
    let socket = new SockJS('/upload-status-websocket')
    this.stompClient = webstomp.over(socket)
    this.stompClient.connect({}, this.handleConnecting)
  }

  sendName(event) {
    event.preventDefault()
    const url = ReactDOM.findDOMNode(this.refs.url).value
    const filename = ReactDOM.findDOMNode(this.refs.fileName).value
    this.stompClient.send('/app/pull-link', JSON.stringify({'link': url, 'fileName': filename}))
  }


  disconnectFromServer() {
    if (this.stompClient != null) {
      this.stompClient.disconnect()
    }
    this.connected = false
  }
  render() {
    return (
      <div className="row">
        <div className="col-md-4">
          <form className="form-inline">
            <FormGroup>
              <label htmlFor="connect">WebSocket connection:</label>
              <Button id="connect" onClick={this.connectToServer.bind(this)}>Connect</Button>
              <Button id="disconnect" onClick={this.disconnectFromServer.bind(this)}>Disconnect</Button>
            </FormGroup>
          </form>
        </div>
        <div className="col-md-4">
          <form className="form-inline">
            <FormGroup>
              <label htmlFor="url">What is the URL?</label>
              <FormControl type="text" id="url" ref="url" placeholder="http://URL-to-file/example.txt" />
              <FormControl type="text" id="fileName" ref="fileName" placeholder="example.txt" />
            </FormGroup>
            <Button id="send" onClick={this.sendName.bind(this)}>Send</Button>
          </form>
        </div>
      </div>
    );
  }
}