package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {this.movieRepository = movieRepository;}

    public Set<Movie> getAll() {
        return new HashSet<>(movieRepository.findAll());
    }

    public void save(Movie movie) {
        this.movieRepository.save(movie);
    }

    public List<Movie> findByName(String name) {
        return this.movieRepository.findByNameContaining(name);
    }
}
