var map;
var markers = [];

function addMarkerLatLng(lat, lng, name) {
  var location = new google.maps.LatLng(lat, lng);
  addMarker(location, name)
}

function addMarker(location, name) {
  var marker = new google.maps.Marker({
    position: location,
    map: map,
    title: name
  });
  markers.push(marker);
}

function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

function clearMarkers() {
  setAllMap(null);
}

function showMarkers() {
  setAllMap(map);
}

function deleteMarkers() {
  clearMarkers();
  markers = [];
}

function initMap() {
  var myOptions = {
    center: new google.maps.LatLng(37.789404, -122.401042), // TypeSafe coordinate in San Francisco
    zoom: 14,
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    mapTypeControl: true,
    scaleControl: true,
    overviewMapControl: true,
    overviewMapControlOptions: {
      opened: true
    }
  }
  
  map = new google.maps.Map(document.getElementById('map'), myOptions);
  
  var center = new google.maps.LatLng(37.789404, -122.401042);
  
  addMarker(center, "Center");
  
  search(center.lat(), center.lng());
  
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
    addMarker(center, "Center");
    scheduleDelayedCallback();
  });
}