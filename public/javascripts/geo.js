var map;
var markers = [];
var cross = 'assets/images/cross.png';

function addMarkerLatLng(lat, lng, name) {
  var location = new google.maps.LatLng(lat, lng);
  addMarker(location, name, '', google.maps.Marker.MAX_ZINDEX)
}

function addMarker(location, name, image, zindex) {
  var marker = new google.maps.Marker({
    position: location,
    title: name,
    icon: image,
    zIndex: zindex,
    map: map
  });
  markers.push(marker);
}

function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

function deleteMarkers() {
  setAllMap(null);
  markers = [];
}

function initMap(lat, lng) {
  var myOptions = {
    center: new google.maps.LatLng(lat, lng),
    zoom: 13,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    mapTypeControl: true,
    scaleControl: true,
    overviewMapControl: true,
    overviewMapControlOptions: {
      opened: true
    }
  }
  
  map = new google.maps.Map(document.getElementById('map'), myOptions);
  
  var center = new google.maps.LatLng(lat, lng);
  
  // marker always on top of the others
  addMarker(center, '', cross, google.maps.Marker.MAX_ZINDEX + 1);
  
  // Google Map Issue https://code.google.com/p/gmaps-api-issues/issues/detail?id=1371
  function fireIfLastEvent() {
    if (lastEvent.getTime() + 500 <= new Date().getTime()) {
      search(center.lat(), center.lng());
    }
  }

  function scheduleDelayedCallback() {
    lastEvent = new Date();
    setTimeout(fireIfLastEvent, 500);
  }
  // End Google Map Issue
  
  google.maps.event.addListener(map, 'center_changed', function() {
    deleteMarkers();
    center = map.getCenter();
    addMarker(center, '', cross, google.maps.Marker.MAX_ZINDEX + 1);
    scheduleDelayedCallback();
  });
  
  search(center.lat(), center.lng());
}