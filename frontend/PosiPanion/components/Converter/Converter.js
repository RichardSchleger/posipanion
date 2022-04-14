
//https://stackoverflow.com/questions/19700283/how-to-convert-time-in-milliseconds-to-hours-min-sec-format-in-javascript
const msToTime = duration => {
    let minutes = Math.floor((duration / (1000 * 60)) % 60),
      hours = Math.floor((duration / (1000 * 60 * 60)) % 24);

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;

    return hours + ':' + minutes;
};

const mAndMsTokmh = (m, ms) => {
  return ms !== 0 ? Math.round((m / 1000 / (ms / 3600000)) * 10) / 10 : 0;
}

export default {msToTime, mAndMsTokmh};
