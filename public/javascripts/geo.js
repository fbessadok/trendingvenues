function load() {
  var myOptions = {
      center: new google.maps.LatLng(37.789404, -122.401042), // TypeSafe coordinate in San Francisco
      zoom: 15,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      mapTypeControl: true,
      scaleControl: true,
      overviewMapControl: true,
      overviewMapControlOptions: {
        opened: true
      }
  }
  
  var map = new google.maps.Map(document.getElementById('map'), myOptions);
  
  var center = new google.maps.LatLng(37.789404, -122.401042);
  
  var marker = new google.maps.Marker({
      position: center,
      map: map,
      title: "Center"
  });
  
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
    marker.setMap(null);
    center = map.getCenter();
    marker = new google.maps.Marker({
      position: center,
      map: map,
      title: "Center"
    });
    scheduleDelayedCallback();
  });
}