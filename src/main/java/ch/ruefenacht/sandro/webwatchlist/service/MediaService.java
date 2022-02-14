package ch.ruefenacht.sandro.webwatchlist.service;

import ch.ruefenacht.sandro.webwatchlist.model.Media;
import ch.ruefenacht.sandro.webwatchlist.model.Movie;
import ch.ruefenacht.sandro.webwatchlist.repository.MovieRepository;
import ch.ruefenacht.sandro.webwatchlist.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    @Autowired
    public MediaService(MovieRepository movieRepository, SeriesRepository seriesRepository) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    public Optional<? extends Media> findMediaById(UUID uuid) {
        Optional<Movie> movie = this.movieRepository.findById(uuid);

        if(movie.isEmpty()) {
            return this.seriesRepository.findById(uuid);
        }

        return movie;
    }
}
