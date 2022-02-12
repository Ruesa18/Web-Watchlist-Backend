package ch.ruefenacht.sandro.webwatchlist.controller;

import ch.ruefenacht.sandro.webwatchlist.dto.MovieCreateDto;
import ch.ruefenacht.sandro.webwatchlist.dto.MovieShowDto;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .map(movie -> new MovieShowDto(movie.getId(), movie.getName()))
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
                .map(movie -> new MovieShowDto(movie.getId(), movie.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(movieShowDtos);
    }

    @PostMapping
    public ResponseEntity<MovieShowDto> createMovie(@RequestBody MovieCreateDto createDto) {
        Movie movie = new Movie();

        movie.setName(createDto.getName());

        this.movieService.save(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MovieShowDto(movie.getId(), movie.getName()));
    }
}
