const calculateDistanceBetweenLatLonEle = (
  lat1,
  lon1,
  ele1,
  lat2,
  lon2,
  ele2,
) => {
  const height = ele2 - ele1;
  const theta = lon1 - lon2;
  let distance =
    Math.sin(degreesToRadians(lat1)) * Math.sin(degreesToRadians(lat2)) +
    Math.cos(degreesToRadians(lat1)) *
      Math.cos(degreesToRadians(lat2)) *
      Math.cos(degreesToRadians(theta));
  distance = Math.acos(distance);
  distance = radiansToDegrees(distance);
  distance = distance * (60 * 1.1515 * 1.609344 * 1000);
  distance = Math.pow(distance, 2) + Math.pow(height, 2);
  distance = Math.sqrt(distance);
  return distance;
};

function degreesToRadians(degrees) {
  return degrees * (Math.PI / 180);
}

function radiansToDegrees(radians) {
  return radians * (180 / Math.PI);
}

export default {calculateDistanceBetweenLatLonEle};
