package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {this.movieRepository = movieRepository;}

    public List<Movie> getAll() {
        return movieRepository.findByOrderByNameAsc();
    }

    public void save(Movie movie) {
        this.movieRepository.save(movie);
    }

    public List<Movie> findByName(String name) {
        return this.movieRepository.findByNameContaining(name);
    }
}
