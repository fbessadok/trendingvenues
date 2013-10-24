var foursquare_oauth_token = '';

function search(lat, lng) {
  $.post('/search',
    {'lat':lat, 'lng':lng, 'oauth_token':foursquare_oauth_token},
    function(data) {
      $("#venues").empty();
      for (venue in data) {
        $("#venues").append($('<li>').append(data[venue].name + " - Here now " + data[venue].hereNow));
        addMarkerLatLng(data[venue].lat, data[venue].lng, data[venue].name);
      }
    }
  );
}