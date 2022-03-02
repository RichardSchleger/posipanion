import type {TimingState, TimingDispatch} from '../../types';

import React from 'react';
import {useLocationTracking} from '../../hooks';
import Map from '../Map';

type Props = {
  state: TimingState,
  dispatch: TimingDispatch,
};

const CurrentLocation = ({state, dispatch}: Props) => {
  useLocationTracking(state, dispatch);
  return <Map state={state} />;
};

export default CurrentLocation;
