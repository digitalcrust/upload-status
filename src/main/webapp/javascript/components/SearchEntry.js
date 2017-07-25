import React, { Component, PropTypes } from 'react'

export default class SearchEntry extends Component {
  render () {
    const { value, onChange, onSearch } = this.props
    return (
      <div className="row">
        <div className="col-md-12">
          <span>
            <input type="text" id="sbsearch" onChange={e => onChange(e.target.value)} value={value} />
            <button onClick={e => onSearch({value})}>Search</button>
          </span>
        </div>
      </div>
    )
  }
}

SearchEntry.propTypes = {
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired
}
