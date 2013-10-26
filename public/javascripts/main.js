var foursquare_oauth_token = '';

function search(lat, lng) {
  $.post('/search',
    {'lat':lat, 'lng':lng, 'oauth_token':foursquare_oauth_token},
    function(data) {
      $("#venues").empty();
      for (venue in data) {
        $("#venues").append(
          $('<li>').append(
            $('<address>').append('<span class="push-right small tooltip" data-tip-text="' + data[venue].hereNow + ' people here right now" data-tip-color="blue">' + data[venue].hereNow + '</span>').append(
              $('<h6>').append(data[venue].name)
            )
            .append($('<p>').append(data[venue].address + "<br>" + data[venue].city))
          )
        );
        addMarkerLatLng(data[venue].lat, data[venue].lng, data[venue].name);
      }
      Ink.requireModules(['Ink.UI.Tooltip_1'], function (Tooltip) {
        new Tooltip('.tooltip', {where: 'left'});
      });
    }
  );
}