import React, { PropTypes, Component } from 'react'
import {formatBytes} from '../actions'
import SBItemFiles from './SBItemFiles'
import '../../css/general.css'

export default class SBItems extends Component {
  render () {
    return (
      <div>
        {this.props.sbitems.map((item, i) =>
          <section key={i} className='scienceBaseItem'>
            <a href={item.link.url} className='title'>{item.title}</a>
            <p className='description'>{item.body}</p>
            <SBItemFiles sbfiles={item.files} movefile={this.props.movefile} stomper={this.props.stomper}></SBItemFiles>
          </section>
        )}
      </div>
    )
  }
}

SBItems.propTypes = {
  sbitems: PropTypes.array.isRequired
}
