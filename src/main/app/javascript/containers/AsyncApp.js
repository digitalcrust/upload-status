import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import { selectSBQuery, fetchSBItemsIfNeeded, invalidateSBQuery } from '../actions'
import PaginationNavigator from '../components/PaginationNavigator'
import SearchEntry from '../components/SearchEntry'
import SBItems from '../components/SBItems'
import SockJS from 'sockjs-client'
import webstomp from 'webstomp-client'
import classNames from 'classnames'

class AsyncApp extends Component {
  constructor(props) {
    super(props)
    this.handleSearchChange = this.handleSearchChange.bind(this)
    this.handleSearchRequest = this.handleSearchRequest.bind(this)
    this.handleRefreshClick = this.handleRefreshClick.bind(this)
    this.handleConnecting = this.handleConnecting.bind(this)
    this.showStatus = this.showStatus.bind(this)
    this.stomper = null
    this.isConnected = false
  }

  handleConnecting() {
    if (this.stomper.connected) {
      this.stomper.subscribe('/user/queue/pull-status', this.showStatus)
    }
  }

  showStatus(statusupdate) {
    if (statusupdate != null && statusupdate.body != null) {
      const body = JSON.parse(statusupdate.body)
      if (body.message) {
        document.getElementById('transferstatus').innerHTML = body.message
        console.log(body.message)
      }
    }
  }

  connectToServer() {
    let socket = new SockJS('/upload-status-websocket')
    this.stomper = webstomp.over(socket)
    this.stomper.connect({}, this.handleConnecting)
  }


  componentDidMount() {
    const { dispatch, sbQuery } = this.props
    dispatch(fetchSBItemsIfNeeded(sbQuery))
    this.connectToServer()
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
      <div className="sbitems">
        <div className={classNames('row', 'transferstatus-wrapper')}>
          <div className={classNames('col-sm-12', '')}>
            <label>Transfer Status: </label>
            <span id='transferstatus' className='status'></span>
          </div>
        </div>
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
            <SBItems sbitems={sbitems} stomper={this.stomper}/>
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
