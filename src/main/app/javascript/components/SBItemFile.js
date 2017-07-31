import React, { Component } from 'react';
import { formatBytes } from '../numberFormatting'
import { Button } from 'react-bootstrap'

export default class SBItemFile extends Component {
  movefile(fileurl, filename) {
    console.log(fileurl, filename)
    document.getElementById('transferstatus').innerHTML = ''
    this.props.stomper.send("/app/pull-link",JSON.stringify({'link': fileurl, 'fileName': filename}));
  }
  render () {
    var stompSocketClient;
    const sbfile = this.props.sbfile
    const fs = formatBytes(sbfile.size)
    return (<section className='files'>
        <span data-fileurl={sbfile.name} className='movefile'>
          <Button data-toggle="popover" title="Copy File to S3" onClick={()=>{this.movefile(sbfile.url, sbfile.name)}}><span className='glyphicon glyphicon-upload pull-left pull-up' /></Button>
        </span>
        <a href={sbfile.url} className='filelink'>{sbfile.name}</a>
        <span className='filesize'>{fs}</span>
      </section>)
  }
}
