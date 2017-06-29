# Popular Movies
Popular Movies is an Android application that allows users to discover the most popular movies and mark them as favorites as they want. It presents the users with an grid arrangement of movie posters sorted by a specific sort order.

This is my project for the [Udacity's Developing Android Apps Course](https://www.udacity.com/course/developing-android-apps--ud853).

### Features:
• The application allows the users to change sort order via settings, and it can be one of these options:
  1. Most popular movies.
  2. Highest-rated movies.
  3. Favorite movies.
  
• The application allows the users to tap on a movie poster and move to a details screen with additional information such as:
  1. Movie original title.
  2. Movie poster.
  3. Movie overview.
  4. Movie release date.
  5. Users rating. 
  
• The application allows the users to: 
  1. View and play trailers.
  2. Read reviews of the selected movie.
  3. Mark a movie as a favorite and unmark it anytime.

### Built With:
- [Themoviedb.org](https://www.themoviedb.org/documentation/api) API: A web service to search for movies and TV shows based on filters or definable values like ratings, certifications or release dates.
- [Picasso](http://square.github.io/picasso/): A powerful image downloading and caching library. I used Picasso to easily load album art thumbnails into my views. It handles loading the images on a background thread, image decompression and caching the images.
- [Retrofit](https://square.github.io/retrofit/): A type-safe HTTP client library. I used Retrofit in turning my HTTP API into a Java interface and in object conversion to request body (JSON object).

### License:
This software is licensed under the [Modified BSD License](https://opensource.org/licenses/BSD-3-Clause).