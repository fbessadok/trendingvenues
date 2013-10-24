var foursquare_oauth_token = '';

function search(lat, lng) {
  $.post('/search',
    {'lat':lat, 'lng':lng, 'oauth_token':foursquare_oauth_token},
    function(data) {
      $("#venues").empty();
      for (venue in data) {
        $("#venues").append($('<li>').append(data[venue].name + " : [" + data[venue].lat + "," + data[venue].lng + "] - Here now " + data[venue].hereNow));
      }
    }
  );
}