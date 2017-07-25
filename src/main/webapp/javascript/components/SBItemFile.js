import React, { Component } from 'react';
import {formatBytes} from '../numberFormatting'

export default class SBItemFile extends Component {
  movefile(fileurl) {
    console.log(fileurl)
    this.props.stomper.send("/app/pull-link", {}, JSON.stringify({'link': fileurl}));
  }
  render () {
    var stompSocketClient;
    const sbfile = this.props.sbfile
    const fs = formatBytes(sbfile.size)
    return (<section className='files'>
        <a href={sbfile.url} className='filelink'>{sbfile.name}</a>
        <span className='filesize'>{fs}</span>
        <span data-fileurl={sbfile.name} className='movefile'>
          <button onClick={()=>{this.movefile(sbfile.url)}}>
        <img src='s3moveitem.svg'/></button></span>
      </section>)
  }
}
