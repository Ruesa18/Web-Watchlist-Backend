package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.MovieCreateDto;
import ch.ruefenacht.sandro.webwatchlist.dto.MovieShowDto;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.service.MovieService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieShowDto>> getAll() {
        List<MovieShowDto> movieShowDtos = this.movieService.getAll().stream()
                .map(movie -> new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(movieShowDtos);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        List<Movie> movies = this.movieService.findByName(name);

        if(movies.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<MovieShowDto> movieShowDtos = movies.stream()
                .map(movie -> new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(movieShowDtos);
    }

    @PostMapping
    public ResponseEntity<MovieShowDto> createMovie(@RequestBody MovieCreateDto createDto) throws IOException, JSONException {
        Movie movie = new Movie();

        movie.setName(createDto.getName());

        // TODO Fix this whole mess of loading an image. Frontend should do it...
        URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=15d2ea6d0dc1d476efbca3eba2b9bbfb&query=" + URLEncoder.encode(createDto.getName(), StandardCharsets.UTF_8.toString()));
        System.out.println(url.getQuery());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if(con.getResponseCode() == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONObject obj = new JSONObject(String.valueOf(content));
            if(obj.getJSONArray("results").length() > 0) {
                String imageUrl = obj.getJSONArray("results").getJSONObject(0).get("poster_path").toString();
                movie.setImageUrl("http://image.tmdb.org/t/p/w500/" + imageUrl);
            }else{
                movie.setImageUrl("");
            }
        }

        this.movieService.save(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()));
    }
}
