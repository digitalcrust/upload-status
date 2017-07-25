import React, { PropTypes, Component } from 'react'
import '../../css/general.css'


export default class PaginationNavigator extends Component {
  toggleDescriptions() {
    $('.description').toggle()
  }
  render () {
    return (
      <div className='navigation'>
        <a className='previous' href='#'>‹ previous</a>
        <a className='next' href='#'>next ›</a>
        <button className='toggle-descriptions' onClick={()=>{this.toggleDescriptions()}}>toggle descriptions</button>
      </div>)
  }
}
