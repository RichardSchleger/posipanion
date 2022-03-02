import type {TimingState, TimingDispatch} from '../../types';

import React from 'react';
import {useNativeLocationTracking} from '../../hooks';
import Map from '../Map';

type Props = {
  state: TimingState,
  dispatch: TimingDispatch,
};

const CurrentLocation = ({state, dispatch}: Props) => {
  useNativeLocationTracking(state, dispatch);
  return <Map state={state} />;
};

export default CurrentLocation;
