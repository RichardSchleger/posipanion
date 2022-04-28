/**
 * @format
 * @flow
 */

import {useReducer} from 'react';

export const Actions = {
  Start: 'Start',
  Stop: 'Stop',
  LocationRequested: 'LocationRequested',
  UpdatePosition: 'UpdatePosition',
};

export const initialState = {
  // granted: undefined,
  position: null,
  running: true,
};

export const getInitialState = () => {
  return {...initialState};
};

export const reducer = (
  state,
  action,
) => {
  switch (action.type) {
    case Actions.Start:
      return {
        ...state,
        running: true,
        position: null,
      };

    case Actions.Stop:
      return {
        ...state,
        running: false,
      };

    case Actions.LocationRequested:
      return {
        ...state,
        granted: !!action.granted,
      };

    case Actions.UpdatePosition:
      return {
        ...state,
        position: {
          accuracy: action.position.accuracy,
          lat: action.position.lat,
          lng: action.position.lng,
          alti: action.position.alti,
          timestamp: action.position.timestamp,
        },
      };

    default:
      return state;
  }
};

export const useTimingReducer = () => {
  return useReducer<TimingState, TimingAction>(reducer, null, () =>
    getInitialState(),
  );
};
