import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import { selectSBQuery, fetchSBItemsIfNeeded, invalidateSBQuery } from '../actions'
import PaginationNavigator from '../components/PaginationNavigator'
import SearchEntry from '../components/SearchEntry'
import SBItems from '../components/SBItems'

class AsyncApp extends Component {
  constructor(props) {
    super(props)
    this.handleSearchChange = this.handleSearchChange.bind(this)
    this.handleSearchRequest = this.handleSearchRequest.bind(this)
    this.handleRefreshClick = this.handleRefreshClick.bind(this)
  }

  componentDidMount() {
    const { dispatch, sbQuery } = this.props
    dispatch(fetchSBItemsIfNeeded(sbQuery))
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.sbQuery !== this.props.sbQuery) {
      const { dispatch, sbQuery } = nextProps
      dispatch(fetchSBItemsIfNeeded(sbQuery))
    }
  }

  handleSearchChange(nextsbquery) {
    this.props.dispatch(selectSBQuery(nextsbquery));
  }

  handleSearchRequest(nextsbquery) {
    this.props.dispatch(selectSBQuery(nextsbquery.value));
  }

  handleRefreshClick(e) {
    e.preventDefault();
    const { dispatch, sbQuery } = this.props;
    dispatch(invalidateSBQuery(sbQuery));
    dispatch(fetchSBItemsIfNeeded(sbQuery));
  }

  render () {
    const { sbQuery, sbitems, isFetching, lastUpdated } = this.props;
    return (
      <div>
        <div id='transferstatus'></div>
        <SearchEntry value={sbQuery}
                onChange={this.handleSearchChange}
                onSearch={this.handleSearchRequest} />
        <p>
          {lastUpdated &&
            <span>
              Last updated at {new Date(lastUpdated).toLocaleTimeString()}.
              {' '}
            </span>
          }
          {!isFetching &&
            <a href='#'
               onClick={this.handleRefreshClick}>
              Refresh
            </a>
          }
        </p>
        {isFetching && sbitems.length === 0 &&
          <h2>Loading...</h2>
        }
        {!isFetching && sbitems.length === 0 &&
          <h2>Empty.</h2>
        }
        {sbitems.length > 0 &&
          <div style={{ opacity: isFetching ? 0.5 : 1 }}>
            <SBItems sbitems={sbitems} stomper={this.props.stomper}/>
          </div>
        }
        <PaginationNavigator></PaginationNavigator>
      </div>
    );
  }
}

AsyncApp.propTypes = {
  sbQuery: PropTypes.string.isRequired,
  sbitems: PropTypes.array.isRequired,
  isFetching: PropTypes.bool.isRequired,
  lastUpdated: PropTypes.number,
  dispatch: PropTypes.func.isRequired
}

function mapStateToProps(state) {
  const { sbQuery, sbitemsBySBQuery } = state;
  const {
    isFetching,
    lastUpdated,
    items: sbitems
  } = sbitemsBySBQuery[sbQuery] || {
    isFetching: true,
    items: []
  };

  return {
    sbQuery,
    sbitems,
    isFetching,
    lastUpdated
  };
}

export default connect(mapStateToProps)(AsyncApp);
