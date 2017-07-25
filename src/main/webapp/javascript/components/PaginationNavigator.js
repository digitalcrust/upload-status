import React, { PropTypes, Component } from 'react'
import '../../css/general.css'


export default class PaginationNavigator extends Component {
  toggleDescriptions() {
    document.querySelectorAll('.description').forEach(el => el.style.display = el.style.display === 'none' ? 'block' : 'none')
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
