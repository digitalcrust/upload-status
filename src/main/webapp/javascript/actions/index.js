import fetch from 'isomorphic-fetch';

export const REQUEST_SB_ITEMS = 'REQUEST_SB_ITEMS';
export const RECEIVE_SB_ITEMS = 'RECEIVE_SB_ITEMS';
export const SELECT_SB_QUERY = 'SELECT_SB_QUERY';
export const INVALIDATE_SB_QUERY = 'INVALIDATE_SB_QUERY';

export function selectSBQuery(sbquery) {
  return {
    type: SELECT_SB_QUERY,
    sbquery
  };
}

export function invalidateSBQuery(sbquery) {
  return {
    type: INVALIDATE_SB_QUERY,
    sbquery
  };
}

function requestSBItems(sbquery) {
  return {
    type: REQUEST_SB_ITEMS,
    sbquery
  };
}

function receiveSBItems(sbquery, json) {
  return {
    type: RECEIVE_SB_ITEMS,
    sbquery,
    sbitems: json.items,
    receivedAt: Date.now()
  };
}

function fetchSBItems(sbquery) {
  console.log(sbquery)
  const url = `https://www.sciencebase.gov/catalog/items?q=${sbquery}&format=json&fields=files,title,body`
  //const url = `https://www.sciencebase.gov/catalog/items?q=${sbquery}&format=json&fields=files,title,body&offset=0&max=1`
  return dispatch => {
    dispatch(requestSBItems(sbquery));
    return fetch(url)
      .then(req => req.json())
      .then(json => dispatch(receiveSBItems(sbquery, json)));
  }
}

function shouldFetchSBItems(state, sbquery) {
  const sbitems = state.sbitemsBySBQuery[sbquery];
  if (!sbitems) {
    return true;
  } else if (sbitems.isFetching) {
    return false;
  } else {
    return sbitems.didInvalidate;
  }
}

export function fetchSBItemsIfNeeded(sbquery) {
  return (dispatch, getState) => {
    if (shouldFetchSBItems(getState(), sbquery)) {
      return dispatch(fetchSBItems(sbquery))
    }
  }
}

export function filterSBItemsWithFiles(items) {
  return items.filter(item =>
    !((item.files === null) || (typeof item.files === 'undefined') || (!item.files.length))
  )
}
