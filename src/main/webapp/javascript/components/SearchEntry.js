import React, { Component, PropTypes } from 'react'

export default class SearchEntry extends Component {
  render () {
    const { value, onChange, onSearch } = this.props
    return (
      <span>
        <input type="text" id="sbsearch" onChange={e => onChange(e.target.value)} value={value} />
        <button onClick={e => onSearch({value})}>Search</button>
      </span>
    )
  }
}

SearchEntry.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired
}
