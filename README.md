# Welcome to Trending Venues

The **Trending Venues** service helps you discover places around you that trends right now in social media communities.

This little project was inspired by TweetsGallery from [Guillaume Bort](https://github.com/guillaumebort/devoxxfr2013) and [Sadek Drobi](https://gist.github.com/sadache/5792650) and by [gmaptools](https://github.com/janosgyerik/gmaptools) by Janos Gyerik.

Trending Venues is built on top of

* [Play Framework v2](http://www.playframework.com/), [Scala](http://www.scala-lang.org/)
* [Foursquare API v2](https://developer.foursquare.com/)
* [Ink UI Framework v2.2.1](http://ink.sapo.pt/)
* [Less CSS](http://lesscss.org/) (Included in Play Framework)
* [Google Maps API v3](https://developers.google.com/maps/documentation/javascript/)
* [jQuery 1.9.0](http://jquery.com/)

# Live Demo

A demo is hosted here, try it! :)
http://obscure-plateau-1268.herokuapp.com/

# Configuration

In order to run Trending Venues yourself you need to create a [foursquare app](https://fr.foursquare.com/developers/apps) and to fill the *client_id* and *client_secret* in **Venue.scala** with the ones from the app you just created.

# TODO

* Handle errors, bad requests (incorrect lat & lng for example) and Foursquare errors
* Implement Foursquare authentication to raise the overall rate limit
* Display different marker icons for each venue category
* Display user's checkins

-----------------

2013, by [Firas Bessadok](http://firas.bessadok.com/)