import { combineReducers } from 'redux';
import {
  SELECT_SB_QUERY, INVALIDATE_SB_QUERY,
  REQUEST_SB_ITEMS, RECEIVE_SB_ITEMS
} from '../actions';

function sbQuery(state = 'water', action) {
  switch (action.type) {
  case SELECT_SB_QUERY:
    return action.sbquery;
  default:
    return state;
  }
}

function sbitems(state = {
  isFetching: false,
  didInvalidate: false,
  items: []
}, action) {
  switch (action.type) {
  case INVALIDATE_SB_QUERY:
    return Object.assign({}, state, {
      didInvalidate: true
    })
  case REQUEST_SB_ITEMS:
    return Object.assign({}, state, {
      isFetching: true,
      didInvalidate: false
    });
  case RECEIVE_SB_ITEMS:
    return Object.assign({}, state, {
      isFetching: false,
      didInvalidate: false,
      items: action.sbitems,
      lastUpdated: action.receivedAt
    });
  default:
    return state;
  }
}


function sbitemsBySBQuery(state = { }, action) {
  switch (action.type) {
  case INVALIDATE_SB_QUERY:
  case RECEIVE_SB_ITEMS:
  case REQUEST_SB_ITEMS:
    return Object.assign({}, state, {
      [action.sbquery]: sbitems(state[action.sbquery], action)
    });
  default:
    return state;
  }
}


const rootReducer = combineReducers({
  sbitemsBySBQuery,
  sbQuery
});

export default rootReducer;
