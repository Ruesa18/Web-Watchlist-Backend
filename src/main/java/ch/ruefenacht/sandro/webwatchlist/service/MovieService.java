package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.dto.MovieShowDto;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {this.movieRepository = movieRepository;}

    public List<MovieShowDto> getAll() {
        List<Movie> movies = movieRepository.findByOrderByNameAsc();
        List<MovieShowDto> movieShowDtos = new ArrayList<>(List.of());

        for(Movie movie : movies) {
            movieShowDtos.add(new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()));
        }

        return movieShowDtos;
    }

    public MovieShowDto save(Movie movie) {
        this.movieRepository.save(movie);
        return new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl());
    }

    public List<MovieShowDto> findByName(String name) {
        List<Movie> movies = this.movieRepository.findByNameContaining(name);
        List<MovieShowDto> movieShowDtos = new ArrayList<>(List.of());

        for(Movie movie : movies) {
            movieShowDtos.add(new MovieShowDto(movie.getId(), movie.getName(), movie.getImageUrl()));
        }

        return movieShowDtos;
    }
}
